
package model;

import java.util.Objects;

public abstract class SecurityDevice implements NetworkElement {

    private final String id;
    private final String ipAddress;
    private final int vulnerabilities;
    private final int criticality;

    protected SecurityDevice(
            String id,
            String ipAddress,
            int vulnerabilities,
            int criticality) {

        this.id = Objects.requireNonNull(id);
        this.ipAddress = Objects.requireNonNull(ipAddress);

        if (id.isBlank() || ipAddress.isBlank()
                || vulnerabilities < 0
                || criticality < 1 || criticality > 5) {
            throw new IllegalArgumentException(
                    "Invalid device properties"
            );
        }

        this.vulnerabilities = vulnerabilities;
        this.criticality = criticality;
    }

    @Override
    public final String id() {
        return id;
    }

    public final String ipAddress() {
        return ipAddress;
    }

    public final int vulnerabilities() {
        return vulnerabilities;
    }

    public final int criticality() {
        return criticality;
    }
}
