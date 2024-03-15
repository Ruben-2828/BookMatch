package com.example.bookmatch.adapter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmatch.R;
import com.example.bookmatch.model.Book;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class SavedRecyclerViewAdapter extends RecyclerView.Adapter<SavedRecyclerViewAdapter.SavedViewHolder> {

    private final List<Book> savedList;
    private final SavedRecyclerViewAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {

        void onItemClick(Book book);
    }

    public SavedRecyclerViewAdapter(List<Book> savedList, OnItemClickListener onItemClickListener) {
        this.savedList = savedList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SavedRecyclerViewAdapter.SavedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.saved_book_list_item, parent, false);

        return new SavedRecyclerViewAdapter.SavedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedRecyclerViewAdapter.SavedViewHolder holder, int position) {
        holder.bind(savedList.get(position));
    }

    @Override
    public int getItemCount() {
        if (savedList != null)
            return savedList.size();
        return 0;
    }

    public class SavedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView title;
        private final TextView author;

        public SavedViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.book_title);
            author = itemView.findViewById(R.id.book_author);
            ImageButton addImageButton = itemView.findViewById(R.id.imageview_edit);
            itemView.setOnClickListener(this);
            addImageButton.setOnClickListener(this);
        }

        public void bind(Book p) {
            title.setText(p.getTitle());
            author.setText(p.getAuthor());
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.imageview_edit) {
                PopupMenu popupMenu = new PopupMenu(itemView.getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.saved_option_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    int id = item.getItemId();
                    if (id == R.id.remove_book) {
                        Snackbar.make(view, "Book Removed", Snackbar.LENGTH_SHORT).show();
                        return true;
                    } else if (id == R.id.collection_option_1) {
                        Snackbar.make(view, "Added to Collection 1", Snackbar.LENGTH_SHORT).show();
                        return true;
                    } else if (id == R.id.collection_option_2) {
                        Snackbar.make(view, "Added to Collection 2", Snackbar.LENGTH_SHORT).show();
                        return true;
                    } else if (id == R.id.collection_option_3) {
                        Snackbar.make(view, "Added to Collection 3", Snackbar.LENGTH_SHORT).show();
                        return true;
                    } else {
                        return false;
                    }
                });
                popupMenu.show();
            } else {
                onItemClickListener.onItemClick(savedList.get(getAdapterPosition()));
            }
        }

    }

}
