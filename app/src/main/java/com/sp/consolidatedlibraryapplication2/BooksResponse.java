package com.sp.consolidatedlibraryapplication2;

import java.util.List;

public class BooksResponse {
    private int count;
    private List<BookModel> data; // Assuming BookModel is the structure of each book

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<BookModel> getData() {
        return data;
    }

    public void setData(List<BookModel> data) {
        this.data = data;
    }
}