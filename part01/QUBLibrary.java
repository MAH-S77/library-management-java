package part01;

import java.util.Scanner;

public class QUBLibrary {
    private static Scanner scanner = new Scanner(System.in);
    private static Library library = new Library();
    
    public static void main(String[] args) {
        // Add some sample books to the library
        addSampleBooks();
        
        // Define menu options
        String[] menuOptions = {
            "List All Books",
            "List Books by Status",
            "Add a Book",
            "Remove a Book",
            "Borrow a Book",
            "Return a Book",
            "Display Ranked List",
            "Exit"
        };
        
        Menu menu = new Menu("QUB Library Management System", menuOptions);
        
        boolean running = true;
        while (running) {
            int choice = menu.getChoice();
            
            switch (choice) {
                case 1:
                    listAllBooks();
                    break;
                case 2:
                    listBooksByStatus();
                    break;
                case 3:
                    addBook();
                    break;
                case 4:
                    removeBook();
                    break;
                case 5:
                    borrowBook();
                    break;
                case 6:
                    returnBook();
                    break;
                case 7:
                    displayRankedList();
                    break;
                case 8:
                    running = false;
                    System.out.println("Exiting the system. Goodbye!");
                    break;
            }
        }
    }
    
    // Method to add sample books to the library
    private static void addSampleBooks() {
        library.add("Programming in Java", "James Gosling", "1234567890", BookType.NON_FICTION, 
                    3, "A comprehensive guide to Java programming language for beginners.", 29.99, "java_book.jpg");
                    
        library.add("Harry Potter and the Philosopher's Stone", "J.K. Rowling", "0987654321", BookType.FICTION, 
                    1, "The first book in the Harry Potter series about a young wizard.", 19.99, "harry_potter.jpg");
                    
        library.add("Oxford English Dictionary", "Oxford Press", "5678901234", BookType.REFERENCE, 
                    10, "The definitive record of the English language featuring 600,000 words.", 59.99, "dictionary.jpg");
                    
        library.add("The Lord of the Rings", "J.R.R. Tolkien", "2345678901", BookType.FICTION, 
                    5, "An epic fantasy novel about a quest to destroy a powerful ring.", 24.99, "lotr.jpg");
                    
        library.add("Introduction to Algorithms", "Thomas Cormen", "3456789012", BookType.NON_FICTION, 
                    4, "A comprehensive introduction to modern algorithmic techniques.", 49.99, "algorithms.jpg");
    }
    
    // Method to list all books
    private static void listAllBooks() {
        System.out.println("\n=== All Books ===");
        LibraryBook[] books = library.list();
        
        if (books.length == 0) {
            System.out.println("No books in the library.");
        } else {
            for (LibraryBook book : books) {
                System.out.println(book);
            }
        }
    }
    
    // Method to list books by status
    private static void listBooksByStatus() {
        System.out.println("\n=== List Books by Status ===");
        System.out.println("1. Available");
        System.out.println("2. On Loan");
        System.out.println("3. Withdrawn");
        System.out.print("Enter status choice: ");
        
        int statusChoice = getIntInput(1, 3);
        BookStatus status = null;
        
        switch (statusChoice) {
            case 1:
                status = BookStatus.AVAILABLE;
                break;
            case 2:
                status = BookStatus.ON_LOAN;
                break;
            case 3:
                status = BookStatus.WITHDRAWN;
                break;
        }
        
        LibraryBook[] books = library.list(status);
        
        System.out.println("\n=== Books with status: " + status + " ===");
        if (books.length == 0) {
            System.out.println("No books with this status.");
        } else {
            for (LibraryBook book : books) {
                System.out.println(book);
            }
        }
    }
    
    // Method to add a new book
    private static void addBook() {
        System.out.println("\n=== Add a New Book ===");
        
        try {
            System.out.print("Enter title (5-40 characters): ");
            String title = scanner.nextLine();
            
            System.out.print("Enter author (5-40 characters): ");
            String author = scanner.nextLine();
            
            System.out.print("Enter ISBN (exactly 10 digits): ");
            String isbn = scanner.nextLine();
            
            System.out.println("Select book type:");
            System.out.println("1. Fiction");
            System.out.println("2. Non-Fiction");
            System.out.println("3. Reference");
            System.out.print("Enter type choice: ");
            int typeChoice = getIntInput(1, 3);
            
            BookType type = null;
            switch (typeChoice) {
                case 1:
                    type = BookType.FICTION;
                    break;
                case 2:
                    type = BookType.NON_FICTION;
                    break;
                case 3:
                    type = BookType.REFERENCE;
                    break;
            }
            
            System.out.print("Enter edition (must be >= 1): ");
            int edition = getIntInput(1, Integer.MAX_VALUE);
            
            System.out.print("Enter summary (20-150 characters): ");
            String summary = scanner.nextLine();
            
            System.out.print("Enter price (£): ");
            double price = getDoubleInput(0.01, Double.MAX_VALUE);
            
            System.out.print("Enter cover image filename: ");
            String coverImage = scanner.nextLine();
            
            boolean success = library.add(title, author, isbn, type, edition, summary, price, coverImage);
            
            if (success) {
                System.out.println("Book added successfully!");
            } else {
                System.out.println("Failed to add book. Please check your input.");
            }
            
        } catch (Exception e) {
            System.out.println("Error adding book: " + e.getMessage());
        }
    }
    
    // Method to remove a book
    private static void removeBook() {
        System.out.println("\n=== Remove a Book ===");
        
        // Display available books
        LibraryBook[] books = library.list();
        if (books.length == 0) {
            System.out.println("No books in the library to remove.");
            return;
        }
        
        System.out.println("Select a book to remove:");
        for (LibraryBook book : books) {
            System.out.println("ID: " + book.getId() + " - " + book.getTitle() + " (" + book.getStatus() + ")");
        }
        
        System.out.print("Enter book ID to remove: ");
        int id = getIntInput(1, Integer.MAX_VALUE);
        
        boolean success = library.remove(id);
        if (success) {
            System.out.println("Book removed successfully (status set to WITHDRAWN)!");
        } else {
            System.out.println("Failed to remove book. It may not exist or is currently on loan.");
        }
    }
    
    // Method to borrow a book
    private static void borrowBook() {
        System.out.println("\n=== Borrow a Book ===");
        
        // Display available books
        LibraryBook[] availableBooks = library.list(BookStatus.AVAILABLE);
        if (availableBooks.length == 0) {
            System.out.println("No books available to borrow.");
            return;
        }
        
        System.out.println("Select a book to borrow:");
        for (LibraryBook book : availableBooks) {
            System.out.println("ID: " + book.getId() + " - " + book.getTitle());
        }
        
        System.out.print("Enter book ID to borrow: ");
        int id = getIntInput(1, Integer.MAX_VALUE);
        
        boolean success = library.borrowBook(id);
        if (success) {
            System.out.println("Book borrowed successfully!");
        } else {
            System.out.println("Failed to borrow book. It may not exist or is not available.");
        }
    }
    
    // Method to return a book
    private static void returnBook() {
        System.out.println("\n=== Return a Book ===");
        
        // Display books on loan
        LibraryBook[] booksOnLoan = library.list(BookStatus.ON_LOAN);
        if (booksOnLoan.length == 0) {
            System.out.println("No books currently on loan.");
            return;
        }
        
        System.out.println("Select a book to return:");
        for (LibraryBook book : booksOnLoan) {
            System.out.println("ID: " + book.getId() + " - " + book.getTitle());
        }
        
        System.out.print("Enter book ID to return: ");
        int id = getIntInput(1, Integer.MAX_VALUE);
        
        boolean success = library.returnBook(id);
        if (success) {
            System.out.println("Book returned successfully!");
        } else {
            System.out.println("Failed to return book. It may not exist or is not on loan.");
        }
    }
    
    // Method to display ranked list of books by popularity
    private static void displayRankedList() {
        System.out.println("\n=== Most Popular Books ===");
        
        LibraryBook[] popularBooks = library.mostPopular();
        if (popularBooks.length == 0) {
            System.out.println("No books in the library.");
            return;
        }
        
        System.out.println("Books ranked by number of times borrowed:");
        for (int i = 0; i < popularBooks.length; i++) {
            LibraryBook book = popularBooks[i];
            System.out.println((i + 1) + ". " + book.getTitle() + " by " + book.getAuthor() + 
                              " - Borrowed " + book.getLoanCount() + " times");
        }
    }
    
    // Helper method to get validated integer input
    private static int getIntInput(int min, int max) {
        int value = 0;
        boolean valid = false;
        
        while (!valid) {
            try {
                value = Integer.parseInt(scanner.nextLine());
                if (value >= min && value <= max) {
                    valid = true;
                } else {
                    System.out.print("Please enter a number between " + min + " and " + max + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid integer: ");
            }
        }
        
        return value;
    }
    
    // Helper method to get validated double input
    private static double getDoubleInput(double min, double max) {
        double value = 0;
        boolean valid = false;
        
        while (!valid) {
            try {
                value = Double.parseDouble(scanner.nextLine());
                if (value >= min && value <= max) {
                    valid = true;
                } else {
                    System.out.print("Please enter a number between " + min + " and " + max + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
        
        return value;
    }
}
