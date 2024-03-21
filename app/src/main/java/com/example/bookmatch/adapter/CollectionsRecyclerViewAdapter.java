package com.example.bookmatch.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmatch.R;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.model.Collection;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class CollectionsRecyclerViewAdapter extends RecyclerView.Adapter<CollectionsRecyclerViewAdapter.ViewHolder> {

    private final List<Collection> collectionItems;
    private final List<Book> selectedBooks;
    private final OnCollectionClickListener collectionClickListener;
    private final OnDeleteCollectionClickListener deleteCollectionClickListener;

    public interface OnCollectionClickListener {
        void onCollectionClick(Collection collection);
    }

    public interface OnDeleteCollectionClickListener {
        void onDeleteCollectionClick(Collection collection);
    }

    public CollectionsRecyclerViewAdapter(List<Collection> collectionItems, OnCollectionClickListener collectionClickListener, List<Book> selectedBooks, OnDeleteCollectionClickListener deleteCollectionClickListener) {
        this.collectionItems = collectionItems;
        this.collectionClickListener = collectionClickListener;
        this.selectedBooks = selectedBooks;
        this.deleteCollectionClickListener = deleteCollectionClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Collection item = collectionItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return collectionItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewDescription;
        public ImageButton deleteButton;
        RecyclerView booksCarousel;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.collection_item_name);
            textViewDescription = itemView.findViewById(R.id.collection_item_description);
            deleteButton = itemView.findViewById(R.id.delete_collection_button);
            booksCarousel = itemView.findViewById(R.id.books_carousel);


            deleteButton.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && deleteCollectionClickListener != null) {
                    deleteCollectionClickListener.onDeleteCollectionClick(collectionItems.get(position));
                    Snackbar.make(v, "Collection deleted!", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            });

            itemView.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && collectionClickListener != null) {
                    collectionClickListener.onCollectionClick(collectionItems.get(position));
                }
            });
        }

        public void bind(Collection collection) {
            textViewName.setText(collection.getName());
            textViewDescription.setText(collection.getDescription());

            BooksCarouselAdapter booksAdapter = new BooksCarouselAdapter(itemView.getContext(), selectedBooks);
            booksCarousel.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            booksCarousel.setAdapter(booksAdapter);
        }

    }
}

