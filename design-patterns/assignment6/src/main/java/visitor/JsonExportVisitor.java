
package visitor;

import model.Firewall;
import model.IoTDevice;
import model.Network;
import model.SecurityDevice;
import model.Segment;
import model.Server;
import model.Workstation;

import java.util.StringJoiner;

public final class JsonExportVisitor implements Visitor<String> {

    @Override
    public String visitNetwork(Network network) {
        StringJoiner elements = new StringJoiner(",", "[", "]");

        network.segments().forEach(
                segment -> elements.add(segment.accept(this))
        );

        return "{\"type\":\"network\",\"id\":"
                + quote(network.id())
                + ",\"segments\":" + elements + "}";
    }

    @Override
    public String visitSegment(Segment segment) {
        StringJoiner elements = new StringJoiner(",", "[", "]");

        segment.devices().forEach(
                device -> elements.add(device.accept(this))
        );

        return "{\"type\":\"segment\",\"id\":"
                + quote(segment.id())
                + ",\"devices\":" + elements + "}";
    }

    @Override
    public String visitServer(Server server) {
        return deviceJson("server", server);
    }

    @Override
    public String visitWorkstation(Workstation workstation) {
        return deviceJson("workstation", workstation);
    }

    @Override
    public String visitIoTDevice(IoTDevice device) {
        return deviceJson("iot", device);
    }

    @Override
    public String visitFirewall(Firewall firewall) {
        return deviceJson("firewall", firewall);
    }

    private String deviceJson(
            String type, SecurityDevice device) {

        return "{\"type\":" + quote(type)
                + ",\"id\":" + quote(device.id())
                + ",\"ip\":" + quote(device.ipAddress())
                + ",\"vulnerabilities\":"
                + device.vulnerabilities()
                + ",\"criticality\":"
                + device.criticality() + "}";
    }

    private String quote(String value) {
        StringBuilder result = new StringBuilder("\"");

        for (char c : value.toCharArray()) {
            switch (c) {
                case '"' -> result.append("\\\"");
                case '\\' -> result.append("\\\\");
                case '\n' -> result.append("\\n");
                case '\r' -> result.append("\\r");
                case '\t' -> result.append("\\t");
                case '\b' -> result.append("\\b");
                case '\f' -> result.append("\\f");

                default -> {
                    if (c < 0x20) {
                        result.append(
                                String.format("\\u%04x", (int) c)
                        );
                    } else {
                        result.append(c);
                    }
                }
            }
        }

        return result.append('"').toString();
    }
}
