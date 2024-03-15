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

import java.util.List;

public class AddBookToCollectionRecyclerViewAdapter extends RecyclerView.Adapter<AddBookToCollectionRecyclerViewAdapter.BookViewHolder> {

    private final List<Book> bookList;


    public AddBookToCollectionRecyclerViewAdapter(List<Book> bookList) {
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.add_book_to_collecxtion_list_item, parent, false);

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

    public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView title;
        private final TextView author;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.book_title);
            author = itemView.findViewById(R.id.book_author);
            ImageButton addImageButton = itemView.findViewById(R.id.imageview_edit);
            itemView.setOnClickListener(this);
            addImageButton.setOnClickListener(this);
        }

        public void bind(Book book) {
            title.setText(book.getTitle());
            author.setText(book.getAuthor());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Book book = bookList.get(position);
                if (view.getId() == R.id.imageview_edit) {
                    removeItem(position);
                } else {
                    Snackbar.make(view, book.getTitle(), Snackbar.LENGTH_SHORT).show();
                }
            }
        }

        private void removeItem(final int position) {
            final Book addedBook = bookList.remove(position);
            notifyItemRemoved(position);
            Snackbar snackbar = Snackbar.make(itemView, addedBook.getTitle() + " added to collection", Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.undo, v -> {
                bookList.add(position, addedBook);
                notifyItemInserted(position);
            });
            snackbar.show();
        }
    }
}
