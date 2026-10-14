
package clearance;

import exception.AirportException;

public class PassportControl {
    public void execute(boolean valid) {
        if (!valid) {
            throw new AirportException("Passport rejected");
        }
    }
}
