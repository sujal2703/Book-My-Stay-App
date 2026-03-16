import java.io.*;
import java.util.*;

/**
 * Use Case 12: Data Persistence & System Recovery
 * Demonstrates saving and loading system state using Serialization.
 * * @author sujal2703
 * @version 12.0
 */

// --- Persistent State Wrapper ---
// Must implement Serializable to be saved to a file
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;
    public Map<String, Integer> inventory;
    public List<String> bookingHistory;

    public SystemState(Map<String, Integer> inventory, List<String> bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }
}

// --- Persistence Service ---
class PersistenceService {
    private static final String FILE_NAME = "hotel_state.ser";

    public static void saveState(Map<String, Integer> inv, List<String> history) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            SystemState state = new SystemState(inv, history);
            oos.writeObject(state);
            System.out.println("[Persistence] State saved successfully to " + FILE_NAME);
        } catch (IOException e) {
            System.err.println("[Error] Failed to save state: " + e.getMessage());
        }
    }

    public static SystemState loadState() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("[Persistence] No existing state found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            SystemState state = (SystemState) ois.readObject();
            System.out.println("[Persistence] State recovered successfully from " + FILE_NAME);
            return state;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[Error] Recovery failed: " + e.getMessage());
            return null;
        }
    }
}

// --- Main Application ---
public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("******************************************");
        System.out.println("   Book My Stay App - Persistence & Recovery");
        System.out.println("******************************************\n");

        // 1. Recovery Phase: Try to load existing data
        SystemState recovered = PersistenceService.loadState();

        Map<String, Integer> inventory;
        List<String> history;

        if (recovered != null) {
            inventory = recovered.inventory;
            history = recovered.bookingHistory;
        } else {
            // Initial Setup if no file exists
            inventory = new HashMap<>();
            inventory.put("Single", 10);
            inventory.put("Suite", 5);
            history = new ArrayList<>();
        }

        // 2. Display Current State (Recovered or Fresh)
        System.out.println("Current Inventory: " + inventory);
        System.out.println("History Count: " + history.size());

        // 3. Simulate System Activity
        System.out.println("\n[Action] Processing a new booking for 'Sujal'...");
        if (inventory.get("Single") > 0) {
            inventory.put("Single", inventory.get("Single") - 1);
            history.add("Booking-ID-" + (history.size() + 101) + ": Sujal (Single)");
        }

        // 4. Persistence Phase: Save state before shutdown
        System.out.println("\n[Action] Shutting down. Saving state...");
        PersistenceService.saveState(inventory, history);

        System.out.println("\n------------------------------------------");
        System.out.println("Status: System state persisted. Try running the app again!");
        System.out.println("******************************************");
    }
}