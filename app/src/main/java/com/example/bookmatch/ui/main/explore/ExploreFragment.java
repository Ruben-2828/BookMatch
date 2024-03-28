package com.example.bookmatch.ui.main.explore;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bookmatch.R;
import com.example.bookmatch.adapter.CardStackAdapter;
import com.example.bookmatch.databinding.FragmentExploreBinding;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.ui.main.BookViewModel;
import com.example.bookmatch.ui.main.BookViewModelFactory;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;

public class ExploreFragment extends Fragment implements CardStackListener {

    private static final String TAG = ExploreFragment.class.getSimpleName();
    private static final String GENRE_KEY = "genre";
    private static final String CURRENT_ITEM_KEY = "current";
    private int currentItemIndex = 0;

    private FragmentExploreBinding binding;
    private CardStackView cardStackView;
    private CardStackLayoutManager cardStackManager;
    private CardStackAdapter cardStackAdapter;
    private BookViewModel bookViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(inflater, container, false);

        BookViewModelFactory factory = new BookViewModelFactory(requireActivity().getApplication());
        bookViewModel = new ViewModelProvider(this, factory).get(BookViewModel.class);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayAdapter<String> genreAdapter = new ArrayAdapter<String>(getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.search_genre));
        binding.genre.setAdapter(genreAdapter);

        binding.noMoreBooks.setVisibility(View.INVISIBLE);

        cardStackView = binding.cardStackView;
        cardStackManager = new CardStackLayoutManager(getContext(), this);
        cardStackAdapter = new CardStackAdapter(new ArrayList<>(), new CardStackAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                Bundle args = new Bundle();
                args.putParcelable("book", book);

                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_navigation_explore_to_navigation_book, args);
            }
        });

        cardStackView.setLayoutManager(cardStackManager);
        cardStackView.setAdapter(cardStackAdapter);
        setupButtons();

        binding.genre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (binding.explorePlaceholder.getVisibility() == View.VISIBLE) {
                    binding.explorePlaceholder.setVisibility(View.INVISIBLE);
                    binding.noMoreBooks.setVisibility(View.VISIBLE);
                }


                cardStackAdapter.setBooks(new ArrayList<>());
                bookViewModel.fetchBooks(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bookViewModel.getExtractedBooksLiveData().observe(getViewLifecycleOwner(), books -> {
            if (books != null) {
                cardStackAdapter.addBooks(books);
            }
        });

        // Restore view
        if (savedInstanceState != null) {
            binding.genre.setText(savedInstanceState.getString(GENRE_KEY));
            currentItemIndex = savedInstanceState.getInt(CURRENT_ITEM_KEY, 0);
            Log.d(TAG, "Restored current item index: " + currentItemIndex);
        } else {
            binding.genre.setText(genreAdapter.getItem(0), false);
        }
    }

    @Override
    public void onCardDragging(@NonNull Direction direction, float ratio) {
    }

    @Override
    public void onCardSwiped(@NonNull Direction direction) {

        int position = cardStackManager.getTopPosition() - 1;
        Book currentBook = cardStackAdapter.getBook(position);

        if (direction == Direction.Right) {
            Log.d(TAG, "saved book as favorite: " + cardStackAdapter.getBook(position).getTitle());
            bookViewModel.saveBook(currentBook, true);
        }
        if (direction == Direction.Left) {
            Log.d(TAG, "skipped book: " + cardStackAdapter.getBook(position).getTitle());
        }
        if (direction == Direction.Bottom) {
            Log.d(TAG, "saved book as deleted: " + cardStackAdapter.getBook(position).getTitle());
            bookViewModel.saveBook(currentBook, false);
        }
    }

    @Override
    public void onCardRewound() {
        Log.d(TAG, "onCardRewound: " + cardStackManager.getTopPosition());
    }

    @Override
    public void onCardCanceled() {
        Log.d(TAG, "onCardCanceled: " + cardStackManager.getTopPosition());
    }

    @Override
    public void onCardAppeared(@NonNull View view, int position) {
        TextView titleView = view.findViewById(R.id.book_title);
        Log.d(TAG, "onCardAppeared: (" + position + ") " + titleView.getText());
    }

    @Override
    public void onCardDisappeared(@NonNull View view, int position) {
        TextView titleView = view.findViewById(R.id.book_title);
        Log.d(TAG, "onCardDisappeared: (" + position + ") " + titleView.getText());

        if(cardStackManager.getTopPosition() == cardStackAdapter.getItemCount() - 5) {
            String genre = String.valueOf(binding.genre.getText());
            bookViewModel.fetchBooks(genre);
        }
        if(cardStackManager.getTopPosition() == cardStackAdapter.getItemCount() - 1) {
            binding.noMoreBooks.setVisibility(View.VISIBLE);
        }
    }

    private void setupButtons() {
        binding.skipButton.setOnClickListener(v -> {
            SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Left)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(new AccelerateInterpolator())
                    .build();
            cardStackManager.setSwipeAnimationSetting(setting);
            cardStackView.swipe();
        });

        binding.deleteButton.setOnClickListener(v -> {
            SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Bottom)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(new AccelerateInterpolator())
                    .build();
            cardStackManager.setSwipeAnimationSetting(setting);
            cardStackView.swipe();
        });

        binding.likeButton.setOnClickListener(v -> {

            SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Right)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(new AccelerateInterpolator())
                    .build();
            cardStackManager.setSwipeAnimationSetting(setting);
            cardStackView.swipe();
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (binding != null) {
            String genre = String.valueOf(binding.genre.getText());
            outState.putString(GENRE_KEY, genre);
            outState.putInt(CURRENT_ITEM_KEY, cardStackManager.getTopPosition());
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        // Not the finest thing to do, see this: https://github.com/material-components/material-components-android/issues/2012
        ArrayAdapter<String> genreAdapter = new ArrayAdapter<String>(getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.search_genre));
        binding.genre.setAdapter(genreAdapter);
    }
}