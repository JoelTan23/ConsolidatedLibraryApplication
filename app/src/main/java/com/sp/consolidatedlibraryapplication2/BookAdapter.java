package com.sp.consolidatedlibraryapplication2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<BookModel> bookList;

    // Constructor
    public BookAdapter(List<BookModel> bookList) {
        this.bookList = bookList;
        Log.d("BookAdapter", "Adapter initialized with " + (bookList != null ? bookList.size() : 0) + " books");
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("BookAdapter", "Creating new ViewHolder");
        // Inflate item layout (CardView)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookitem, parent, false);
        return new BookViewHolder(view);
    }

    // Connecting Data to the recycler view
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        // Get the book at the current position
        if (bookList != null && bookList.size() > 0) {
            BookModel book = bookList.get(position);

            Log.d("BookAdapter", "Binding book at position " + position + ": " + book.getBook_name());

            // Set the book data to the views in the CardView
            holder.bookName.setText(book.getBook_name());
            holder.bookGenre.setText(book.getBook_genre());
            holder.bookDescription.setText(book.getBook_description());
            holder.author.setText(book.getAuthor());
        } else {
            Log.e("BookAdapter", "Book list is empty or null.");
        }
    }

    @Override
    public int getItemCount() {
        int itemCount = bookList != null ? bookList.size() : 0;
        return itemCount;
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView bookName, bookGenre, bookDescription, author;

        public BookViewHolder(View itemView) {
            super(itemView);
            // Connects all the variables to the associated textviews in the cards
            bookName = itemView.findViewById(R.id.book_name);
            bookGenre = itemView.findViewById(R.id.book_genre);
            bookDescription = itemView.findViewById(R.id.book_description);
            author = itemView.findViewById(R.id.author);

            // Log to confirm that the ViewHolder has been created successfully
            Log.d("BookAdapter", "ViewHolder created for book item.");
        }
    }
}
