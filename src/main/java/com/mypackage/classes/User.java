package com.mypackage.classes;

import com.opencsv.bean.CsvBindByName;

import java.time.LocalDateTime;
import java.util.*;

public class User extends Person {
    @CsvBindByName
    private String email;
    @CsvBindByName
    private String phoneNumber;
    private final List<AbstractMap.SimpleEntry<LocalDateTime, Book.Copy>> rentedBooks;
    private final List<Book.Copy> nowRentedBooks;

    public List<AbstractMap.SimpleEntry<LocalDateTime, Book.Copy>> getRentedBooks() {
        return Collections.unmodifiableList(rentedBooks);
    }

    public List<Book.Copy> getNowRentedBooks() {
        return Collections.unmodifiableList(nowRentedBooks);
    }

    public User(String email, String name, String phoneNumber) {
        super(name);
        this.email = email;
        this.phoneNumber = phoneNumber;
        rentedBooks = new ArrayList<>();
        nowRentedBooks = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User" + super.toString();

    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean rent(Book book) {
        var copy = book.getBestAvailableCopy();
        if (copy == null)
            return false;
        copy.getsRented(this);
        nowRentedBooks.add(copy);
        rentedBooks.add(new AbstractMap.SimpleEntry<>(LocalDateTime.now(), copy));
        return true;
    }

    public void returnBook(Book.Copy copy) {
        if (nowRentedBooks.contains(copy)) {
            nowRentedBooks.remove(copy);
            copy.getsReturned();
        }
    }

}
