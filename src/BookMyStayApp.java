import java.util.HashMap;
import java.util.Map;

/**
 * UseCase4RoomSearch: Implements a read-only search service.
 * Filters out rooms with zero availability and prevents state mutation.
 * * @author sujal2703
 * @version 4.0
 */

// --- Domain Model ---
abstract class Room {
    private String roomType;
    private double price;

    public Room(String roomType, double price) {
        this.roomType = roomType;
        this.price = price;
    }

    public String getRoomType() { return roomType; }
    public double getPrice() { return price; }
}

class SingleRoom extends Room { public SingleRoom() { super("Single", 100.0); } }
class DoubleRoom extends Room { public DoubleRoom() { super("Double", 180.0); } }
class SuiteRoom extends Room { public SuiteRoom() { super("Suite", 350.0); } }

// --- Inventory (State Holder) ---
class RoomInventory {
    private Map<String, Integer> inventory = new HashMap<>();

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public int getAvailableCount(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public Map<String, Integer> getAllInventory() {
        return new HashMap<>(inventory); // Return copy to protect original state
    }
}

// --- Search Service (Read-Only Logic) ---
class SearchService {
    private RoomInventory inventory;
    private Map<String, Room> roomDetails;

    public SearchService(RoomInventory inventory) {
        this.inventory = inventory;
        this.roomDetails = new HashMap<>();
        // Pre-populate room details for display
        roomDetails.put("Single", new SingleRoom());
        roomDetails.put("Double", new DoubleRoom());
        roomDetails.put("Suite", new SuiteRoom());
    }

    public void searchAvailableRooms() {
        System.out.println("Searching for available rooms...");
        System.out.println("------------------------------------------");
        boolean found = false;

        for (Map.Entry<String, Integer> entry : inventory.getAllInventory().entrySet()) {
            String type = entry.getKey();
            int count = entry.getValue();

            // Validation Logic: Only show rooms with availability > 0
            if (count > 0) {
                Room details = roomDetails.get(type);
                System.out.println("Room Type: " + String.format("%-10s", type) +
                        " | Price: $" + String.format("%-6s", details.getPrice()) +
                        " | Available: " + count);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No rooms currently available.");
        }
        System.out.println("------------------------------------------");
    }
}

// --- Main Application ---
public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("Book My Stay App - Version 4.0 (Search Service)");
        System.out.println("==========================================");

        // 1. Setup Inventory
        RoomInventory hotelInventory = new RoomInventory();
        hotelInventory.addRoomType("Single", 5);
        hotelInventory.addRoomType("Double", 0); // Out of stock
        hotelInventory.addRoomType("Suite", 2);

        // 2. Initialize Search Service
        SearchService searchService = new SearchService(hotelInventory);

        // 3. Perform Search
        // Notice: Double rooms should NOT appear in the output.
        searchService.searchAvailableRooms();

        System.out.println("Search completed successfully.");
        System.out.println("Note: Inventory state remained unchanged.");
        System.out.println("==========================================");
    }
}