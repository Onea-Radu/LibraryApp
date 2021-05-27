package com.mypackage;

import com.mypackage.classes.*;
import com.mypackage.classes.Library.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;


public class MainApp {

    public static void main(String[] args) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        
        UserStore.addUser("Radu", "onearadu12@gmail.com", "0722460830");
        SectionStore.addSection("Literature");
        SectionStore.addSection("Philosophy");
        SectionStore.addSection("Computer Science");
        RedactionStore.addRedaction("Nemira");
        RedactionStore.addRedaction("Paralela45");
        AuthorStore.addAuthor("Dostoevsky");
        AuthorStore.addAuthor("Nietzsche");
        BookStore.addBook("Crime and Punishment",
                AuthorStore.getAuthor("Dostoevsky"),
                SectionStore.getSection("Literature"));
        BookStore.addBook("Thus spake Zarathustra",
                AuthorStore.getAuthor("Nietzsche"),
                SectionStore.getSection("Philosophy"));
        GenreStore.addGenre("Existentialism");
        GenreStore.addGenre("Nihilism");
        GenreStore.addGenre("Realism");
        BookStore.getBook("Thus Spake Zarathustra")
                .addGenre(GenreStore.getGenre("Existentialism"));
        BookStore.getBook("Thus Spake Zarathustra")
                .addGenre(GenreStore.getGenre("Nihilism"));
        BookStore.getBook("Crime and Punishment")
                .addGenre(GenreStore.getGenre("Existentialism"));
        BookStore.getBook("Crime and Punishment")
                .addGenre(GenreStore.getGenre("Realism"));
        BookStore.getBook("Thus Spake Zarathustra")
                .makeCopy(126, RedactionStore.getRedaction("Paralela45"));
        BookStore.getBook("Thus Spake Zarathustra")
                .makeCopy(126, RedactionStore.getRedaction("Paralela45")).damage();
        System.out.println(UserStore.getUser("onearadu12@gmail.com").rent(BookStore.getBook("Thus Spake Zarathustra")));
        System.out.println(UserStore.getUser("onearadu12@gmail.com").rent(BookStore.getBook("Crime and Punishment")));
        var h = BookStore.getBook("Crime and Punishment")
                .makeCopy(126, RedactionStore.getRedaction("Nemira"));
        h.damage();
        h.damage();
        h.damage();
        System.out.println(Library.countByStatus());
        System.out.println(Library.getEveryHuman());
        System.out.println(Library.getBooksByPopularity());
        System.out.println(Library.getMostPopularAuthors());
        System.out.println(Library.getMostPopularGenres());
        System.out.println(Library.getMostPopularRedactions());
        System.out.println(Library.getUsersWithDues());
        System.out.println(Library.getNumberOfCopiesByGenre());
        System.out.println(Library.getUsersByPages());
        System.out.println(Library.getMostPopularSections());

    }
}
