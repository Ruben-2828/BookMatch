package com.example.bookmatch.adapter;

import android.app.Application;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmatch.R;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.ui.main.BookViewModel;
import com.example.bookmatch.ui.main.reviews.AddReviewActivity;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ReviewsRecyclerViewAdapter extends RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ReviewsViewHolder> {

    private List<Book> books;
    private final OnItemClickListener onItemClickListener;

    public ReviewsRecyclerViewAdapter(OnItemClickListener onItemClickListener) {
        this.books = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
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

    //TODO: ONCLICKLISTENER PER I BOTTONI

    public interface OnItemClickListener {
        void onDeleteButtonClick(int position);
        void onEditButtonClick(int position);
    }

    @NonNull
    @Override
    public ReviewsRecyclerViewAdapter.ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_book_list_item, parent, false);
        return new ReviewsRecyclerViewAdapter.ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        holder.bind(books.get(position));
    }

    @Override
    public int getItemCount() {

        return books.size();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView title;
        private final TextView author;
        private final TextView review;

        private final TextView rating;
        private final ImageButton editButton;

        private final ImageButton deleteButton;


        public ReviewsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.book_title);
            author = itemView.findViewById(R.id.book_author);
            review = itemView.findViewById(R.id.book_review);
            rating = itemView.findViewById(R.id.book_rating);
            deleteButton = itemView.findViewById(R.id.imageview_delete);
            editButton = itemView.findViewById(R.id.imageview_edit);
            deleteButton.setOnClickListener(this);
            editButton.setOnClickListener(this);
        }

        public void bind(Book b) {
            title.setText(b.getTitle());
            String authors;
            if (b.getAuthors() != null) {
                authors = "";
                for (String a : b.getAuthors())
                    authors += a + ", ";
                authors = authors.substring(0, authors.length() - 2);
            } else {
                authors = "No author found";
            }
            author.setText(authors);
            if(b.getReview() != null)
                review.setText(b.getReview());
            rating.setText(String.valueOf(b.getRating()));
        }

        @Override
        public void onClick(View view) {
            int position = getAbsoluteAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Book book = books.get(position);
                if (view.getId() == R.id.imageview_delete) {
                    onItemClickListener.onDeleteButtonClick(position);
                } else if (view.getId() == R.id.imageview_edit){
                    onItemClickListener.onEditButtonClick(position);
                }
            }
        }
    }
}