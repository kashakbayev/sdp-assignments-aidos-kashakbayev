
package clearance;

import exception.AirportException;

public class CustomsDeclaration {
    public void execute(boolean cleared) {
        if (!cleared) {
            throw new AirportException("Customs rejected");
        }
    }
}
