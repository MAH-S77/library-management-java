package part01;

import java.util.ArrayList;

public class Library {
    // Attribute
    private ArrayList<LibraryBook> books;
    
    // Constructor
    public Library() {
        books = new ArrayList<>();
    }
    
    // Add a new book to the library
    public boolean add(String title, String author, String isbn, BookType type, 
                       int edition, String summary, double price, String coverImage) {
        // Validate price (must be greater than £0.00)
        if (price <= 0) {
            return false;
        }
        
        try {
            // Create a new LibraryBook and add it to the ArrayList
            LibraryBook newBook = new LibraryBook(title, author, isbn, type, 
                                                  edition, summary, price, coverImage);
            books.add(newBook);
            return true;
        } catch (IllegalArgumentException e) {
            // If any validation fails in the Book constructor
            return false;
        }
    }
    
    // Remove a book from the library
    public boolean remove(int id) {
        LibraryBook book = search(id);
        if (book != null && book.getStatus() != BookStatus.ON_LOAN) {
            book.setStatus(BookStatus.WITHDRAWN);
            return true;
        }
        return false;
    }
    
    // Borrow a book from the library
    public boolean borrowBook(int id) {
        LibraryBook book = search(id);
        if (book != null) {
            return book.checkout();
        }
        return false;
    }
    
    // Return a book to the library
    public boolean returnBook(int id) {
        LibraryBook book = search(id);
        if (book != null) {
            return book.checkIn();
        }
        return false;
    }
    
    // List all books in the library
    public LibraryBook[] list() {
        return books.toArray(new LibraryBook[0]);
    }
    
    // List books by status
    public LibraryBook[] list(BookStatus status) {
        ArrayList<LibraryBook> filteredBooks = new ArrayList<>();
        
        for (LibraryBook book : books) {
            if (book.getStatus() == status) {
                filteredBooks.add(book);
            }
        }
        
        return filteredBooks.toArray(new LibraryBook[0]);
    }
    
    // Find a book by ID
    public LibraryBook search(int id) {
        for (LibraryBook book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;  // Book not found
    }
    
    // Get most popular books (ordered by number of times borrowed)
    public LibraryBook[] mostPopular() {
        // Copy books to a new array for sorting
        LibraryBook[] sortedBooks = books.toArray(new LibraryBook[0]);
        
        // Simple bubble sort by loan count (descending)
        for (int i = 0; i < sortedBooks.length - 1; i++) {
            for (int j = 0; j < sortedBooks.length - i - 1; j++) {
                if (sortedBooks[j].getLoanCount() < sortedBooks[j + 1].getLoanCount()) {
                    // Swap
                    LibraryBook temp = sortedBooks[j];
                    sortedBooks[j] = sortedBooks[j + 1];
                    sortedBooks[j + 1] = temp;
                }
            }
        }
        
        return sortedBooks;
    }
}