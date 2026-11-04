
package service;

import model.Firewall;
import model.Network;
import model.Segment;
import model.Server;
import observer.EventType;
import observer.IntrusionDetectionSystem;
import observer.SecurityLogMonitor;
import observer.ThreatAggregator;
import observer.listeners.SiemDashboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationTest {

    private IntrusionDetectionSystem ids;
    private SecurityLogMonitor logs;
    private IncidentHandler handler;
    private SiemDashboard dashboard;

    @BeforeEach
    void setup() {
        Segment office = new Segment("office", List.of(
                new Server("web", "10.0.1.1", 2, 5)
        ));

        Segment dc = new Segment("dc", List.of(
                new Server("db", "10.0.2.1", 4, 5),
                new Firewall("fw", "10.0.2.2", 0, 4)
        ));

        Network network = new Network(
                "all", List.of(office, dc)
        );

        ids = new IntrusionDetectionSystem();
        logs = new SecurityLogMonitor();

        ThreatAggregator aggregator = new ThreatAggregator();
        handler = new IncidentHandler(network);
        dashboard = new SiemDashboard();

        ids.subscribe(
                EventType.INTRUSION_DETECTED,
                aggregator
        );

        logs.subscribe(
                EventType.FAILED_LOGIN,
                aggregator
        );

        aggregator.subscribe(
                EventType.THREAT_CLUSTER_DETECTED,
                handler
        );

        handler.subscribe(
                EventType.RISK_REPORT_CREATED,
                dashboard
        );
    }

    @Test
    void eventTriggersVisitorOnlyOnMatchingSegment() {
        ids.detect("dc", "1.1.1.1", 9);
        logs.failedLogin("dc", "1.1.1.1", 9);

        assertEquals(1, handler.reports().size());

        RiskReport report = handler.reports().get(0);

        assertEquals("dc", report.segmentId());
        assertEquals(85, report.riskPoints());
        assertEquals(
                List.of("db"),
                report.vulnerableDeviceIds()
        );
    }

    @Test
    void resultReturnsAsObserverEvent() {
        ids.detect("dc", "1.1.1.1", 8);
        ids.detect("dc", "1.1.1.1", 9);

        assertEquals(1, dashboard.events().size());

        assertEquals(
                EventType.RISK_REPORT_CREATED,
                dashboard.events().get(0).type()
        );

        assertTrue(
                dashboard.events().get(0)
                        .message()
                        .contains("Risk points: 85")
        );
    }

    @Test
    void noReportUntilClusterDetected() {
        ids.detect("office", "1.1.1.1", 9);

        assertTrue(handler.reports().isEmpty());
        assertTrue(dashboard.events().isEmpty());
    }

    @Test
    void reportsAreSeparatedBySegment() {
        ids.detect("office", "1.1.1.1", 9);
        ids.detect("office", "1.1.1.1", 9);

        logs.failedLogin("dc", "2.2.2.2", 8);
        logs.failedLogin("dc", "2.2.2.2", 8);

        assertEquals(2, handler.reports().size());

        assertEquals(
                45,
                handler.reports().get(0).riskPoints()
        );

        assertEquals(
                85,
                handler.reports().get(1).riskPoints()
        );
    }
}
