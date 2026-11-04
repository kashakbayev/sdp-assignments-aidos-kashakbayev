
package visitor;

public record SecurityStatistics(
        int servers,
        int workstations,
        int iotDevices,
        int firewalls,
        int totalVulnerabilities
) {
    public int totalDevices() {
        return servers + workstations + iotDevices + firewalls;
    }
}
