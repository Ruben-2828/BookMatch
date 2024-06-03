package com.example.bookmatch.ui.main.explore;

import static com.example.bookmatch.utils.Constants.LAST_UPDATE_PREF;
import static com.example.bookmatch.utils.Constants.SAVE_INTERVAL;
import static com.example.bookmatch.utils.Constants.SHARED_PREF_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ArrayAdapter;
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
import com.example.bookmatch.model.Result;
import com.example.bookmatch.ui.main.BookViewModel;
import com.example.bookmatch.ui.main.BookViewModelFactory;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;

public class ExploreFragment extends Fragment implements CardStackListener {

    private static final String GENRE_KEY = "genre";

    private FragmentExploreBinding binding;
    private CardStackView cardStackView;
    private CardStackLayoutManager cardStackManager;
    private CardStackAdapter cardStackAdapter;
    private BookViewModel bookViewModel;
    private long lastUpdate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(inflater, container, false);

        BookViewModelFactory factory = new BookViewModelFactory(requireActivity().getApplication());
        bookViewModel = new ViewModelProvider(this, factory).get(BookViewModel.class);
        bookViewModel.setStartIndex(0);

        SharedPreferences sp = getContext().getSharedPreferences(
                SHARED_PREF_NAME, Context.MODE_PRIVATE);
        lastUpdate = sp.getLong(LAST_UPDATE_PREF, 0);

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

        ArrayAdapter<String> genreAdapter = new ArrayAdapter<>(getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.search_genre));
        binding.genre.setAdapter(genreAdapter);

        binding.noMoreBooks.setVisibility(View.INVISIBLE);

        cardStackView = binding.cardStackView;
        cardStackManager = new CardStackLayoutManager(getContext(), this);
        cardStackAdapter = new CardStackAdapter(book -> {
            Bundle args = new Bundle();
            args.putParcelable("book", book);

            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_navigation_explore_to_navigation_book, args);
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

                cardStackAdapter.clearBooks();
                bookViewModel.fetchBooks(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bookViewModel.getExtractedBooksLiveData().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccess()){
                ArrayList<Book> books = ((Result.BooksResponseSuccess)result).getBooks();
                if (books != null) {
                    cardStackAdapter.addBooks(books);

                    long currentTime = System.currentTimeMillis();
                    if(currentTime - lastUpdate > SAVE_INTERVAL){
                        bookViewModel.saveMockBooks(books);

                        lastUpdate = System.currentTimeMillis();

                        //Saving last update in preferences
                        SharedPreferences sp = getContext().getSharedPreferences(
                                SHARED_PREF_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putLong(LAST_UPDATE_PREF, lastUpdate);
                        editor.apply();
                    }
                }
            }else{
                String message = ((Result.Error)result).getMessage();
                ArrayList<Book> books = bookViewModel.getMockBooks();
                cardStackAdapter.addBooks(books);
                showSnackBar(message);
            }
        });

        // Restore view
        if (savedInstanceState != null) {
            binding.genre.setText(savedInstanceState.getString(GENRE_KEY));
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
        FloatingActionButton likeButton = binding.likeButton;

        int messageResId;
        boolean saveBook = false;

        switch (direction) {
            case Right:
                messageResId = R.string.book_saved;
                saveBook = true;
                break;
            case Left:
                messageResId = R.string.book_skipped;
                break;
            case Bottom:
                messageResId = R.string.book_deleted;
                break;
            default:
                return;
        }

        showSnackbar(messageResId, likeButton);

        if (saveBook) {
            bookViewModel.saveBook(currentBook, true);
        } else if (direction == Direction.Bottom) {
            bookViewModel.saveBook(currentBook, false);
        }
    }

    private void showSnackbar(int messageResId, View anchorView) {
        Snackbar snackbar = Snackbar.make(binding.getRoot(), getString(messageResId), Snackbar.LENGTH_SHORT);
        snackbar.setDuration(500);
        snackbar.setAnchorView(anchorView);
        snackbar.show();
    }

    @Override
    public void onCardRewound() {
    }

    @Override
    public void onCardCanceled() {
    }

    @Override
    public void onCardAppeared(@NonNull View view, int position) {
        TextView titleView = view.findViewById(R.id.book_title);
    }

    @Override
    public void onCardDisappeared(@NonNull View view, int position) {

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
    public void onResume() {
        super.onResume();

        // Not the finest thing to do, see this: https://github.com/material-components/material-components-android/issues/2012
        ArrayAdapter<String> genreAdapter = new ArrayAdapter<String>(getContext(),
               androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.search_genre));
        binding.genre.setAdapter(genreAdapter);
    }

    private void showSnackBar(String message){
        BottomNavigationView bottomNavigationView = requireActivity().findViewById(R.id.nav_view);
        Snackbar snackbar = Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT);
        snackbar.setAnchorView(bottomNavigationView);
        snackbar.show();
    }
}