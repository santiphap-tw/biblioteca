package com.twu.biblioteca;

public class Book {

    private String title;
    private String author;
    private String publishDate;
    private boolean available;

    public Book(String title) {
        this(title, "", "");
    }

    public Book(String title, String author, String publishDate) {
        this.title = title;
        this.author = author;
        this.publishDate = publishDate;
        this.available = true;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public boolean isAvailable() {
        return available;
    }

    public void checkOut() {
        this.available = false;
    }

    public void checkIn() {
        this.available = true;
    }
}