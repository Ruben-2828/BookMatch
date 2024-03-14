package com.example.bookmatch.ui.main.explore;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.bookmatch.R;
import com.example.bookmatch.adapter.CardAdapter;
import com.example.bookmatch.databinding.FragmentExploreBinding;
import com.example.bookmatch.model.Book;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment implements CardSwipeCallback {

    private FragmentExploreBinding binding;
    private CardAdapter adapter;

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

        List<Book> sampleData = new ArrayList<>();
        for (int i = 0; i < 100; i++){
            sampleData.add(new Book(
                    "i",
                    "Amogus" + i,
                    "Sussy",
                    "Among us game amog amogus",
                    "Fantasy",
                    "2019",
                    "https://heymondo.it/blog/wp-content/uploads/2023/07/Maldive-2.jpg"));
        }
        adapter = new CardAdapter(sampleData, this);
        addCardToFrameLayout();

        binding.dislikeButton.setOnClickListener(v -> selectionMade(0, true));
        binding.likeButton.setOnClickListener(v -> selectionMade(1, true));

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
            args.putString("bookId", currentBook.getId());
            args.putString("title", currentBook.getTitle());
            args.putString("author", currentBook.getAuthor());
            args.putString("plot", currentBook.getPlot());
            args.putString("genre", currentBook.getGenre());
            args.putString("year", currentBook.getPublicationYear());
            args.putString("cover", currentBook.getCover());

            NavController navController = Navigation.findNavController(getView());
            navController.navigate(R.id.action_navigation_explore_to_navigation_book, args);
        } else {
            Toast.makeText(getContext(), "No book selected", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "Discard!", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                adapter.swipeCurrentCard(1);
                Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT).show();
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
}