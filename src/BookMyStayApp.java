/**
 * UseCase2RoomInitialization: Implementation of Room hierarchy and inventory.
 * Focuses on Object Modeling and Static Availability variables.
 * * @author sujal2703
 * @version 2.0
 */

// --- Abstract Base Class ---
abstract class Room {
    private String roomType;
    private int beds;
    private double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + roomType + " | Beds: " + beds + " | Price: $" + price);
    }
}

// --- Concrete Subclasses ---
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 100.0);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 180.0);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 4, 350.0);
    }
}

// --- Main Application Class ---
public class BookMyStayApp {

    public static void main(String[] args) {
        // Welcome Message (from Use Case 1 logic)
        System.out.println("Welcome to Book My Stay App v2.0");
        System.out.println("------------------------------------------");

        // 1. Initialize Room Objects (Polymorphism)
        Room single = new SingleRoom();
        Room dbl = new DoubleRoom();
        Room suite = new SuiteRoom();

        // 2. Static Availability Representation (Individual Variables)
        int singleAvailability = 10;
        int doubleAvailability = 5;
        int suiteAvailability = 2;

        // 3. Display Details and Availability
        single.displayDetails();
        System.out.println("Availability: " + singleAvailability);
        System.out.println();

        dbl.displayDetails();
        System.out.println("Availability: " + doubleAvailability);
        System.out.println();

        suite.displayDetails();
        System.out.println("Availability: " + suiteAvailability);

        System.out.println("------------------------------------------");
        System.out.println("System execution finished.");
    }
}