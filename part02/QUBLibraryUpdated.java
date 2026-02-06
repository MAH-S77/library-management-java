package part02;

import part01.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.concurrent.CountDownLatch;

public class QUBLibraryUpdated {
    private static JFrame frame;
    private static JTextArea textArea;
    private static JTextField inputField;
    private static JButton submitButton;
    private static JPanel mainPanel;
    private static Library library = new Library();
    private static String userInput = "";
    private static boolean inputReceived = false;
    private static CountDownLatch inputLatch;
    
    public static void main(String[] args) {
        // Set up the GUI
        initializeGUI();
        
        // Add some sample books to the library
        addSampleBooks();
        
        // Run the main application
        runApplication();
    }
    
    // Set up the GUI components
    private static void initializeGUI() {
        frame = new JFrame("QUB Library Management System");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255)); // Light blue background
        
        // Text area for output
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setBackground(new Color(250, 250, 250));
        JScrollPane scrollPane = new JScrollPane(textArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel for input
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        submitButton = new JButton("Submit");
        
        ActionListener inputAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userInput = inputField.getText();
                inputField.setText("");
                inputReceived = true;
                if (inputLatch != null) {
                    inputLatch.countDown();
                }
            }
        };
        
        submitButton.addActionListener(inputAction);
        inputField.addActionListener(inputAction);
        
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(submitButton, BorderLayout.EAST);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    // Custom implementation of Scanner.nextLine() for GUI
    private static String readLine() {
        inputReceived = false;
        userInput = "";
        inputLatch = new CountDownLatch(1);
        
        inputField.requestFocus();
        
        try {
            inputLatch.await(); // Wait for user input
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return userInput;
    }
    
    // Run the main application logic
    private static void runApplication() {
        // Display welcome screen
        displayWelcomeScreen();
        
        // Define menu options - same as in the original QUBLibrary
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
        
        // Create a custom menu that uses our GUI
        CustomMenu menu = new CustomMenu("QUB Library Management System", menuOptions);
        
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
                    println("\nExiting the system. Goodbye!");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        // Ignore
                    }
                    frame.dispose();
                    break;
            }
        }
    }
    
    // Custom Menu class that uses our GUI instead of System.in
    private static class CustomMenu {
        private String[] options;
        private String title;
        
        public CustomMenu(String title, String[] options) {
            this.title = title;
            this.options = options;
        }
        
        public int getChoice() {
            int choice = 0;
            boolean validInput = false;
            
            while (!validInput) {
                clear();
                println("\n" + title);
                println("=".repeat(title.length()));
                
                // Display menu options
                for (int i = 0; i < options.length; i++) {
                    println((i + 1) + ". " + options[i]);
                }
                
                print("\nEnter your choice (1-" + options.length + "): ");
                
                try {
                    choice = Integer.parseInt(readLine());
                    if (choice >= 1 && choice <= options.length) {
                        validInput = true;
                    } else {
                        println("Error: Please enter a number between 1 and " + options.length);
                    }
                } catch (NumberFormatException e) {
                    println("Error: Please enter a valid number");
                }
            }
            
            return choice;
        }
    }
    
    // Method to print text to the text area
    private static void print(String text) {
        SwingUtilities.invokeLater(() -> {
            textArea.append(text);
            textArea.setCaretPosition(textArea.getDocument().getLength());
        });
    }
    
    // Method to print text with a newline
    private static void println(String text) {
        SwingUtilities.invokeLater(() -> {
            textArea.append(text + "\n");
            textArea.setCaretPosition(textArea.getDocument().getLength());
        });
    }
    
    // Method to clear the text area
    private static void clear() {
        SwingUtilities.invokeLater(() -> {
            textArea.setText("");
        });
    }
    
    // Display welcome screen
    private static void displayWelcomeScreen() {
        clear();
        
        // Set title with nice formatting
        textArea.setFont(new Font("Arial", Font.BOLD, 20));
        textArea.setForeground(new Color(0, 102, 204)); // Blue color
        println("\n\n          Welcome to QUB Library Management System");
        println("          =======================================");
        
        // Set normal text formatting
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setForeground(Color.BLACK);
        println("\n          A comprehensive solution for managing library books");
        println("          Developed for QUB Library Department");
        
        // Try to display library image
        try {
            ImageIcon icon = new ImageIcon("Images/library.jpg");
            if (icon.getIconWidth() > 0) {
                JLabel imageLabel = new JLabel(icon);
                JOptionPane.showMessageDialog(frame, imageLabel, "Welcome", JOptionPane.PLAIN_MESSAGE);
            }
        } catch (Exception e) {
            // Continue without image if it fails to load
        }
        
        println("\n\nPress Enter to continue...");
        readLine();
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
        clear();
        
        // Set title formatting
        textArea.setFont(new Font("Arial", Font.BOLD, 16));
        textArea.setForeground(new Color(0, 102, 204));
        println("\n=== All Books ===");
        
        // Reset text formatting
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setForeground(Color.BLACK);
        
        LibraryBook[] books = library.list();
        
        if (books.length == 0) {
            println("No books in the library.");
        } else {
            displayBooks(books);
        }
        
        // Display book images in a separate panel
        displayBookImages(books);
        
        println("\nPress Enter to continue...");
        readLine();
    }
    
    // Method to list books by status
    private static void listBooksByStatus() {
        clear();
        
        // Set title formatting
        textArea.setFont(new Font("Arial", Font.BOLD, 16));
        textArea.setForeground(new Color(0, 102, 204));
        println("\n=== List Books by Status ===");
        
        // Reset text formatting
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setForeground(Color.BLACK);
        
        println("1. Available");
        println("2. On Loan");
        println("3. Withdrawn");
        print("Enter status choice: ");
        
        int statusChoice = getIntInput(1, 3);
        BookStatus status = null;
        
        switch (statusChoice) {
            case 1:
                status = BookStatus.AVAILABLE;
                textArea.setForeground(new Color(0, 153, 0)); // Green for available
                break;
            case 2:
                status = BookStatus.ON_LOAN;
                textArea.setForeground(new Color(204, 102, 0)); // Orange for on loan
                break;
            case 3:
                status = BookStatus.WITHDRAWN;
                textArea.setForeground(new Color(204, 0, 0)); // Red for withdrawn
                break;
        }
        
        println("\n=== Books with status: " + status + " ===");
        textArea.setForeground(Color.BLACK);
        
        LibraryBook[] books = library.list(status);
        
        if (books.length == 0) {
            println("No books with this status.");
        } else {
            displayBooks(books);
        }
        
        // Display book images in a separate panel
        displayBookImages(books);
        
        println("\nPress Enter to continue...");
        readLine();
    }
    
    // Method to display books in a formatted way
    private static void displayBooks(LibraryBook[] books) {
        // Create a table header
        println("\nID | Title                     | Author                    | Status    | Price    | Borrowed");
        println("---+---------------------------+---------------------------+-----------+----------+----------");
        
        for (LibraryBook book : books) {
            // Set color based on status
            if (book.getStatus() == BookStatus.AVAILABLE) {
                textArea.setForeground(new Color(0, 153, 0)); // Green
            } else if (book.getStatus() == BookStatus.ON_LOAN) {
                textArea.setForeground(new Color(204, 102, 0)); // Orange
            } else {
                textArea.setForeground(new Color(204, 0, 0)); // Red
            }
            
            String title = truncateString(book.getTitle(), 25);
            String author = truncateString(book.getAuthor(), 25);
            
            println(String.format("%-2d | %-25s | %-25s | %-9s | £%-8.2f | %d", 
                    book.getId(), title, author, book.getStatus(), 
                    book.getPrice(), book.getLoanCount()));
            
            textArea.setForeground(Color.BLACK);
        }
    }
    
    // Method to display book images in a panel
    private static void displayBookImages(LibraryBook[] books) {
        if (books.length > 0 && books.length <= 3) {
            JPanel imagePanel = new JPanel(new GridLayout(1, books.length, 10, 10));
            boolean hasImages = false;
            
            for (LibraryBook book : books) {
                try {
                    ImageIcon icon = new ImageIcon("Images/" + book.getImage());
                    if (icon.getIconWidth() > 0) {
                        JLabel imageLabel = new JLabel(icon);
                        imageLabel.setBorder(BorderFactory.createTitledBorder(book.getTitle()));
                        imagePanel.add(imageLabel);
                        hasImages = true;
                    }
                } catch (Exception e) {
                    // Skip if image can't be loaded
                }
            }
            
            if (hasImages) {
                JOptionPane.showMessageDialog(frame, imagePanel, "Book Covers", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }
    
    // Method to add a new book
    private static void addBook() {
        clear();
        
        // Set title formatting
        textArea.setFont(new Font("Arial", Font.BOLD, 16));
        textArea.setForeground(new Color(0, 102, 204));
        println("\n=== Add a New Book ===");
        
        // Reset text formatting
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setForeground(Color.BLACK);
        
        try {
            print("Enter title (5-40 characters): ");
            String title = readLine();
            
            print("Enter author (5-40 characters): ");
            String author = readLine();
            
            print("Enter ISBN (exactly 10 digits): ");
            String isbn = readLine();
            
            println("Select book type:");
            println("1. Fiction");
            println("2. Non-Fiction");
            println("3. Reference");
            print("Enter type choice: ");
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
            
            print("Enter edition (must be >= 1): ");
            int edition = getIntInput(1, Integer.MAX_VALUE);
            
            print("Enter summary (20-150 characters): ");
            String summary = readLine();
            
            print("Enter price (£): ");
            double price = getDoubleInput(0.01, Double.MAX_VALUE);
            
            print("Enter cover image filename: ");
            String coverImage = readLine();
            
            boolean success = library.add(title, author, isbn, type, edition, summary, price, coverImage);
            
            if (success) {
                textArea.setForeground(new Color(0, 153, 0)); // Green for success
                println("Book added successfully!");
            } else {
                textArea.setForeground(new Color(204, 0, 0)); // Red for failure
                println("Failed to add book. Please check your input.");
            }
            
            textArea.setForeground(Color.BLACK);
            
        } catch (Exception e) {
            textArea.setForeground(new Color(204, 0, 0)); // Red for error
            println("Error adding book: " + e.getMessage());
            textArea.setForeground(Color.BLACK);
        }
        
        println("\nPress Enter to continue...");
        readLine();
    }
    
    // Method to remove a book
    private static void removeBook() {
        clear();
        
        // Set title formatting
        textArea.setFont(new Font("Arial", Font.BOLD, 16));
        textArea.setForeground(new Color(0, 102, 204));
        println("\n=== Remove a Book ===");
        
        // Reset text formatting
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setForeground(Color.BLACK);
        
        // Display available books
        LibraryBook[] books = library.list();
        if (books.length == 0) {
            println("No books in the library to remove.");
            println("\nPress Enter to continue...");
            readLine();
            return;
        }
        
        println("Select a book to remove:");
        for (LibraryBook book : books) {
            // Set color based on status
            if (book.getStatus() == BookStatus.AVAILABLE) {
                textArea.setForeground(new Color(0, 153, 0)); // Green
            } else if (book.getStatus() == BookStatus.ON_LOAN) {
                textArea.setForeground(new Color(204, 102, 0)); // Orange
            } else {
                textArea.setForeground(new Color(204, 0, 0)); // Red
            }
            
            println("ID: " + book.getId() + " - " + book.getTitle() + " (" + book.getStatus() + ")");
            textArea.setForeground(Color.BLACK);
        }
        
        print("\nEnter book ID to remove: ");
        int id = getIntInput(1, Integer.MAX_VALUE);
        
        boolean success = library.remove(id);
        if (success) {
            textArea.setForeground(new Color(0, 153, 0)); // Green for success
            println("Book removed successfully (status set to WITHDRAWN)!");
        } else {
            textArea.setForeground(new Color(204, 0, 0)); // Red for failure
            println("Failed to remove book. It may not exist or is currently on loan.");
        }
        
        textArea.setForeground(Color.BLACK);
        println("\nPress Enter to continue...");
        readLine();
    }
    
    // Method to borrow a book
    private static void borrowBook() {
        clear();
        
        // Set title formatting
        textArea.setFont(new Font("Arial", Font.BOLD, 16));
        textArea.setForeground(new Color(0, 102, 204));
        println("\n=== Borrow a Book ===");
        
        // Reset text formatting
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setForeground(Color.BLACK);
        
        // Display available books
        LibraryBook[] availableBooks = library.list(BookStatus.AVAILABLE);
        if (availableBooks.length == 0) {
            println("No books available to borrow.");
            println("\nPress Enter to continue...");
            readLine();
            return;
        }
        
        println("Select a book to borrow:");
        textArea.setForeground(new Color(0, 153, 0)); // Green for available books
        
        for (LibraryBook book : availableBooks) {
            println("ID: " + book.getId() + " - " + book.getTitle());
        }
        
        textArea.setForeground(Color.BLACK);
        
        // Display book images
        displayBookImages(availableBooks);
        
        print("\nEnter book ID to borrow: ");
        int id = getIntInput(1, Integer.MAX_VALUE);
        
        boolean success = library.borrowBook(id);
        if (success) {
            textArea.setForeground(new Color(0, 153, 0)); // Green for success
            println("Book borrowed successfully!");
        } else {
            textArea.setForeground(new Color(204, 0, 0)); // Red for failure
            println("Failed to borrow book. It may not exist or is not available.");
        }
        
        textArea.setForeground(Color.BLACK);
        println("\nPress Enter to continue...");
        readLine();
    }
    
    // Method to return a book
    private static void returnBook() {
        clear();
        
        // Set title formatting
        textArea.setFont(new Font("Arial", Font.BOLD, 16));
        textArea.setForeground(new Color(0, 102, 204));
        println("\n=== Return a Book ===");
        
        // Reset text formatting
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setForeground(Color.BLACK);
        
        // Display books on loan
        LibraryBook[] booksOnLoan = library.list(BookStatus.ON_LOAN);
        if (booksOnLoan.length == 0) {
            println("No books currently on loan.");
            println("\nPress Enter to continue...");
            readLine();
            return;
        }
        
        println("Select a book to return:");
        textArea.setForeground(new Color(204, 102, 0)); // Orange for on loan books
        
        for (LibraryBook book : booksOnLoan) {
            println("ID: " + book.getId() + " - " + book.getTitle());
        }
        
        textArea.setForeground(Color.BLACK);
        
        // Display book images
        displayBookImages(booksOnLoan);
        
        print("\nEnter book ID to return: ");
        int id = getIntInput(1, Integer.MAX_VALUE);
        
        boolean success = library.returnBook(id);
        if (success) {
            textArea.setForeground(new Color(0, 153, 0)); // Green for success
            println("Book returned successfully!");
        } else {
            textArea.setForeground(new Color(204, 0, 0)); // Red for failure
            println("Failed to return book. It may not exist or is not on loan.");
        }
        
        textArea.setForeground(Color.BLACK);
        println("\nPress Enter to continue...");
        readLine();
    }
    
    // Method to display ranked list of books by popularity
    private static void displayRankedList() {
        clear();
        
        // Set title formatting
        textArea.setFont(new Font("Arial", Font.BOLD, 16));
        textArea.setForeground(new Color(0, 102, 204));
        println("\n=== Most Popular Books ===");
        
        // Reset text formatting
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setForeground(Color.BLACK);
        
        LibraryBook[] popularBooks = library.mostPopular();
        if (popularBooks.length == 0) {
            println("No books in the library.");
            println("\nPress Enter to continue...");
            readLine();
            return;
        }
        
        println("Books ranked by number of times borrowed:");
        println("\nRank | Title                     | Author                    | Times Borrowed");
        println("-----+---------------------------+---------------------------+---------------");
        
        // Display books with medals for top 3
        for (int i = 0; i < popularBooks.length; i++) {
            LibraryBook book = popularBooks[i];
            String title = truncateString(book.getTitle(), 25);
            String author = truncateString(book.getAuthor(), 25);
            
            // Color coding for rankings
            if (i == 0 && book.getLoanCount() > 0) {
                textArea.setForeground(new Color(255, 215, 0)); // Gold for 1st
                println(String.format("%-4d | %-25s | %-25s | %-15d 🥇", 
                        (i + 1), title, author, book.getLoanCount()));
            } else if (i == 1 && book.getLoanCount() > 0) {
                textArea.setForeground(new Color(192, 192, 192)); // Silver for 2nd
                println(String.format("%-4d | %-25s | %-25s | %-15d 🥈", 
                        (i + 1), title, author, book.getLoanCount()));
            } else if (i == 2 && book.getLoanCount() > 0) {
                textArea.setForeground(new Color(205, 127, 50)); // Bronze for 3rd
                println(String.format("%-4d | %-25s | %-25s | %-15d 🥉", 
                        (i + 1), title, author, book.getLoanCount()));
            } else {
                textArea.setForeground(Color.BLACK);
                println(String.format("%-4d | %-25s | %-25s | %-15d", 
                        (i + 1), title, author, book.getLoanCount()));
            }
        }
        
        // Try to display medal images for top 3
        if (popularBooks.length > 0 && popularBooks[0].getLoanCount() > 0) {
            try {
                JPanel medalPanel = new JPanel(new GridLayout(Math.min(3, popularBooks.length), 2, 10, 10));
                boolean hasMedals = false;
                
                for (int i = 0; i < Math.min(3, popularBooks.length); i++) {
                    if (popularBooks[i].getLoanCount() > 0) {
                        try {
                            // Try to load medal image
                            ImageIcon medalIcon = new ImageIcon("Images/medal" + (i+1) + ".jpg");
                            if (medalIcon.getIconWidth() > 0) {
                                JLabel medalLabel = new JLabel(medalIcon);
                                
                                // Try to load book cover
                                ImageIcon bookIcon = new ImageIcon("Images/" + popularBooks[i].getImage());
                                JLabel bookLabel = new JLabel(bookIcon);
                                
                                JPanel bookPanel = new JPanel(new BorderLayout());
                                bookPanel.add(bookLabel, BorderLayout.CENTER);
                                bookPanel.add(new JLabel(popularBooks[i].getTitle(), SwingConstants.CENTER), BorderLayout.SOUTH);
                                
                                medalPanel.add(medalLabel);
                                medalPanel.add(bookPanel);
                                hasMedals = true;
                            }
                        } catch (Exception e) {
                            // Continue if medal image isn't found
                        }
                    }
                }
                
                if (hasMedals) {
                    JOptionPane.showMessageDialog(frame, medalPanel, "Top Borrowed Books", JOptionPane.PLAIN_MESSAGE);
                }
            } catch (Exception e) {
                // Continue if there's an issue with displaying medals
            }
        }
        
        textArea.setForeground(Color.BLACK);
        println("\nPress Enter to continue...");
        readLine();
    }
    
    // Helper method to get a validated integer input
    private static int getIntInput(int min, int max) {
        int value = 0;
        boolean valid = false;
        
        while (!valid) {
            try {
                String input = readLine();
                value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    valid = true;
                } else {
                    textArea.setForeground(new Color(204, 0, 0)); // Red for error
                    print("Please enter a number between " + min + " and " + max + ": ");
                    textArea.setForeground(Color.BLACK);
                }
            } catch (NumberFormatException e) {
                textArea.setForeground(new Color(204, 0, 0)); // Red for error
                print("Please enter a valid integer: ");
                textArea.setForeground(Color.BLACK);
            }
        }
        
        return value;
    }
    
    // Helper method to get a validated double input
    private static double getDoubleInput(double min, double max) {
        double value = 0;
        boolean valid = false;
        
        while (!valid) {
            try {
                String input = readLine();
                value = Double.parseDouble(input);
                if (value >= min && value <= max) {
                    valid = true;
                } else {
                    textArea.setForeground(new Color(204, 0, 0)); // Red for error
                    print("Please enter a number between " + min + " and " + max + ": ");
                    textArea.setForeground(Color.BLACK);
                }
            } catch (NumberFormatException e) {
                textArea.setForeground(new Color(204, 0, 0)); // Red for error
                print("Please enter a valid number: ");
                textArea.setForeground(Color.BLACK);
            }
        }
        
        return value;
    }
    
    // Helper method to truncate strings to a certain length
    private static String truncateString(String str, int length) {
        if (str.length() <= length) {
            return str;
        }
        return str.substring(0, length - 3) + "...";
    }
}
