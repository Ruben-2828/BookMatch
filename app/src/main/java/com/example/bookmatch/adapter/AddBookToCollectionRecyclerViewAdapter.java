package com.example.bookmatch.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmatch.R;
import com.example.bookmatch.model.Book;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class AddBookToCollectionRecyclerViewAdapter extends
        RecyclerView.Adapter<AddBookToCollectionRecyclerViewAdapter.BookViewHolder> {

    private final List<Book> bookList;
    public List<Book> selectedBooks = new ArrayList<>();
    private final OnBookSelectedListener onBookSelectedListener;

    public interface OnBookSelectedListener {
        void onBookSelected(Book book, String action);
    }

    public AddBookToCollectionRecyclerViewAdapter(List<Book> bookList, OnBookSelectedListener listener) {
        this.bookList = bookList;
        this.onBookSelectedListener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.add_book_to_collection_list_item, parent, false);

        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        holder.bind(bookList.get(position));
    }

    @Override
    public int getItemCount() {
        if (bookList != null)
            return bookList.size();
        return 0;
    }

    public List<Book> getSelectedBooks() {
        return selectedBooks;
    }

    public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView title;
        private final TextView author;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.book_title);
            author = itemView.findViewById(R.id.book_author);
            ImageButton addImageButton = itemView.findViewById(R.id.add_book_to_collection);
            //itemView.setOnClickListener(this);
            addImageButton.setOnClickListener(this);
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
                if (view.getId() == R.id.add_book_to_collection) {
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

            onBookSelectedListener.onBookSelected(removedBook, "add");

            Snackbar snackbar = Snackbar.make(itemView, removedBook.getTitle() + " added to collection", Snackbar.LENGTH_SHORT);
            snackbar.setAction(R.string.undo, v -> {
                bookList.add(position, removedBook);
                notifyItemInserted(position);
                selectedBooks.remove(removedBook);

                onBookSelectedListener.onBookSelected(removedBook, "remove");
            });
            snackbar.show();
        }


    }
}
