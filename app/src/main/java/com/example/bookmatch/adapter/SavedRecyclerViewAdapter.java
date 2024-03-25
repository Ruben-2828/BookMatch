package com.example.bookmatch.adapter;

import android.os.Bundle;
import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmatch.R;
import com.example.bookmatch.model.Book;
import com.example.bookmatch.ui.main.BookViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class SavedRecyclerViewAdapter extends RecyclerView.Adapter<SavedRecyclerViewAdapter.SavedViewHolder> {

    private List<Book> books;

    public void setBooks(List<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {

        void onItemClick(Book book);
    }

    public SavedRecyclerViewAdapter(List<Book> books, OnItemClickListener onItemClickListener) {
        this.books = books;
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
        holder.itemView.setOnClickListener(v -> {
            Book currentBook = books.get(position);
            Bundle args = new Bundle();
            args.putParcelable("book", currentBook);

            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_navigation_saved_to_navigation_book, args);
        });
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

        public SavedViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.book_title);
            author = itemView.findViewById(R.id.book_author);
            ImageButton addImageButton = itemView.findViewById(R.id.imageview_edit);
            itemView.setOnClickListener(this);
            addImageButton.setOnClickListener(this);
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
                if (view.getId() == R.id.imageview_edit) {
                    removeItem(position);
                } else {
                    Snackbar.make(view, book.getTitle(), Snackbar.LENGTH_SHORT).show();
                }
            }
        }

        private void removeItem(final int position) {
            final Book removedBook = books.remove(position);
            notifyItemRemoved(position);
            Snackbar snackbar = Snackbar.make(itemView, removedBook.getTitle() + " removed from saved!", Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.undo, v -> {
                books.add(position, removedBook);
                notifyItemInserted(position);
            });
            snackbar.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                        //TODO: SISTEMARE, creare callback, mettere onClickListener dal savedFragment
                        Application application = (Application) itemView.getContext().getApplicationContext();
                        BookViewModel bookViewModel = new BookViewModel(application);
                        bookViewModel.deleteBook(removedBook);
                    }
                }
            });
            snackbar.show();
        }

    }

}
