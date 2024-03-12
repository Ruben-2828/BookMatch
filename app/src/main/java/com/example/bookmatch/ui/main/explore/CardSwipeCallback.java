package com.example.bookmatch.ui.main.explore;
public interface CardSwipeCallback {
    void onCardSwipedLeft();
    void onCardSwipedRight();
    void onCardClicked();
    void onCardSwiping(int direction, float scale, float alpha, float borderProgress);
    void onCardStopSwiping();
}
