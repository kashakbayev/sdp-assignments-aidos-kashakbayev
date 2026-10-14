
package processing;

import exception.AirportException;

public class BaggageHandler {
    public void execute(double weight) {
        if (!Double.isFinite(weight) || weight < 0 || weight > 23) {
            throw new AirportException("Baggage rejected");
        }
    }
}
