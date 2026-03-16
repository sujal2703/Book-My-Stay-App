import java.util.*;

/**
 * Use Case 6: Reservation Confirmation & Room Allocation
 * Focuses on unique Room ID assignment and preventing double-booking using Sets.
 * * @author sujal2703
 * @version 6.0
 */

// --- Domain Models ---
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }

    @Override
    public String toString() {
        return "Guest: " + guestName + " | Room: " + roomType;
    }
}

// --- Allocation & Inventory Logic ---
class BookingService {
    private Map<String, Integer> inventory = new HashMap<>();
    // Tracks unique Room IDs already assigned: Map<RoomType, Set<RoomIDs>>
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();
    private int idCounter = 101; // Simple counter to generate unique Room IDs

    public void setupInventory(String type, int count) {
        inventory.put(type, count);
        allocatedRooms.put(type, new HashSet<>());
    }

    public void processBooking(Reservation request) {
        String type = request.getRoomType();
        int available = inventory.getOrDefault(type, 0);

        System.out.println("[Processing] " + request.getGuestName() + " for " + type + "...");

        if (available > 0) {
            // 1. Generate Unique Room ID (e.g., S-101, D-102)
            String roomID = type.charAt(0) + "-" + (idCounter++);

            // 2. Uniqueness Enforcement: Add to Set
            // Set.add() returns false if the ID already exists
            if (allocatedRooms.get(type).add(roomID)) {
                // 3. Decrement Inventory immediately
                inventory.put(type, available - 1);

                System.out.println(" >> SUCCESS: Room " + roomID + " allocated to " + request.getGuestName());
            } else {
                System.out.println(" >> ERROR: Collision detected for Room ID " + roomID);
            }
        } else {
            System.out.println(" >> REJECTED: No " + type + " rooms available.");
        }
    }

    public void displaySummary() {
        System.out.println("\n--- Final Allocation Summary ---");
        allocatedRooms.forEach((type, ids) -> {
            System.out.println(type + " Allocated: " + ids + " | Remaining: " + inventory.get(type));
        });
    }
}

// --- Main Application ---
public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("******************************************");
        System.out.println("   Book My Stay App - Room Allocation     ");
        System.out.println("******************************************\n");

        // 1. Setup Services
        BookingService service = new BookingService();
        service.setupInventory("Single", 2);
        service.setupInventory("Suite", 1);

        // 2. Setup Request Queue (FIFO from UC5)
        Queue<Reservation> requestQueue = new LinkedList<>();
        requestQueue.add(new Reservation("Sujal", "Single"));
        requestQueue.add(new Reservation("Amit", "Suite"));
        requestQueue.add(new Reservation("John", "Single"));
        requestQueue.add(new Reservation("Jane", "Single")); // This should be rejected (only 2 Singles)

        // 3. Dequeue and Process (UC6 Core Logic)
        while (!requestQueue.isEmpty()) {
            Reservation nextRequest = requestQueue.poll();
            service.processBooking(nextRequest);
        }

        // 4. Final Verification
        service.displaySummary();

        System.out.println("\nStatus: All queued requests processed. Consistency maintained.");
        System.out.println("******************************************");
    }
}