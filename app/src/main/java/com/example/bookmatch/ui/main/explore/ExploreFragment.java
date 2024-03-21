package com.example.bookmatch.ui.main.explore;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bookmatch.R;
import com.example.bookmatch.adapter.CardStackAdapter;
import com.example.bookmatch.data.database.BookDao;
import com.example.bookmatch.data.database.BookRoomDatabase;
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
import java.util.Arrays;
import java.util.List;

public class ExploreFragment extends Fragment implements CardStackListener {

    private static final String TAG = ExploreFragment.class.getSimpleName();

    private FragmentExploreBinding binding;
    private CardStackView cardStackView;
    private CardStackLayoutManager cardStackManager;
    private CardStackAdapter cardStackAdapter;
    private BookViewModel bookViewModel;
    private BookDao bookDao;
    private final ArrayList<Book> bookList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExploreBinding.inflate(inflater, container, false);

        BookViewModelFactory factory = new BookViewModelFactory(requireActivity().getApplication());
        bookViewModel = new ViewModelProvider(this, factory).get(BookViewModel.class);
        bookDao = BookRoomDatabase.getDatabase(requireContext()).bookDao();

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
                bookViewModel.fetchBooks(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bookViewModel.getAllBooks().observe(getViewLifecycleOwner(), books -> {
            Log.d(TAG, "QUa:" +books);
            if (books != null) {
                cardStackAdapter.setBooks(books);
            }
        });

        binding.genre.setText(getResources().getStringArray(R.array.search_genre)[0]);
    }

    @Override
    public void onCardDragging(@NonNull Direction direction, float ratio) {
        Log.d("CardStackView", "onCardDragging: d = " + direction.name() + ", r = " + ratio);
    }

    @Override
    public void onCardSwiped(@NonNull Direction direction) {
        Log.d("CardStackView", "onCardSwiped: p = " + cardStackManager.getTopPosition() + ", d = " + direction);

        if (direction == Direction.Right) {

        }
    }

    @Override
    public void onCardRewound() {
        Log.d("CardStackView", "onCardRewound: " + cardStackManager.getTopPosition());
    }

    @Override
    public void onCardCanceled() {
        Log.d("CardStackView", "onCardCanceled: " + cardStackManager.getTopPosition());
    }

    @Override
    public void onCardAppeared(@NonNull View view, int position) {
        TextView textView = view.findViewById(R.id.book_title);
        Log.d("CardStackView", "onCardAppeared: (" + position + ") " + textView.getText());
    }

    @Override
    public void onCardDisappeared(@NonNull View view, int position) {
        TextView textView = view.findViewById(R.id.book_title);
        Log.d("CardStackView", "onCardDisappeared: (" + position + ") " + textView.getText());
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

        /*binding.rewindButton.setOnClickListener(v -> {
            SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                    .setDirection(Direction.Bottom)
                    .setDuration(Duration.Normal.duration)
                    .setInterpolator(new DecelerateInterpolator())
                    .build();
            cardStackManager.setSwipeAnimationSetting(setting);
            cardStackView.rewind();
        });*/

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

    private void loadDataFromDatabase() {
        BookRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Book> allBooks = bookDao.getAllBooks();

            for (Book book : allBooks) {
                if (!book.isSaved()) {
                    bookList.add(book);
                }
            }

            requireActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    cardStackAdapter.setBooks(bookList);
                }
            });
        });
    }
}