
package processing;

import exception.AirportException;

public class SecurityScreening {
    public void execute(boolean passed) {
        if (!passed) {
            throw new AirportException("Security screening failed");
        }
    }
}
