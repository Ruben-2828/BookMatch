package com.example.bookmatch.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmatch.R;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.ui.main.BookViewModel;
import com.example.bookmatch.ui.main.BookViewModelFactory;
import com.example.bookmatch.ui.main.CollectionGroupViewModel;
import com.example.bookmatch.ui.main.CollectionGroupViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class CollectionGroupsRecyclerViewAdapter extends
        RecyclerView.Adapter<CollectionGroupsRecyclerViewAdapter.BookViewHolder> {

    private final List<Book> bookList;
    private final OnBookSelectedListener onBookSelectedListener;

    public interface OnBookSelectedListener {
        void onBookSelected(String bookId, String action);
    }

    public CollectionGroupsRecyclerViewAdapter(List<Book> bookList, OnBookSelectedListener listener) {
        this.bookList = bookList;
        this.onBookSelectedListener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.collection_book_list_item, parent, false);

        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        if (book != null) {
            holder.bind(book);
        }
    }

    @Override
    public int getItemCount() {
        if (bookList != null)
            return bookList.size();
        return 0;
    }

    public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView title;
        private final TextView author;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.book_title_collection);
            author = itemView.findViewById(R.id.book_author_collection);
            ImageButton removeBookImageButton = itemView.findViewById(R.id.imageview_delete_collection_book);
            removeBookImageButton.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void bind(Book book) {
            title.setText(book.getTitle());

            String authors = "";
            for(String a: book.getAuthors())
                authors += a + ", ";
            authors = authors.substring(0, authors.length() - 2);
            author.setText(authors);
        }

        @Override
        public void onClick(View view) {
            int position = getAbsoluteAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Book book = bookList.get(position);
                if (view.getId() == R.id.imageview_delete_collection_book) {
                    removeItem(position);
                } else {
                    Snackbar.make(view, book.getTitle(), Snackbar.LENGTH_SHORT).show();
                }
            }
        }

        private void removeItem(final int position) {
            final Book removedBook = bookList.get(position);
            bookList.remove(position);
            notifyItemRemoved(position);

            onBookSelectedListener.onBookSelected(removedBook.getId(), "remove");

            Snackbar snackbar = Snackbar.make(itemView, removedBook.getTitle() + " removed from collection", Snackbar.LENGTH_SHORT);
            snackbar.setAction(R.string.undo, v -> {
                bookList.add(position, removedBook);
                notifyItemInserted(position);
                onBookSelectedListener.onBookSelected(removedBook.getId(), "add");
            });
            snackbar.show();
        }

    }
}
