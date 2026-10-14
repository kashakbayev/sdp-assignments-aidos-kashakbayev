
package clearance;

import exception.AirportException;

public class GateController {
    public void execute(boolean open) {
        if (!open) {
            throw new AirportException("Gate closed");
        }
    }
}
