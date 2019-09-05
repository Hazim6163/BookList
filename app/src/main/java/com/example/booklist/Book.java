package com.example.booklist;

public class Book {
    private String mBookTitle;
    private String mAuthor;
    private String mPublishDate;
    private String mUrl;

    public Book(String title, String author, String date, String url){
        mBookTitle = title;
        mAuthor = author;
        mPublishDate = date;
        mUrl = url;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmBookTitle() {
        return mBookTitle;
    }

    public String getmPublishDate() {
        return mPublishDate;
    }

    public String getmUrl() {
        return mUrl;
    }
}
