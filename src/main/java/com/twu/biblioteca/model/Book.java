package com.twu.biblioteca.model;

import java.util.ArrayList;

public class Book extends Rental {

    private String author;
    private String publishDate;

    public Book(String title) {
        this(title, "", "");
    }

    public Book(String title, String author, String publishDate) {
        this.title = title;
        this.author = author;
        this.publishDate = publishDate;
        this.available = true;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublishDate() {
        return publishDate;
    }
}
