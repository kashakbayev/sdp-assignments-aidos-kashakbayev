
package checkin;

import exception.AirportException;

public class Kiosk {
    public void execute(boolean valid) {
        if (!valid) {
            throw new AirportException("Invalid booking");
        }
    }
}
