package com.mypackage.classes;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Library {
    //region Attributes
    static private Set<Author> authors = new TreeSet();
    static private Set<User> users = new TreeSet();
    static private Set<Section> sections = new TreeSet();
    static private Set<Genre> genres = new TreeSet();
    final static private Set<Book> books;
    static private Set<Redaction> redactions = new TreeSet();
    final static private Pattern patternEmail;
    final static private Pattern patternPhone;

    static {
        try {
            authors = (new Provider<Author>()).getInstance(Author.class).Read();
            sections = (new Provider<Section>()).getInstance(Section.class).Read();


            genres = (new Provider<Genre>()).getInstance(Genre.class).Read();
            redactions = (new Provider<Redaction>()).getInstance(Redaction.class).Read();

            users = (new Provider<User>()).getInstance(User.class).Read();
        } catch (Exception ignored) {
            System.out.println(ignored.getMessage());
        }
        books = new TreeSet<>();
        String regexEmail = "^(.+)@(.+)$";
        patternEmail = Pattern.compile(regexEmail);
        patternPhone = Pattern.compile("^\\d{10}$");
    }

    //endregion

    //region Collection manipulation
    //region Users
    public static class UserStore {
        public static Set<User> getUsers() {
            return Collections.unmodifiableSet(users);
        }

        private static boolean verifyCredential(String email, String phoneNumber) {
            return patternPhone.matcher(phoneNumber).matches() && patternEmail.matcher(email).matches();
        }

        public static void addUser(String name, String email, String phoneNumber) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
            if (getUser(email) == null && verifyCredential(email, phoneNumber)) {
                users.add(new User(name, email, phoneNumber));
                var j = (new Provider<User>()).getInstance(User.class);
                j.Update(users);
            }
        }

        public static User getUser(String email) {
            var opt = users.stream()
                    .filter(user -> user.getEmail().equalsIgnoreCase(email))
                    .findFirst();
            return opt.orElse(null);
        }

        public static void removeUser(String email) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
            var user = getUser(email);
            removeUser(user);
        }

        public static void removeUser(User user) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
            users.remove(user);
            var j = (new Provider<User>()).getInstance(User.class);
            j.Update(users);
        }
    }

    //endregion
    //region Authors
    public static class AuthorStore {
        public static Set<Author> getAuthors() {
            return Collections.unmodifiableSet(authors);
        }

        public static void addAuthor(String name) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
            if (getAuthor(name) == null) {
                authors.add(new Author(name));
                var j = (new Provider<Author>()).getInstance(Author.class);
                j.Update(authors);
            }

        }

        public static Author getAuthor(String authorName) {
            var opt = authors.stream()
                    .filter(author -> author.getName().equalsIgnoreCase(authorName))
                    .findFirst();
            return opt.orElse(null);
        }

        public static void removeAuthor(String authorName) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
            var author = getAuthor(authorName);
            removeAuthor(author);

        }

        public static void removeAuthor(Author author) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
            authors.remove(author);
            for (var i : author.getBooksWrote()) {
                BookStore.removeBook(i);
            }
            var j = (new Provider<Author>()).getInstance(Author.class);
            j.Update(authors);
        }
    }

    //endregion
    //region Sections
    public static class SectionStore {
        public static Set<Section> getSections() {
            return Collections.unmodifiableSet(sections);
        }

        public static void addSection(String name) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {

            if (getSection(name) == null) {
                sections.add(new Section(name));
                var j = (new Provider<Section>()).getInstance(Section.class);
                j.Update(sections);
            }
        }

        public static Section getSection(String sectionName) {
            var opt = sections.stream()
                    .filter(section -> section.getName().equalsIgnoreCase(sectionName))
                    .findFirst();
            return opt.orElse(null);
        }

        public static void removeSection(Section section) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
            sections.remove(section);
            for (var i : books) {
                if (i.getSection().equals(section))
                    i.setSection(null);
            }
            var j = (new Provider<Section>()).getInstance(Section.class);
            j.Update(sections);
        }

        public static void removeSection(String sectionName) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
            var section = getSection(sectionName);
            removeSection(section);
        }
    }

    //endregion
    //region Genres
    public static class GenreStore {
        public static Set<Genre> getGenres() {
            return Collections.unmodifiableSet(genres);
        }

        public static void addGenre(String name) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
            if (getGenre(name) == null) {
                genres.add(new Genre(name));
                var j = (new Provider<Genre>()).getInstance(Genre.class);
                j.Update(genres);
            }
        }

        public static Genre getGenre(String genreName) {
            var opt = genres.stream()
                    .filter(genre -> genre.getName().equalsIgnoreCase(genreName))
                    .findFirst();
            return opt.orElse(null);
        }

        public static void removeGenre(Genre genre) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
            genres.remove(genre);
            for (var i : books) {
                if (i.hasGenre(genre))
                    i.removeGenre(genre);
            }
            var j = (new Provider<Genre>()).getInstance(Genre.class);
            j.Update(genres);
        }

        public static void removeGenre(String genreName) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
            var genre = getGenre(genreName);
            removeGenre(genre);
        }
    }

    //endregion
    //region Books
    public static class BookStore {
        public static Set<Book> getBooks() {
            return Collections.unmodifiableSet(books);
        }

        public static void addBook(String name, Author author, Section section) {
            if (getBook(name) == null) {
                books.add(new Book(name, author, section));

            }
        }

        public static Book getBook(String bookName) {
            var opt = books.stream()
                    .filter(book -> book.getName().equalsIgnoreCase(bookName))
                    .findFirst();
            return opt.orElse(null);
        }

        public static void removeBook(Book book) {
            books.remove(book);
        }

        public static void removeBook(String bookName) {
            var book = getBook(bookName);

        }
    }

    //endregion

    //region Redactions

    //Only the redaction will not be able to be deleted, they can be renamed but never deleted
    //It would add too much complexity to be viable and the cases in which it would be used are too niche
    //An author might be censored by a regime but a redaction could appear even without any books

    public static class RedactionStore {

        public static Set<Redaction> getRedactions() {
            return Collections.unmodifiableSet(redactions);
        }

        public static void addRedaction(String name) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
            if (getRedaction(name) == null) {
                redactions.add(new Redaction(name));
                var j = (new Provider<Redaction>()).getInstance(Redaction.class);
                j.Update(redactions);
            }
        }

        public static Redaction getRedaction(String redactionName) {
            var opt = redactions.stream()
                    .filter(redaction -> redaction.getName().equalsIgnoreCase(redactionName))
                    .findFirst();
            return opt.orElse(null);
        }
    }
    //endregion
    //endregion

    //region Queries
    /*
     * 1.Get users with dues on books
     * 2.Get most popular books
     * 3.Get most avid readers(by number of pages)
     * 4.Get most popular authors
     * 5.Get most used sections
     * 6.Get most popular redactions
     * 7.Get most liked genres
     * 8.Count copies by genre
     * 9.Get every person in the database
     * 10.Count by status
     */

    //region 1
    public static List<User> getUsersWithDues() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        StackWalker walker = StackWalker.getInstance();
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        Provider.Audit.Write(methodName.get());
        return users.stream().filter(user -> {
            for (var i : user.getNowRentedBooks())
                if (i.isDue())
                    return true;
            return false;
        }).collect(Collectors.toList());
    }

    //endregion
    //region 2
    public static Map<Book, Integer> getBooksByPopularity() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        StackWalker walker = StackWalker.getInstance();
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        Provider.Audit.Write(methodName.get());
        Map<Book, Integer> bookCounter = new HashMap<>();
        for (var user : users) {
            for (var cop : user.getRentedBooks()) {
                var copy = cop.getValue();
                var count = bookCounter.getOrDefault(copy.getBook(), 0) + 1;
                bookCounter.put(copy.getBook(), count);
            }
        }

        return bookCounter;
    }

    //endregion
    //region 3
    public static List<User> getUsersByPages() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        return getUsersByPages(users.size());
    }

    public static List<User> getUsersByPages(int numberOfUsersToGet) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        //sort the array in descending order
        StackWalker walker = StackWalker.getInstance();
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        Provider.Audit.Write(methodName.get());
        return users.stream()
                .map(user -> {
                    //get each user with the number of pages he got
                    var numberOfPages = user.getRentedBooks().stream().map(AbstractMap.SimpleEntry::getValue)
                            .map(Book.Copy::getPages).reduce(0, Integer::sum);
                    return new AbstractMap.SimpleEntry<>(user, numberOfPages);
                }).sorted(
                        (o1, o2) -> -Integer.compare(o1.getValue(), o2.getValue()))
                .map(AbstractMap.SimpleEntry::getKey)
                .limit(numberOfUsersToGet)
                .collect(Collectors.toList());
        //get the users and limit it to the requested number of users
    }

    //endregion
    //region 4
    public static List<Author> getMostPopularAuthors() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        return getMostPopularAuthors(authors.size());
    }

    public static List<Author> getMostPopularAuthors(int numberOfAuthorsToGet) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        StackWalker walker = StackWalker.getInstance();
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        Provider.Audit.Write(methodName.get());
        Map<Author, Integer> authorCounter = new HashMap<>();
        for (var user : users) {
            for (var cop : user.getRentedBooks()) {
                var copy = cop.getValue();
                var count = authorCounter.getOrDefault(copy.getBook().getAuthor(), 0) + 1;
                authorCounter.put(copy.getBook().getAuthor(), count);
            }
        }
        return authorCounter.entrySet().stream()
                //make the entrySet sorted
                .sorted((author1, author2) -> -author1.getValue().compareTo(author2.getValue()))
                //get only sorted authors
                .map(Map.Entry::getKey)
                //limit to number of Authors needed
                .limit(numberOfAuthorsToGet).collect(Collectors.toList());
    }

    //endregion
    //region 5
    public static List<Section> getMostPopularSections() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        return getMostPopularSections(sections.size());

    }

    public static List<Section> getMostPopularSections(int numberOfSectionsToGet) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        StackWalker walker = StackWalker.getInstance();
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        Provider.Audit.Write(methodName.get());
        Map<Section, Integer> sectionCounter = new HashMap<>();
        for (var user : users) {
            for (var cop : user.getRentedBooks()) {
                var copy = cop.getValue();
                var count = sectionCounter.getOrDefault(copy.getBook().getSection(), 0) + 1;
                sectionCounter.put(copy.getBook().getSection(), count);
            }
        }

        return sectionCounter.entrySet().stream()
                //make the entrySet sorted
                .sorted((section1, section2) -> -section1.getValue().compareTo(section2.getValue()))
                //get only sorted sections
                .map(Map.Entry::getKey)
                //limit to number of sections needed
                .limit(numberOfSectionsToGet).collect(Collectors.toList());
    }

    //endregion
    //region 6
    public static List<Redaction> getMostPopularRedactions() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        return getMostPopularRedactions(redactions.size());
    }

    public static List<Redaction> getMostPopularRedactions(int numberOfRedactionsToGet) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        StackWalker walker = StackWalker.getInstance();
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        Provider.Audit.Write(methodName.get());
        Map<Redaction, Integer> redactionCounter = new HashMap<>();
        for (var user : users) {
            for (var cop : user.getRentedBooks()) {
                var copy = cop.getValue();
                var count = redactionCounter.getOrDefault(copy.getRedaction(), 0) + 1;
                redactionCounter.put(copy.getRedaction(), count);
            }
        }
        return redactionCounter.entrySet().stream()
                //make the entrySet sorted
                .sorted((redaction1, redaction2) -> -redaction1.getValue().compareTo(redaction2.getValue()))
                //get only sorted redactions
                .map(Map.Entry::getKey)
                //limit to number of redactions needed
                .limit(numberOfRedactionsToGet).collect(Collectors.toList());
    }

    //endregion
    //region 7
    public static List<Genre> getMostPopularGenres() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        return getMostPopularGenres(genres.size());
    }

    public static List<Genre> getMostPopularGenres(int numberOfGenresToGet) throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        StackWalker walker = StackWalker.getInstance();
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        Provider.Audit.Write(methodName.get());
        Map<Genre, Integer> genreCounter = new HashMap<>();
        for (var user : users) {
            for (var copy : user.getRentedBooks()) {
                for (Genre genre : copy.getValue().getBook().getGenres()) {
                    var count = genreCounter.getOrDefault(genre, 0) + 1;
                    genreCounter.put(genre, count);
                }
            }
        }
        return genreCounter.entrySet().stream()
                //make the entrySet sorted
                .sorted((genre1, genre2) -> -genre1.getValue().compareTo(genre2.getValue()))
                //get only sorted redactions
                .map(Map.Entry::getKey)
                //limit to number of redactions needed
                .limit(numberOfGenresToGet).collect(Collectors.toList());
    }

    //endregion
    //region 8
    public static Map<Genre, Integer> getNumberOfCopiesByGenre() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        StackWalker walker = StackWalker.getInstance();
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        Provider.Audit.Write(methodName.get());
        Map<Genre, Integer> genreCounter = new HashMap<>();
        for (var book : books) {
            for (Genre genre : book.getGenres()) {
                var count = genreCounter.getOrDefault(genre, 0) + book.getCopies().size();
                genreCounter.put(genre, count);
            }
        }
        return genreCounter;
    }

    //endregion
    //region 9
    public static List<Person> getEveryHuman() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        StackWalker walker = StackWalker.getInstance();
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        Provider.Audit.Write(methodName.get());
        return Stream.concat(authors.stream(), users.stream()).sorted().collect(Collectors.toList());
    }

    //endregion
    //region 10
    public static Map<Status, Integer> countByStatus() throws CsvRequiredFieldEmptyException, IOException, CsvDataTypeMismatchException {
        StackWalker walker = StackWalker.getInstance();
        Optional<String> methodName = walker.walk(frames -> frames
                .findFirst()
                .map(StackWalker.StackFrame::getMethodName));
        Provider.Audit.Write(methodName.get());
        Map<Status, Integer> statusCounter = new HashMap<>();
        //Any unusable copy is discarded so of course it will be 0
        var numberOfStatusesWeCareOf = Status.getValues().size() - 1;

        //start counter at zero for all statuses
        for (int i = 0; i < numberOfStatusesWeCareOf; i++) {
            statusCounter.put(Status.getValues().get(i), 0);
        }

        for (var book : books) {
            for (var copy : book.getCopies()) {
                var count = statusCounter.get(copy.getStatus()) + 1;
                statusCounter.put(copy.getStatus(), count);
            }
        }
        return statusCounter;
    }
//endregion
    //endregion

    //region Util implementation
    private Library() {
    }
    //endregion
}
