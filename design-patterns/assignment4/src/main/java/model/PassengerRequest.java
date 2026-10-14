
package model;

public record PassengerRequest(
        String id,
        String name,
        double weight,
        boolean passportValid,
        boolean bookingValid,
        boolean securityPassed,
        boolean customsCleared,
        boolean gateOpen
) {
}
