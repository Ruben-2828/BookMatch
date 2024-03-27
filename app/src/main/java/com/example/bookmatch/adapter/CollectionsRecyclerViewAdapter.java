package com.example.bookmatch.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmatch.R;
import com.example.bookmatch.model.Collection;

import java.util.List;

public class CollectionsRecyclerViewAdapter extends RecyclerView.Adapter<CollectionsRecyclerViewAdapter.CollectionViewHolder> {

    private List<Collection> collections;
    private final OnCollectionClickListener onCollectionClickListener;

    public void setCollections(List<Collection> collections) {
        this.collections = collections;
        notifyDataSetChanged();
    }

    public interface OnCollectionClickListener {
        void onItemClick(Collection collection);
        void onDeleteButtonClick(Collection collection);
    }

    public CollectionsRecyclerViewAdapter(List<Collection> collections,
                                          OnCollectionClickListener onCollectionClickListener) {
        this.collections = collections;
        this.onCollectionClickListener = onCollectionClickListener;
    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_item, parent, false);
        return new CollectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, int position) {
        Collection item = collections.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return collections.size();
    }

    public class CollectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView textViewName;
        private final TextView textViewDescription;
        private final RecyclerView booksCarousel;
        private final ImageButton deleteButton;


        public CollectionViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.collection_item_name);
            textViewDescription = itemView.findViewById(R.id.collection_item_description);
            booksCarousel = itemView.findViewById(R.id.books_carousel);
            deleteButton = itemView.findViewById(R.id.delete_collection_button);
            itemView.setOnClickListener(this);
        }

        public void bind(Collection collection) {
            textViewName.setText(collection.getName());
            textViewDescription.setText(collection.getDescription());
            deleteButton.setOnClickListener(this);

            //BooksCarouselAdapter booksAdapter = new BooksCarouselAdapter(itemView.getContext(), selectedBooks);
            //booksCarousel.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            //booksCarousel.setAdapter(booksAdapter);
        }

        @Override
        public void onClick(View v) {

            if(v.getId() == R.id.delete_collection_button)
                onCollectionClickListener.onDeleteButtonClick(collections.get(getAbsoluteAdapterPosition()));
            else
                onCollectionClickListener.onItemClick(collections.get(getAbsoluteAdapterPosition()));
        }
    }
}

