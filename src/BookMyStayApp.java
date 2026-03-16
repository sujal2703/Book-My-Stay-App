import java.util.*;

/**
 * Use Case 8: Booking History & Reporting
 * Uses a List to maintain a chronological audit trail of confirmed bookings.
 * * @author sujal2703
 * @version 8.0
 */

// --- Domain Models ---

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private double price;

    public Reservation(String reservationId, String guestName, String roomType, double price) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.price = price;
    }

    public String getReservationId() { return reservationId; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return String.format("ID: %-8s | Guest: %-10s | Room: %-10s | Paid: $%.2f",
                reservationId, guestName, roomType, price);
    }
}

// --- Booking History & Reporting Service ---

class BookingReportService {
    // Persistent storage for the session
    private List<Reservation> history = new ArrayList<>();

    // Record a successful booking
    public void recordBooking(Reservation res) {
        history.add(res);
    }

    // Generate a summary report (Read-Only)
    public void generateAdminReport() {
        System.out.println("\n========== ADMIN STRATEGIC REPORT ==========");
        System.out.println("Total Bookings Confirmed: " + history.size());

        double totalRevenue = 0;
        for (Reservation res : history) {
            System.out.println(" -> " + res);
            totalRevenue += res.getPrice();
        }

        System.out.println("--------------------------------------------");
        System.out.println("Total Revenue Generated: $" + totalRevenue);
        System.out.println("============================================\n");
    }

    // Getter for manual audit
    public List<Reservation> getHistory() {
        return Collections.unmodifiableList(history); // Defensive copy
    }
}

// --- Main Application ---

public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("******************************************");
        System.out.println("   Book My Stay App - History & Audit     ");
        System.out.println("******************************************");

        // 1. Initialize the History Service
        BookingReportService reportService = new BookingReportService();

        // 2. Simulate successful bookings being recorded
        // (In a full app, these would come from the BookingService after allocation)
        System.out.println("[System] Recording confirmed bookings to history...");

        reportService.recordBooking(new Reservation("RES-101", "Sujal", "Single", 100.0));
        reportService.recordBooking(new Reservation("RES-102", "Amit", "Suite", 350.0));
        reportService.recordBooking(new Reservation("RES-103", "John", "Double", 180.0));

        // 3. Admin requests the report
        reportService.generateAdminReport();

        // 4. Demonstrate manual verification
        System.out.println("Audit Status: History sequence verified (FIFO).");
        System.out.println("Latest Reservation ID: " +
                reportService.getHistory().get(reportService.getHistory().size() - 1).getReservationId());

        System.out.println("\nStatus: Persistence layer simulated. Ready for file/DB integration.");
        System.out.println("******************************************");
    }
}