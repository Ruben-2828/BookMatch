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

import java.util.List;

public class ReviewsRecyclerViewAdapter extends RecyclerView.Adapter<ReviewsRecyclerViewAdapter.ReviewsViewHolder> {

    private List<Book> books;

    public ReviewsRecyclerViewAdapter(List<Book> books) {
        this.books = books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    //TODO: ONCLICKLISTENER PER I BOTTONI

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
        private final ImageButton editButton;

        private final ImageButton deleteButton;


        public ReviewsViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.book_title);
            author = itemView.findViewById(R.id.book_author);
            review = itemView.findViewById(R.id.book_review);
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
        }

        @Override
        public void onClick(View view) {
            int position = getAbsoluteAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Book book = books.get(position);
                if (view.getId() == R.id.imageview_delete) {
                    removeItem(position);
                } else if (view.getId() == R.id.imageview_edit){
                    addReview(position);
                } else {
                    Snackbar.make(view, book.getTitle(), Snackbar.LENGTH_SHORT).show();
                }
            }
        }

        private void removeItem(final int position) {
            final Book removedBook = books.remove(position);
            notifyItemRemoved(position);
            Snackbar snackbar = Snackbar.make(itemView, removedBook.getTitle() + " removed from reviews!", Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.undo, v -> {
                books.add(position, removedBook);
                notifyItemInserted(position);
            });
            snackbar.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                        removedBook.setReviewed(false);
                        Application application = (Application) itemView.getContext().getApplicationContext();
                        BookViewModel bookViewModel = new BookViewModel(application);
                        bookViewModel.updateBook(removedBook);
                    }
                }
            });
            snackbar.show();
        }

        private void addReview(final int position) {
            final Book book = books.get(position);
            Intent intent = new Intent(itemView.getContext(), AddReviewActivity.class);
            intent.putExtra("book", book);
            itemView.getContext().startActivity(intent);

        }
    }
}