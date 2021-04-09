package com.mypackage.classes;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Book implements Comparable<Book> {
    //region Book
    private String name;
    private Author author;
    final private List<Genre> genres;
    final private List<Copy> copies;
    private Section section;

    @Override
    public String toString() {
        return "{Book " + name + " by " + author.getName() + "}";

    }

    Book(@NotNull String name, @NotNull Author author, Section section) {
        this.name = name;
        this.author = author;
        this.copies = new ArrayList<>();
        this.genres = new ArrayList<>();
        this.section = section;
        author.addBook(this);
    }

    public List<Genre> getGenres() {
        return Collections.unmodifiableList(genres);
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author.removeBook(this);
        this.author = author;
        author.addBook(this);
    }

    public void removeGenre(Genre genre) {
        genres.remove(genre);
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    public boolean hasGenre(Genre genre) {
        return genres.contains(genre);
    }

    @Override
    public int compareTo(Book o) {
        return this.getName().compareTo(o.getName());
    }

    public Copy makeCopy(int pages, Redaction redaction) {
        var copy = new Copy(this, pages, redaction);
        copies.add(copy);
        return copy;
    }

    public void removeCopy(Copy copy) {
        copies.remove(copy);
    }

    public List<Copy> getCopies() {
        return Collections.unmodifiableList(copies);
    }

    public Copy getBestAvailableCopy() {
        return copies.stream()
                .filter(copy -> !copy.isRented()).sorted().findFirst()
                .orElse(null);
    }

    //endregion
    public static class Copy implements Comparable<Copy> {
        final private Book book;
        final private int pages;
        final private Redaction redaction;
        private boolean isRented;
        private LocalDateTime dueDate;
        final private List<User> users;
        private Status status;

        public Status getStatus() {
            return status;
        }

        public Status damage() {
            status = status.next();
            if (status == Status.UNUSABLE)
                book.removeCopy(this);
            return status;
        }


        public LocalDateTime getDueDate() {
            return dueDate;
        }

        public List<User> getUsers() {
            return Collections.unmodifiableList(users);
        }

        public Book getBook() {
            return book;
        }

        public int getPages() {
            return pages;
        }

        public Redaction getRedaction() {
            return redaction;
        }

        public boolean isRented() {
            return isRented;
        }

        public boolean isDue() {
            return isRented && (LocalDateTime.now().isAfter(dueDate));
        }

        void getsRented(User user) {
            users.add(user);
            isRented = true;
            dueDate = LocalDateTime.now().plusDays(30);
        }

        public void getsReturned() {
            isRented = false;
        }

        private Copy(Book book, int pages, @NotNull Redaction redaction) {
            this.book = book;
            this.pages = pages;
            this.redaction = redaction;
            this.status = Status.NEW;
            isRented = false;
            users = new ArrayList<>();
        }

        @Override
        public int compareTo(Copy other) {
            return Integer.compare(this.status.ordinal(), other.status.ordinal());
        }
    }

}
