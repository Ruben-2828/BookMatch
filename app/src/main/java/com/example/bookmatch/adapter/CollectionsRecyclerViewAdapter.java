package com.example.bookmatch.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmatch.R;
import com.example.bookmatch.model.Collection;

import java.util.List;

public class CollectionsRecyclerViewAdapter extends RecyclerView.Adapter<CollectionsRecyclerViewAdapter.ViewHolder> {

    private final List<Collection> collectionItems;
    private OnCollectionClickListener listener;

    // Interfaccia per il click listener
    public interface OnCollectionClickListener {
        void onCollectionClick(Collection collection);
    }

    // Modifica il costruttore per includere il listener
    public CollectionsRecyclerViewAdapter(List<Collection> collectionItems, OnCollectionClickListener listener) {
        this.collectionItems = collectionItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_item, parent, false);
        return new ViewHolder(view, listener); // Passa il listener al ViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Collection item = collectionItems.get(position);
        holder.textViewName.setText(item.getName());
        holder.textViewDescription.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return collectionItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public TextView textViewDescription;

        public ViewHolder(View itemView, final OnCollectionClickListener listener) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.collection_item_name);
            textViewDescription = itemView.findViewById(R.id.collection_item_description);

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
