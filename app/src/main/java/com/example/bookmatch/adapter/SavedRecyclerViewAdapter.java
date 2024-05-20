package com.example.bookmatch.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmatch.R;
import com.example.bookmatch.model.Book;

import java.util.ArrayList;
import java.util.List;

public class SavedRecyclerViewAdapter extends RecyclerView.Adapter<SavedRecyclerViewAdapter.SavedViewHolder> {

    private List<Book> books;
    private final OnItemClickListener onItemClickListener;

    @SuppressLint("NotifyDataSetChanged")
    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Book book);
        void onDeleteButtonClick(int position);
        void onReviewButtonClick(int position);
    }

    public SavedRecyclerViewAdapter(OnItemClickListener onItemClickListener) {
        this.books = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    public Book removeBook(int position) {
        return books.remove(position);
    }

    public void addBook(int position, Book book) {
        books.add(position, book);
    }

    public Book getBook(int position) {
        return books.get(position);
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
        holder.bind(books.get(position));
    }

    @Override
    public int getItemCount() {
        if (books != null)
            return books.size();
        return 0;
    }

    public class SavedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView title;
        private final TextView author;
        private final ImageButton reviewButton;

        public SavedViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.book_title);
            author = itemView.findViewById(R.id.book_author);
            ImageButton deleteButton = itemView.findViewById(R.id.imageview_delete);
            reviewButton = itemView.findViewById(R.id.imageview_review);

            itemView.setOnClickListener(this);
            deleteButton.setOnClickListener(this);
            reviewButton.setOnClickListener(this);
        }

        public void bind(Book b) {
            title.setText(b.getTitle());
            if (b.getAuthors() != null) {
                String authors = "";
                for (String a : b.getAuthors())
                    authors += a + ", ";
                authors = authors.substring(0, authors.length() - 2);
                author.setText(authors);
            }

            if (b.isReviewed())
                reviewButton.setImageResource(R.drawable.baseline_star_rate_24);
            else
                reviewButton.setImageResource(R.drawable.baseline_star_outline_24);
        }

        @Override
        public void onClick(View view) {
            int position = getAbsoluteAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                if (view.getId() == R.id.imageview_delete ) {
                    onItemClickListener.onDeleteButtonClick(position);
                } else if (view.getId() == R.id.imageview_review) {
                    onItemClickListener.onReviewButtonClick(position);
                } else {
                    onItemClickListener.onItemClick(books.get(position));
                }
            }
        }
    }
}
