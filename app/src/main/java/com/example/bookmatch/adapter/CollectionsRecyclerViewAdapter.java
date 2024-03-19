package com.example.bookmatch.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmatch.R;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.model.Collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CollectionsRecyclerViewAdapter extends RecyclerView.Adapter<CollectionsRecyclerViewAdapter.ViewHolder> {

    private final List<Collection> collectionItems;
    private OnCollectionClickListener listener;

    public interface OnCollectionClickListener {
        void onCollectionClick(Collection collection);
    }

    public CollectionsRecyclerViewAdapter(List<Collection> collectionItems, OnCollectionClickListener listener) {
        this.collectionItems = collectionItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_item, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Collection item = collectionItems.get(position);
        holder.textViewName.setText(item.getName());
        holder.textViewDescription.setText(item.getDescription());

        //TODO: Replace with actual books
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            books.add(new Book(
                    i + "",
                    "Occhi nel Codice: Il Genio di Jouness Amsaet. Parte  " + i,
                    new ArrayList<String>(Arrays.asList("Paco Quackez", "acacaca")),
                    new ArrayList<String>(Arrays.asList("Avventura ezezez")),
                    "2024",
                    new ArrayList<String>(Arrays.asList("horror", "fiction")),
                    "https://heymondo.it/blog/wp-content/uploads/2023/07/Maldive-2.jpg",
                    false
            ));
        }

        BooksCarouselAdapter booksAdapter = new BooksCarouselAdapter(holder.itemView.getContext(), books);
        holder.booksCarousel.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        holder.booksCarousel.setAdapter(booksAdapter);
    }


    @Override
    public int getItemCount() {
        return collectionItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewDescription;
        RecyclerView booksCarousel;

        public ViewHolder(View itemView, final OnCollectionClickListener listener) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.collection_item_name);
            textViewDescription = itemView.findViewById(R.id.collection_item_description);
            booksCarousel = itemView.findViewById(R.id.books_carousel);

            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onCollectionClick((Collection) itemView.getTag());
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        holder.itemView.setTag(collectionItems.get(position));
    }
}
