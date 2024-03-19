package com.example.bookmatch.ui.main.explore;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bookmatch.R;
import com.example.bookmatch.adapter.CardAdapter;
import com.example.bookmatch.data.database.BookDao;
import com.example.bookmatch.data.database.BookRoomDatabase;
import com.example.bookmatch.data.repository.books.BookRepository;
import com.example.bookmatch.databinding.FragmentExploreBinding;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.ui.main.saved.SharedViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExploreFragment extends Fragment implements CardSwipeCallback {

    private static final String TAG = ExploreFragment.class.getSimpleName();

    private FragmentExploreBinding binding;
    private SharedViewModel sharedViewModel;

    private CardAdapter adapter;
    private BookDao bookDao;
    private final List<Book> bookList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentExploreBinding.inflate(inflater, container, false);
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

        BookRepository bookRepository = new BookRepository(requireActivity().getApplication());

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);


        adapter = new CardAdapter(bookList, this);
        addCardToFrameLayout();

        binding.dislikeButton.setOnClickListener(v -> selectionMade(0, true));
        binding.likeButton.setOnClickListener(v -> {
            selectionMade(1, true);
            Book currentBook = adapter.getCurrentItemData();
            if (currentBook != null) {
                sharedViewModel.saveBook(currentBook);
            }
        });

        binding.genre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bookRepository.fetchBooks(s.toString());
                String formattedGenre = "%" + s.toString() + "%";
                Log.d(TAG, formattedGenre);
                loadDataFromDatabase(formattedGenre);
                addCardToFrameLayout();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void addCardToFrameLayout() {
        View cardView = adapter.getCurrentItemView(binding.cardStackView);
        if (cardView != null) {
            binding.cardStackView.addView(cardView);
        }
    }

    @Override
    public void onCardSwipedLeft() {
        selectionMade(0, false);
    }

    @Override
    public void onCardSwipedRight() {
        selectionMade(1, false);
    }

    @Override
    public void onCardClicked() {
        Book currentBook = adapter.getCurrentItemData();
        if (currentBook != null) {
            Bundle args = new Bundle();
            args.putParcelable("book", currentBook);

            NavController navController = Navigation.findNavController(getView());
            navController.navigate(R.id.action_navigation_explore_to_navigation_book, args);
        } else {
            Toast.makeText(getContext(), (R.string.no_book_selected), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCardSwiping(int direction, float scale, float alpha, float borderProgress){
        disableButtons();
        if(direction == 0) {
            binding.dislikeButton.setScaleX(scale);
            binding.dislikeButton.setScaleY(scale);
            binding.dislikeButton.setAlpha(alpha);
            binding.likeFabBorderView.updateBorderProgress(0);
            binding.dislikeFabBorderView.updateBorderProgress(borderProgress);
        } else {
            binding.likeButton.setScaleX(scale);
            binding.likeButton.setScaleY(scale);
            binding.likeButton.setAlpha(alpha);
            binding.dislikeFabBorderView.updateBorderProgress(0);
            binding.likeFabBorderView.updateBorderProgress(borderProgress);
        }
    }

    @Override
    public void onCardStopSwiping(){
        enableButtons();
        binding.dislikeFabBorderView.updateBorderProgress(0);
        binding.likeFabBorderView.updateBorderProgress(0);
        binding.dislikeButton.setScaleX(1);
        binding.dislikeButton.setScaleY(1);
        binding.dislikeButton.setAlpha(1f);
        binding.likeButton.setScaleX(1);
        binding.likeButton.setScaleY(1);
        binding.likeButton.setAlpha(1f);
    }

    public void selectionMade(int action, boolean animation){
        switch(action) {
            case 0:
                adapter.swipeCurrentCard(0);
                Toast.makeText(getContext(), (R.string.discard_toast), Toast.LENGTH_SHORT).show();
                break;
            case 1:
                adapter.swipeCurrentCard(1);
                Toast.makeText(getContext(), (R.string.saved_toast), Toast.LENGTH_SHORT).show();
                break;
        }

        if(animation){
            disableButtons();
            new Handler().postDelayed(() -> {
                updateCards();
                enableButtons();
            }, 300);
        } else {
            updateCards();
        }
    }

    private void updateCards(){
        // Remove the current card
        binding.cardStackView.removeAllViews();

        // Advance to the next card
        adapter.advanceToNextItem();
        addCardToFrameLayout();
    }

    private void disableButtons() {
        binding.dislikeButton.setEnabled(false);
        binding.likeButton.setEnabled(false);
    }

    private void enableButtons() {
        binding.dislikeButton.setEnabled(true);
        binding.likeButton.setEnabled(true);
    }

    private void loadDataFromDatabase(String genre) {
        BookRoomDatabase.databaseWriteExecutor.execute(() -> {
            bookDao = BookRoomDatabase.getDatabase(requireContext()).bookDao();
            bookList.clear();
            bookList.addAll(bookDao.getBooksByGenre(genre));
        });
    }
}