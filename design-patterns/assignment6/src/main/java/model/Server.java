
package model;

import visitor.Visitor;

public final class Server extends SecurityDevice {

    public Server(String id, String ipAddress,
                  int vulnerabilities, int criticality) {
        super(id, ipAddress, vulnerabilities, criticality);
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitServer(this);
    }
}
