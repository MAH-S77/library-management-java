package part01;

public interface Lendable {
    boolean checkout();  // to request that a book be marked as 'borrowed'
    boolean checkIn();   // to request that a book be marked as 'returned'
}