import java.util.*;

/**
 * Use Case 10: Booking Cancellation & Inventory Rollback
 * Demonstrates state reversal using a Stack and synchronized inventory updates.
 * * @author sujal2703
 * @version 10.0
 */

// --- Domain Model ---
class Reservation {
    private String resId;
    private String roomType;
    private String guestName;

    public Reservation(String resId, String roomType, String guestName) {
        this.resId = resId;
        this.roomType = roomType;
        this.guestName = guestName;
    }

    public String getResId() { return resId; }
    public String getRoomType() { return roomType; }
    public String getGuestName() { return guestName; }
}

// --- Cancellation Service ---
class CancellationService {
    private Map<String, Integer> inventory;
    private Map<String, Reservation> activeBookings;
    private Stack<String> releasedRoomIds; // LIFO Rollback structure

    public CancellationService(Map<String, Integer> inventory, Map<String, Reservation> activeBookings) {
        this.inventory = inventory;
        this.activeBookings = activeBookings;
        this.releasedRoomIds = new Stack<>();
    }

    public void cancelBooking(String resId) {
        System.out.println("[Action] Attempting to cancel Reservation: " + resId);

        // 1. Validation: Ensure reservation exists
        if (!activeBookings.containsKey(resId)) {
            System.err.println(" >> ERROR: Cancellation failed. Reservation ID " + resId + " not found.");
            return;
        }

        // 2. Retrieve reservation details
        Reservation res = activeBookings.remove(resId);
        String type = res.getRoomType();

        // 3. Rollback State: Record released Room ID (Mocking ID reversal)
        String roomId = type.charAt(0) + "-" + resId.split("-")[1];
        releasedRoomIds.push(roomId);

        // 4. Inventory Restoration: Increment count
        inventory.put(type, inventory.get(type) + 1);

        System.out.println(" >> SUCCESS: " + res.getGuestName() + "'s booking cancelled.");
        System.out.println(" >> Room " + roomId + " returned to pool. New " + type + " inventory: " + inventory.get(type));
    }

    public void displayRollbackStatus() {
        System.out.println("\n--- Rollback Stack Status ---");
        System.out.println("Recently Released IDs (Top to Bottom): " + releasedRoomIds);
        System.out.println("------------------------------------------");
    }
}

// --- Main Application ---
public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("******************************************");
        System.out.println("   Book My Stay App - Cancellation        ");
        System.out.println("******************************************\n");

        // 1. Setup Mock System State
        Map<String, Integer> hotelInventory = new HashMap<>();
        hotelInventory.put("Single", 2);
        hotelInventory.put("Suite", 1);

        Map<String, Reservation> activeBookings = new HashMap<>();
        activeBookings.put("RES-101", new Reservation("RES-101", "Single", "Sujal"));
        activeBookings.put("RES-102", new Reservation("RES-102", "Suite", "Amit"));

        // 2. Initialize Cancellation Service
        CancellationService cancelService = new CancellationService(hotelInventory, activeBookings);

        // 3. Perform Cancellations
        cancelService.cancelBooking("RES-101"); // Valid
        cancelService.cancelBooking("RES-999"); // Invalid (Non-existent)
        cancelService.cancelBooking("RES-102"); // Valid

        // 4. Show Inventory and Rollback Status
        cancelService.displayRollbackStatus();

        System.out.println("Final Inventory: " + hotelInventory);
        System.out.println("\nStatus: Rollback operations completed successfully.");
        System.out.println("******************************************");
    }
}
