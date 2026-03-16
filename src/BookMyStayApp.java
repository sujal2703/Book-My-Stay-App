import java.util.*;

/**
 * Use Case 11: Concurrent Booking Simulation (Thread Safety)
 * Demonstrates the use of synchronization to prevent race conditions.
 * * @author sujal2703
 * @version 11.0
 */

// --- Shared Inventory Resource ---
class ThreadSafeInventory {
    private final Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    /**
     * synchronized ensures that if two threads try to book the last room,
     * one will wait until the other finished the check-and-decrement cycle.
     */
    public synchronized boolean bookRoom(String type) {
        int available = inventory.getOrDefault(type, 0);
        if (available > 0) {
            // Simulate processing time to increase the chance of a race condition
            try { Thread.sleep(10); } catch (InterruptedException e) {}

            inventory.put(type, available - 1);
            return true;
        }
        return false;
    }

    public synchronized int getCount(String type) {
        return inventory.getOrDefault(type, 0);
    }
}

// --- Booking Task (Runnable) ---
class BookingTask implements Runnable {
    private String guestName;
    private String roomType;
    private ThreadSafeInventory inventory;

    public BookingTask(String guestName, String roomType, ThreadSafeInventory inventory) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        System.out.println("[Thread " + Thread.currentThread().getId() + "] " +
                guestName + " is attempting to book " + roomType);

        if (inventory.bookRoom(roomType)) {
            System.out.println(" >> SUCCESS: Room allocated to " + guestName);
        } else {
            System.out.println(" >> FAILED: No availability for " + guestName);
        }
    }
}

// --- Main Application ---
public class BookMyStayApp {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("******************************************");
        System.out.println("   Book My Stay App - Concurrent Simulation");
        System.out.println("******************************************\n");

        // 1. Setup Shared Inventory (Only 2 rooms available)
        ThreadSafeInventory sharedInventory = new ThreadSafeInventory();
        sharedInventory.addRoomType("Suite", 2);

        // 2. Create multiple threads (5 guests competing for 2 rooms)
        Thread t1 = new Thread(new BookingTask("Sujal", "Suite", sharedInventory));
        Thread t2 = new Thread(new BookingTask("Amit", "Suite", sharedInventory));
        Thread t3 = new Thread(new BookingTask("John", "Suite", sharedInventory));
        Thread t4 = new Thread(new BookingTask("Jane", "Suite", sharedInventory));
        Thread t5 = new Thread(new BookingTask("Alice", "Suite", sharedInventory));

        // 3. Start all threads simultaneously
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();

        // 4. Wait for all threads to finish
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();

        // 5. Final Consistency Check
        System.out.println("\n------------------------------------------");
        System.out.println("Final Suite Inventory: " + sharedInventory.getCount("Suite"));
        System.out.println("Status: Multi-threaded consistency maintained.");
        System.out.println("******************************************");
    }
}