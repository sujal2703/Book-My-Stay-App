import java.util.*;

/**
 * Use Case 7: Add-On Service Selection
 * Demonstrates a One-to-Many relationship using Map<String, List<Service>>.
 * * @author sujal2703
 * @version 7.0
 */

// --- Domain Models ---

class Service {
    private String name;
    private double price;

    public Service(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return name + " ($" + price + ")";
    }
}

class Reservation {
    private String reservationId;
    private String guestName;

    public Reservation(String reservationId, String guestName) {
        this.reservationId = reservationId;
        this.guestName = guestName;
    }

    public String getReservationId() { return reservationId; }
    public String getGuestName() { return guestName; }
}

// --- Add-On Service Manager ---

class AddOnManager {
    // Mapping Reservation ID -> List of selected Services
    private Map<String, List<Service>> selectionMap = new HashMap<>();

    public void addServiceToReservation(String resId, Service service) {
        // If the reservation doesn't have a list yet, create one
        selectionMap.computeIfAbsent(resId, k -> new ArrayList<>()).add(service);
        System.out.println("[Add-On] Added " + service.getName() + " to Reservation: " + resId);
    }

    public void displayBill(Reservation res) {
        String id = res.getReservationId();
        List<Service> services = selectionMap.getOrDefault(id, new ArrayList<>());

        System.out.println("\n--- Service Summary for " + res.getGuestName() + " (" + id + ") ---");
        double total = 0;

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
        } else {
            for (Service s : services) {
                System.out.println(" + " + s);
                total += s.getPrice();
            }
        }
        System.out.println("Total Add-On Cost: $" + total);
        System.out.println("------------------------------------------");
    }
}

// --- Main Application ---

public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("******************************************");
        System.out.println("   Book My Stay App - Add-On Services     ");
        System.out.println("******************************************\n");

        // 1. Setup existing reservations (from UC6 logic)
        Reservation res1 = new Reservation("RES-101", "Sujal");
        Reservation res2 = new Reservation("RES-102", "Amit");

        // 2. Define available Add-On Services
        Service breakfast = new Service("Buffet Breakfast", 25.0);
        Service wifi = new Service("Premium WiFi", 10.0);
        Service spa = new Service("Spa Session", 50.0);

        // 3. Initialize Add-On Manager
        AddOnManager manager = new AddOnManager();

        // 4. Guest selects services
        manager.addServiceToReservation(res1.getReservationId(), breakfast);
        manager.addServiceToReservation(res1.getReservationId(), wifi);

        manager.addServiceToReservation(res2.getReservationId(), spa);

        // 5. Display Aggregated Costs
        manager.displayBill(res1);
        manager.displayBill(res2);

        System.out.println("\nStatus: Add-on services processed independently of inventory.");
        System.out.println("******************************************");
    }
}