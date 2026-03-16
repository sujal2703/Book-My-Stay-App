import java.util.*;

/**
 * Use Case 9: Error Handling & Validation
 * Demonstrates custom exceptions, fail-fast validation, and state guarding.
 * * @author sujal2703
 * @version 9.0
 */

// --- Custom Exceptions ---

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

class RoomNotAvailableException extends Exception {
    public RoomNotAvailableException(String message) {
        super(message);
    }
}

// --- Validation Service ---

class BookingValidator {
    private static final Set<String> VALID_ROOM_TYPES = Set.of("Single", "Double", "Suite");

    public static void validateRequest(String roomType, int availability)
            throws InvalidBookingException, RoomNotAvailableException {

        // 1. Validate Input (Case Sensitive)
        if (!VALID_ROOM_TYPES.contains(roomType)) {
            throw new InvalidBookingException("Error: Invalid Room Type '" + roomType + "'.");
        }

        // 2. Validate System State (Inventory Check)
        if (availability <= 0) {
            throw new RoomNotAvailableException("Error: No " + roomType + " rooms left in inventory.");
        }
    }
}

// --- Main Application ---

public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("******************************************");
        System.out.println("   Book My Stay App - Error Handling      ");
        System.out.println("******************************************\n");

        // Simple Mock Inventory
        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("Single", 1);
        inventory.put("Suite", 0); // Out of stock

        // List of scenarios to test validation
        String[] testRooms = {"Single", "Penthouse", "Suite", "single"}; // "single" is lowercase (invalid)

        for (String room : testRooms) {
            System.out.println("[Action] Attempting to book: " + room);
            try {
                // Fail-Fast: Validate before doing anything else
                int count = inventory.getOrDefault(room, -1);

                BookingValidator.validateRequest(room, count);

                // If validation passes, proceed (Mocking the allocation)
                inventory.put(room, count - 1);
                System.out.println(" >> SUCCESS: Booking confirmed for " + room);

            } catch (InvalidBookingException | RoomNotAvailableException e) {
                // Graceful Handling: Catch the error and print the message
                System.err.println(" >> VALIDATION FAILED: " + e.getMessage());
            } catch (Exception e) {
                System.err.println(" >> SYSTEM ERROR: " + e.getMessage());
            }
            System.out.println("------------------------------------------");
        }

        System.out.println("\nStatus: Validation logic verified. System remains stable.");
        System.out.println("******************************************");
    }
}