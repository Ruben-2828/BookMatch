package com.example.bookmatch.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.bookmatch.R;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.ui.main.explore.CardSwipeCallback;
import com.example.bookmatch.ui.main.explore.CardViewHolder;

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

            String authors = "";
            for(String a: dataItem.getAuthors())
                authors += a + ", ";
            authors = authors.substring(0, authors.length() - 2);
            holder.author.setText(authors);
            holder.title.setText(dataItem.getTitle());
            Glide.with(holder.itemView).load(dataItem.getCoverURI()).into(holder.cover);
        }
    }

    public View getCurrentItemView(ViewGroup parent) {
        if (currentPosition < dataList.size()) {
            CardViewHolder holder = onCreateViewHolder(parent);
            onBindViewHolder(holder, currentPosition);
            return holder.itemView;
        }
        return null;
    }

    public Book getCurrentItemData() {
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
                    currentHolder.swipeCardLeft();
                    break;
                case 1:
                    currentHolder.swipeCardRight();
                    break;
            }
        }
    }
}
