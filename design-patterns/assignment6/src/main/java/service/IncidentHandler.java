
package service;

import model.Network;
import model.Segment;
import observer.EventType;
import observer.Observer;
import observer.SecurityEvent;
import observer.SecurityEventSource;
import visitor.RiskScoreVisitor;
import visitor.VulnerableDeviceVisitor;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class IncidentHandler
        extends SecurityEventSource
        implements Observer<SecurityEvent> {

    private final Network network;

    private final ConcurrentLinkedQueue<RiskReport> reports =
            new ConcurrentLinkedQueue<>();

    public IncidentHandler(Network network) {
        this.network = network;
    }

    @Override
    public void onEvent(SecurityEvent event) {
        if (event.type() != EventType.THREAT_CLUSTER_DETECTED) {
            return;
        }

        Segment target = network.segments()
                .stream()
                .filter(segment ->
                        segment.id().equals(event.segmentId()))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Unknown segment: "
                                        + event.segmentId()
                        ));

        int risk = target.accept(new RiskScoreVisitor());

        List<String> vulnerable = target
                .accept(new VulnerableDeviceVisitor())
                .stream()
                .map(device -> device.id())
                .toList();

        RiskReport report = new RiskReport(
                target.id(), risk, vulnerable
        );

        reports.add(report);

        publish(new SecurityEvent(
                EventType.RISK_REPORT_CREATED,
                target.id(),
                event.sourceIp(),
                "Risk points: " + risk
                        + "; vulnerable devices: " + vulnerable,
                event.severity()
        ));
    }

    public List<RiskReport> reports() {
        return List.copyOf(reports);
    }
}
