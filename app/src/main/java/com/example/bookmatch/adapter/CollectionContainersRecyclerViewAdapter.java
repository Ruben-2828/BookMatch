package com.example.bookmatch.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmatch.R;
import com.example.bookmatch.model.CollectionContainer;
import com.example.bookmatch.utils.Converters;

import java.util.List;

public class CollectionContainersRecyclerViewAdapter extends RecyclerView.Adapter<CollectionContainersRecyclerViewAdapter.CollectionViewHolder> {

    private List<CollectionContainer> collections;
    private final OnCollectionClickListener onCollectionClickListener;

    public void setCollections(List<CollectionContainer> collections) {
        this.collections = collections;
        notifyDataSetChanged();
    }

    public interface OnCollectionClickListener {
        void onItemClick(CollectionContainer collection);
        void onDeleteButtonClick(CollectionContainer collection);
    }

    public CollectionContainersRecyclerViewAdapter(List<CollectionContainer> collections,
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
        CollectionContainer item = collections.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return collections.size();
    }

    public class CollectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ImageView imageView;
        private final TextView textViewName;
        private final TextView textViewDescription;
        private final ImageButton deleteButton;


        public CollectionViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.collection_item_image);
            textViewName = itemView.findViewById(R.id.collection_item_name);
            textViewDescription = itemView.findViewById(R.id.collection_item_description);
            deleteButton = itemView.findViewById(R.id.delete_collection_button);
            itemView.setOnClickListener(this);
        }

        public void bind(CollectionContainer collection) {
            byte[] imageData = collection.getImageData();
            if (imageData != null) {
                Bitmap bitmap = Converters.toBitmap(imageData);
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.drawable.library); // Default image if no image stored
            }
            textViewName.setText(collection.getName());
            textViewDescription.setText(collection.getDescription());
            deleteButton.setOnClickListener(this);
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

