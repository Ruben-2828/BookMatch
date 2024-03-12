package com.example.bookmatch.ui.main.explore;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.bookmatch.R;
import com.example.bookmatch.model.Book;
import java.util.HashMap;
import java.util.List;

public class CardAdapter {

    private final List<Book> dataList;
    private final CardSwipeCallback swipeCallback;
    private final HashMap<Integer, CardViewHolder> holderMap = new HashMap<>();
    private int currentPosition = 0;

    public CardAdapter(List<Book> dataList, CardSwipeCallback swipeCallback) {
        this.dataList = dataList;
        this.swipeCallback = swipeCallback;
    }

    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cardview_swipeable, viewGroup, false);
        CardViewHolder holder = new CardViewHolder(view, swipeCallback);
        holderMap.put(currentPosition, holder);
        return holder;
    }

    public void onBindViewHolder(CardViewHolder holder, int position) {

        if (!dataList.isEmpty() && position < dataList.size()) {
            Book dataItem = dataList.get(position);

            holder.plot.setText(dataItem.getPlot());
            holder.author.setText(dataItem.getAuthor());
            holder.title.setText(dataItem.getTitle());
            Glide.with(holder.itemView).load(dataItem.getCover()).into(holder.cover);
        }
    }

    public int getItemCount() {
        return dataList.size();
    }

    public View getCurrentItemView(ViewGroup parent) {
        if (currentPosition < dataList.size()) {
            CardViewHolder holder = onCreateViewHolder(parent);
            onBindViewHolder(holder, currentPosition);
            return holder.itemView;
        }
        return null;
    }

    public Book getCurrentItemData(ViewGroup parent) {
        if (currentPosition < dataList.size()) {
            return dataList.get(currentPosition);
        }
        return null;
    }

    public void advanceToNextItem() {
        if (!dataList.isEmpty() && currentPosition < dataList.size()) {
            currentPosition = (currentPosition + 1) % dataList.size();
            holderMap.remove(currentPosition - 1);
        }
    }

    public void swipeCurrentCard(int direction) {
        CardViewHolder currentHolder = holderMap.get(currentPosition);

        if (currentHolder != null) {
            switch (direction){
                case 0:
                    currentHolder.swipeCardUp();
                    break;
                case 1:
                    currentHolder.swipeCardLeft();
                    break;
                case 2:
                    currentHolder.swipeCardRight();
                    break;
                case 3:
                    currentHolder.swipeCardDown();
            }
        }
    }
}
