package com.mypackage.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Author extends Person {

    private String details;
    private final List<Book> booksWrote;

    Author(String name) {
        super(name);
        booksWrote = new ArrayList<Book>();
    }

    @Override
    public String toString() {
        return "Author" + super.toString();
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<Book> getBooksWrote() {
        return Collections.unmodifiableList(booksWrote);
    }

    public void addBook(Book book) {
        booksWrote.add(book);
    }

    public void removeBook(Book book) {
        booksWrote.remove(book);
    }

}
