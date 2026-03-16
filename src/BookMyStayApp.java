import java.util.HashMap;
import java.util.Map;

/**
 * BookMyStayApp: Introduces Centralized Inventory Management.
 * Replaces scattered variables with a HashMap for better scalability.
 * * @author sujal2703
 * @version 3.0
 */

// --- Domain Model (Existing from UC2) ---
abstract class Room {
    private String roomType;
    private double price;

    public Room(String roomType, double price) {
        this.roomType = roomType;
        this.price = price;
    }

    public String getRoomType() { return roomType; }

    public void displayDetails() {
        System.out.println("Room Type: " + String.format("%-12s", roomType) + " | Price: $" + price);
    }
}

class SingleRoom extends Room { public SingleRoom() { super("Single", 100.0); } }
class DoubleRoom extends Room { public DoubleRoom() { super("Double", 180.0); } }
class SuiteRoom extends Room { public SuiteRoom() { super("Suite", 350.0); } }

// --- New Inventory Management Class ---
class RoomInventory {
    // Centralized storage: Map<RoomTypeName, Count>
    private Map<String, Integer> inventory;

    public RoomInventory() {
        this.inventory = new HashMap<>();
    }

    // Register room types and their initial counts
    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    // Controlled update of availability
    public void updateAvailability(String type, int change) {
        if (inventory.containsKey(type)) {
            int currentCount = inventory.get(type);
            inventory.put(type, currentCount + change);
        }
    }

    // Retrieve availability
    public int getAvailableCount(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void displayInventory() {
        System.out.println("Current Inventory Status (Single Source of Truth):");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(" -> " + entry.getKey() + " Rooms: " + entry.getValue());
        }
    }
}

// --- Main Application ---
public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("Book My Stay App - Version 3.0");
        System.out.println("==========================================");

        // 1. Initialize the Centralized Inventory
        RoomInventory hotelInventory = new RoomInventory();

        // 2. Register Room Types (Initial Setup)
        hotelInventory.addRoomType("Single", 10);
        hotelInventory.addRoomType("Double", 5);
        hotelInventory.addRoomType("Suite", 2);

        // 3. Display Initial State
        hotelInventory.displayInventory();

        // 4. Demonstrate a Controlled Update (e.g., a booking occurs)
        System.out.println("\n[Action] Booking 1 Single Room...");
        hotelInventory.updateAvailability("Single", -1);

        // 5. Display Updated State
        System.out.println("Updated Inventory:");
        System.out.println("Single Rooms left: " + hotelInventory.getAvailableCount("Single"));

        System.out.println("==========================================");
        System.out.println("Inventory Management logic successfully centralized.");
    }
}