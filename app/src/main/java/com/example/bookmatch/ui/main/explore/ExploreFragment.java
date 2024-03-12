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

import com.example.bookmatch.databinding.FragmentExploreBinding;
import com.example.bookmatch.model.Book;

import java.util.ArrayList;
import java.util.List;

public class ExploreFragment extends Fragment implements CardSwipeCallback{

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

        binding.dislikeButton.setOnClickListener(v -> selectionMade(1, true));
        binding.likeButton.setOnClickListener(v -> selectionMade(2, true));

    }

    private void addCardToFrameLayout() {
        View cardView = adapter.getCurrentItemView(binding.cardStackView);
        if (cardView != null) {
            binding.cardStackView.addView(cardView);
        }
    }

    @Override
    public void onCardSwipedLeft() {
        selectionMade(1, false);
    }

    @Override
    public void onCardSwipedRight() {
        selectionMade(2, false);
    }

    @Override
    public void onCardClicked() {
        //Book currentBook = adapter.getCurrentItemData(binding.cardStackView);

        //Bundle args = new Bundle();
        //args.putString("project id", currentBook.getId());

        //NavController navC = Navigation.findNavController(getView());
        //navC.navigate(R.id.action_navigation_explore_projects_to_projectPageFragment, args);
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
            case 1:
                adapter.swipeCurrentCard(1);
                Toast.makeText(getContext(), "Disliked!", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                adapter.swipeCurrentCard(2);
                Toast.makeText(getContext(), "Liked!", Toast.LENGTH_SHORT).show();
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
        binding.cardStackView.removeAllViews();
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