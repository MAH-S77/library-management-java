package part01;

public class Book {
    // Attributes
    private String title;
    private String author;
    private String isbn;
    private BookType type;
    private int edition;
    private String summary;
    private double price;
    
    // Constructor
    public Book(String title, String author, String isbn, BookType type, int edition, String summary, double price) {
        // Validate and set title
        if (title == null || title.length() < 5 || title.length() > 40) {
            throw new IllegalArgumentException("Title must be between 5 and 40 characters");
        }
        this.title = title;
        
        // Validate and set author
        if (author == null || author.length() < 5 || author.length() > 40) {
            throw new IllegalArgumentException("Author must be between 5 and 40 characters");
        }
        this.author = author;
        
        // Validate and set ISBN
        if (isbn == null || isbn.length() != 10 || !isValidIsbn(isbn)) {
            throw new IllegalArgumentException("ISBN must be exactly 10 digits (0-9)");
        }
        this.isbn = isbn;
        
        // Set book type
        this.type = type;
        
        // Validate and set edition
        if (edition < 1) {
            throw new IllegalArgumentException("Edition must be greater than or equal to 1");
        }
        this.edition = edition;
        
        // Validate and set summary
        if (summary == null || summary.length() < 20 || summary.length() > 150) {
            throw new IllegalArgumentException("Summary must be between 20 and 150 characters");
        }
        this.summary = summary;
        
        // Set price
        this.price = price;
    }
    
    // Method to validate ISBN (must be 10 digits)
    private boolean isValidIsbn(String isbn) {
        for (char c : isbn.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
    
    // Getters
    public String getTitle() {
        return title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public BookType getType() {
        return type;
    }
    
    public int getEdition() {
        return edition;
    }
    
    public String getSummary() {
        return summary;
    }
    
    public double getPrice() {
        return price;
    }
    
    // toString method
    @Override
    public String toString() {
        return "Title: " + title + ", Author: " + author + ", ISBN: " + isbn + 
               ", Type: " + type + ", Edition: " + edition + 
               ", Summary: " + summary + ", Price: £" + price;
    }
}