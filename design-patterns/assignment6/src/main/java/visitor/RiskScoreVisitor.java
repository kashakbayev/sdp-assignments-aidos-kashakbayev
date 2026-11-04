
package visitor;

import model.Firewall;
import model.IoTDevice;
import model.Network;
import model.SecurityDevice;
import model.Segment;
import model.Server;
import model.Workstation;

public final class RiskScoreVisitor implements Visitor<Integer> {

    @Override
    public Integer visitNetwork(Network network) {
        return network.segments()
                .stream()
                .mapToInt(s -> s.accept(this))
                .sum();
    }

    @Override
    public Integer visitSegment(Segment segment) {
        return segment.devices()
                .stream()
                .mapToInt(d -> d.accept(this))
                .sum();
    }

    @Override
    public Integer visitServer(Server server) {
        return points(server);
    }

    @Override
    public Integer visitWorkstation(Workstation workstation) {
        return points(workstation);
    }

    @Override
    public Integer visitIoTDevice(IoTDevice device) {
        return points(device);
    }

    @Override
    public Integer visitFirewall(Firewall firewall) {
        return points(firewall);
    }

    private int points(SecurityDevice device) {
        return device.criticality() * 5
                + device.vulnerabilities() * 10;
    }
}
