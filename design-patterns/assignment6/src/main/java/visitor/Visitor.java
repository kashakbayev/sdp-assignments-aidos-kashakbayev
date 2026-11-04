
package visitor;

import model.Firewall;
import model.IoTDevice;
import model.Network;
import model.Segment;
import model.Server;
import model.Workstation;

public interface Visitor<R> {
    R visitNetwork(Network network);
    R visitSegment(Segment segment);
    R visitServer(Server server);
    R visitWorkstation(Workstation workstation);
    R visitIoTDevice(IoTDevice device);
    R visitFirewall(Firewall firewall);
}
