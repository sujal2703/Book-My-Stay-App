import java.util.LinkedList;
import java.util.Queue;

/**
 * Use Case 5: Booking Request (First-Come-First-Served)
 * Demonstrates the use of a Queue to manage incoming requests fairly.
 * * @author sujal2703
 * @version 5.0
 */

// --- Domain Model: Reservation ---
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    @Override
    public String toString() {
        return "Guest: " + String.format("%-10s", guestName) + " | Requested: " + roomType;
    }
}

// --- Main Application ---
public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("******************************************");
        System.out.println("   Book My Stay App - Use Case 5          ");
        System.out.println("   (First-Come-First-Served Queue)        ");
        System.out.println("******************************************\n");

        // 1. Initialize the Booking Request Queue (FIFO)
        // LinkedList implements the Queue interface in Java
        Queue<Reservation> bookingQueue = new LinkedList<>();

        // 2. Guest submits booking requests (Intake phase)
        System.out.println("[Action] Receiving incoming booking requests...");

        bookingQueue.add(new Reservation("Sujal", "Suite"));
        bookingQueue.add(new Reservation("Amit", "Single"));
        bookingQueue.add(new Reservation("John", "Double"));
        bookingQueue.add(new Reservation("Jane", "Suite"));

        // 3. Displaying the Queue State
        // The order should be exactly as they were added.
        System.out.println("\nCurrent Booking Request Queue:");
        System.out.println("------------------------------------------");

        if (bookingQueue.isEmpty()) {
            System.out.println("No pending requests.");
        } else {
            // We use a for-each loop to view the queue without removing items (Peek)
            for (Reservation request : bookingQueue) {
                System.out.println(" -> " + request);
            }
        }

        // 4. Verification of FIFO Principle
        System.out.println("------------------------------------------");
        System.out.println("Queue Size: " + bookingQueue.size());
        System.out.println("Next guest to be served: " + bookingQueue.peek());

        System.out.println("\nStatus: Requests queued fairly. No inventory modified.");
        System.out.println("******************************************");
    }
}