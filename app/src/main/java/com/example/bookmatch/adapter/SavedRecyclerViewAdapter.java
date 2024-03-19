package com.example.bookmatch.adapter;

import android.app.Application;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookmatch.R;
import com.example.bookmatch.data.repository.books.BookRepository;
import com.example.bookmatch.model.Book;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class SavedRecyclerViewAdapter extends RecyclerView.Adapter<SavedRecyclerViewAdapter.SavedViewHolder> {

    private final List<Book> savedList;



    public interface OnItemClickListener {

        void onItemClick(Book book);
    }

    public SavedRecyclerViewAdapter(List<Book> savedList, OnItemClickListener onItemClickListener) {
        this.savedList = savedList;
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
            author.setText(p.getAuthors().toString());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Book book = savedList.get(position);
                if (view.getId() == R.id.imageview_edit) {
                    removeItem(position);
                } else {
                    Snackbar.make(view, book.getTitle(), Snackbar.LENGTH_SHORT).show();
                }
            }
        }

        private void removeItem(final int position) {
            final Book removedBook = savedList.remove(position);
            notifyItemRemoved(position);
            Snackbar snackbar = Snackbar.make(itemView, removedBook.getTitle() + " removed from saved!", Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.undo, v -> {
                savedList.add(position, removedBook);
                notifyItemInserted(position);
            });
            snackbar.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                        Application application = (Application) itemView.getContext().getApplicationContext();
                        BookRepository bookRepository = new BookRepository(application);
                        bookRepository.removeBookFromSaved(removedBook);
                    }
                }
            });
            snackbar.show();
        }


    }

}
