package part01;

public class LibraryBook extends Book implements Lendable {
    // Static variable for next available ID
    private static int nextId = 1;
    
    // Attributes
    private int id;
    private BookStatus status;
    private String image;
    private int loanCount;
    
    // Constructor
    public LibraryBook(String title, String author, String isbn, BookType type, 
                       int edition, String summary, double price, String coverImage) {
        // Call superclass constructor
        super(title, author, isbn, type, edition, summary, price);
        
        // Initialize LibraryBook specific attributes
        this.id = nextId++;
        this.status = BookStatus.AVAILABLE;  // New books are available by default
        this.image = coverImage;
        this.loanCount = 0;
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public BookStatus getStatus() {
        return status;
    }
    
    public void setStatus(BookStatus status) {
        this.status = status;
    }
    
    public String getImage() {
        return image;
    }
    
    public int getLoanCount() {
        return loanCount;
    }
    
    // Implementation of Lendable interface
    @Override
    public boolean checkout() {
        if (status == BookStatus.AVAILABLE) {
            status = BookStatus.ON_LOAN;
            loanCount++;
            return true;
        }
        return false;  // Can't check out if not available
    }
    
    @Override
    public boolean checkIn() {
        if (status == BookStatus.ON_LOAN) {
            status = BookStatus.AVAILABLE;
            return true;
        }
        return false;  // Can't check in if not on loan
    }
    
    // toString method
    @Override
    public String toString() {
        return "ID: " + id + ", " + super.toString() + 
               ", Status: " + status + ", Image: " + image + 
               ", Times borrowed: " + loanCount;
    }
}