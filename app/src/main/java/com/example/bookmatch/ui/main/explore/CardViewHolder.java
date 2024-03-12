package com.example.bookmatch.ui.main.explore;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookmatch.R;

public class CardViewHolder implements View.OnTouchListener {
    private float dX, dY;
    private final CardSwipeCallback swipeCallback;
    private float centerX;
    private float centerY;
    private long startTimeClick;
    private float cumulativeDistance;
    private float swipeThreshold;

    public View itemView;
    public TextView title;
    public TextView author;
    public TextView plot;
    public ImageView cover;


    public CardViewHolder(View view, CardSwipeCallback passedSwipeCallback) {

        this.itemView = view;
        this.title = view.findViewById(R.id.book_title);
        this.author = view.findViewById(R.id.book_author);
        this.plot = view.findViewById(R.id.book_plot);
        this.cover = view.findViewById(R.id.book_cover);

        this.swipeCallback = passedSwipeCallback;
        view.setOnTouchListener(this);

        // global layout listener to center the card once the layout is completed
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Ensure I only call this once by removing the listener
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                ViewGroup parent = (ViewGroup) view.getParent();
                int parentWidth = parent.getWidth();
                int parentHeight = parent.getHeight();

                // Calculate center position
                centerX = (parentWidth - view.getWidth()) / 2f;
                centerY = (parentHeight - view.getHeight()) / 2f;

                // Initially position the card at the center
                view.setX(centerX);
                view.setY(centerY);

                swipeThreshold = view.getWidth() * 0.5f; // threshold = 50% of the view's width
            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                //to detect click later
                cumulativeDistance = 0;
                startTimeClick = System.currentTimeMillis();

                dX = view.getX() - event.getRawX();
                dY = view.getY() - event.getRawY();

                return true;

            case MotionEvent.ACTION_MOVE:
                //to detect click later
                cumulativeDistance += Math.floor(Math.abs(view.getX() - centerX))
                        + Math.floor(Math.abs(view.getY() - centerY));


                // Calculate new position
                float newX = event.getRawX() + dX;
                float newY = event.getRawY() + dY;
                view.setX(newX);
                view.setY(newY);

                // Calculate rotation
                float rotationSensitivity = 0.03f;
                view.setRotation((newX - centerX) * rotationSensitivity);

                //calculate opacity
                float opacitySensitivity = 0.002f;
                view.setAlpha(1 - Math.min(0.8f, Math.abs(newX - centerX) * opacitySensitivity));

                // Calculate the percentage of swipe progress
                int direction = (newX - centerX < 0) ? 0 : 1; // 0 = left - 1 = right
                float swipeProgress = Math.min(Math.abs(newX - centerX) / swipeThreshold, 1);
                float scale = 1 - swipeProgress * 0.2f; // Scale down to 80% at full swipe
                float alpha = 1 - swipeProgress * 0.8f; // Reduce opacity downto 80% at full swipe
                swipeCallback.onCardSwiping(direction, scale, alpha, swipeProgress);

                return true;

            case MotionEvent.ACTION_UP:
                //reset swipe progress
                swipeCallback.onCardStopSwiping();

                // If it is a click + not too much time has passed
                long elapsedTime = System.currentTimeMillis() - startTimeClick;
                if (elapsedTime <= 200 && cumulativeDistance == 0) {
                    swipeCallback.onCardClicked();

                    return true;
                }

                //If it is a drag
                float finalXPosition = view.getX() + view.getWidth() / 2f; // Center X position of the card
                float originalXPosition = centerX + view.getWidth() / 2f; // Center X position of the card in original position
                float displacementX = finalXPosition - originalXPosition; // Displacement from the center

                if (displacementX < -swipeThreshold) { // Swiped left
                    swipeCallback.onCardSwipedLeft();
                } else if (displacementX > swipeThreshold) { // Swiped right
                    swipeCallback.onCardSwipedRight();
                }

                // Return the card to the original position + remove additional stiling
                view.animate().x(centerX).y(centerY).rotation(0).alpha(1).setDuration(300).start();

                return true;

            default:
                return false;
        }
    }

    public void swipeCardUp() {
        itemView.animate().y(centerY - itemView.getHeight() / 1.5f).alpha(.2f).setDuration(275).start();
    }

    public void swipeCardLeft() {
        itemView.animate().x(centerX - itemView.getWidth() / 1.5f).rotation(-25).alpha(.2f).setDuration(275).start();
    }

    public void swipeCardRight() {
        itemView.animate().x(centerX + itemView.getWidth() / 1.5f).rotation(25).alpha(.2f).setDuration(275).start();
    }

    public void swipeCardDown() {
        itemView.animate().y(centerY + itemView.getHeight() / 1.5f).alpha(.2f).setDuration(275).start();
    }
}