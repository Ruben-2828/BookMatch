package com.example.bookmatch.adapter;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookmatch.R;
import com.example.bookmatch.model.Book;

import java.util.ArrayList;


public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.CardViewHolder> {

    private ArrayList<Book> books;
    private final CardStackAdapter.OnItemClickListener onItemClickListener;

    public CardStackAdapter(OnItemClickListener onItemClickListener) {
        this.books = new ArrayList<>();
        this.onItemClickListener = onItemClickListener;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public Book getBook(int position) {
        return books.get(position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearBooks() {
        this.books.clear();
        notifyDataSetChanged();
    }

    public void addBooks(ArrayList<Book> books) {
        int pos = this.books.size();
        this.books.addAll(books);
        notifyItemRangeInserted(pos, books.size());
    }

    public void removeBook(int position) {
        this.books.remove(position);
    }

    @NonNull
    @Override
    public CardStackAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cardview_swipeable, parent, false);

        return new CardStackAdapter.CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.bind(books.get(position));
    }


    @Override
    public int getItemCount() {
        if (books != null)
            return books.size();
        return 0;
    }

    public interface OnItemClickListener {
        void onItemClick(Book book);
    }

    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView title;
        private final TextView author;
        private final ImageView cover;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.book_title);
            this.author = itemView.findViewById(R.id.book_author);
            this.cover = itemView.findViewById(R.id.book_cover);
            itemView.setOnClickListener(this);
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


            Glide.with(this.itemView)
                    .load(b.getCoverURI())
                    .into(this.cover);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(books.get(getAbsoluteAdapterPosition()));
        }
    }
}
