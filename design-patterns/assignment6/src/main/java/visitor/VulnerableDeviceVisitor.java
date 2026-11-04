
package visitor;

import model.Firewall;
import model.IoTDevice;
import model.Network;
import model.SecurityDevice;
import model.Segment;
import model.Server;
import model.Workstation;

import java.util.ArrayList;
import java.util.List;

public final class VulnerableDeviceVisitor
        implements Visitor<List<SecurityDevice>> {

    @Override
    public List<SecurityDevice> visitNetwork(Network network) {
        List<SecurityDevice> result = new ArrayList<>();

        network.segments().forEach(
                s -> result.addAll(s.accept(this))
        );

        return List.copyOf(result);
    }

    @Override
    public List<SecurityDevice> visitSegment(Segment segment) {
        List<SecurityDevice> result = new ArrayList<>();

        segment.devices().forEach(
                d -> result.addAll(d.accept(this))
        );

        return List.copyOf(result);
    }

    @Override
    public List<SecurityDevice> visitServer(Server server) {
        return matching(server);
    }

    @Override
    public List<SecurityDevice> visitWorkstation(
            Workstation workstation) {
        return matching(workstation);
    }

    @Override
    public List<SecurityDevice> visitIoTDevice(IoTDevice device) {
        return matching(device);
    }

    @Override
    public List<SecurityDevice> visitFirewall(Firewall firewall) {
        return matching(firewall);
    }

    private List<SecurityDevice> matching(
            SecurityDevice device) {
        return device.vulnerabilities() >= 2
                ? List.of(device)
                : List.of();
    }
}
