package com.sp.consolidatedlibraryapplication2;

import android.util.Log;

public class BookModel {
    private String book_name;
    private String book_genre;
    private String book_description;
    private String author;

    // Constructor
    public BookModel(String book_name, String book_genre, String book_description, String author) {
        this.book_name = book_name;
        this.book_genre = book_genre;
        this.book_description = book_description;
        this.author = author;


        Log.d("BookModel", "Book created: " + book_name + " | Genre: " + book_genre + " | Author: " + author);
    }

    // Getters
    public String getBook_name() {
        Log.d("BookModel", "Getting Book Name: " + book_name);
        return book_name;
    }

    public String getBook_genre() {
        Log.d("BookModel", "Getting Book Genre: " + book_genre);
        return book_genre;
    }

    public String getBook_description() {
        Log.d("BookModel", "Getting Book Description: " + book_description);
        return book_description;
    }

    public String getAuthor() {
        Log.d("BookModel", "Getting Author: " + author);
        return author;
    }
}