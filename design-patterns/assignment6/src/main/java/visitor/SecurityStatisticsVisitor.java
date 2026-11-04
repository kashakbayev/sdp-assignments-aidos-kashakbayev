
package visitor;

import model.Firewall;
import model.IoTDevice;
import model.Network;
import model.SecurityDevice;
import model.Segment;
import model.Server;
import model.Workstation;

public final class SecurityStatisticsVisitor
        implements Visitor<SecurityStatistics> {

    private int servers;
    private int workstations;
    private int iotDevices;
    private int firewalls;
    private int vulnerabilities;

    @Override
    public SecurityStatistics visitNetwork(Network network) {
        network.segments().forEach(s -> s.accept(this));
        return snapshot();
    }

    @Override
    public SecurityStatistics visitSegment(Segment segment) {
        segment.devices().forEach(d -> d.accept(this));
        return snapshot();
    }

    @Override
    public SecurityStatistics visitServer(Server server) {
        servers++;
        return count(server);
    }

    @Override
    public SecurityStatistics visitWorkstation(
            Workstation workstation) {
        workstations++;
        return count(workstation);
    }

    @Override
    public SecurityStatistics visitIoTDevice(IoTDevice device) {
        iotDevices++;
        return count(device);
    }

    @Override
    public SecurityStatistics visitFirewall(Firewall firewall) {
        firewalls++;
        return count(firewall);
    }

    private SecurityStatistics count(SecurityDevice device) {
        vulnerabilities += device.vulnerabilities();
        return snapshot();
    }

    private SecurityStatistics snapshot() {
        return new SecurityStatistics(
                servers,
                workstations,
                iotDevices,
                firewalls,
                vulnerabilities
        );
    }
}
