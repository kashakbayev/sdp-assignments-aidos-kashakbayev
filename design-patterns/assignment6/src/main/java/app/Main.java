
package app;

import model.Firewall;
import model.IoTDevice;
import model.Network;
import model.Segment;
import model.Server;
import model.Workstation;
import observer.EventType;
import observer.IntrusionDetectionSystem;
import observer.SecurityEventSource;
import observer.SecurityLogMonitor;
import observer.ThreatAggregator;
import observer.listeners.IncidentAlertService;
import observer.listeners.IpBlocker;
import observer.listeners.SecurityAuditLogger;
import observer.listeners.SiemDashboard;
import observer.listeners.ThreatScorer;
import service.IncidentHandler;
import visitor.JsonExportVisitor;
import visitor.SecurityStatisticsVisitor;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) throws Exception {
        int threadCount = args.length == 0
                ? 3
                : Integer.parseInt(args[0]);

        if (threadCount < 3) {
            throw new IllegalArgumentException(
                    "Use at least 3 producer threads"
            );
        }

        Segment office = new Segment("office", List.of(
                new Server(
                        "office-server", "10.0.1.10", 2, 5
                ),
                new Workstation(
                        "employee-pc", "10.0.1.20", 1, 3
                ),
                new IoTDevice(
                        "camera", "10.0.1.30", 3, 2
                )
        ));

        Segment dataCenter = new Segment("data-center", List.of(
                new Server(
                        "db-server", "10.0.2.10", 4, 5
                ),
                new Firewall(
                        "main-firewall", "10.0.2.1", 0, 4
                )
        ));

        Network network = new Network(
                "company-network",
                List.of(office, dataCenter)
        );

        IntrusionDetectionSystem ids =
                new IntrusionDetectionSystem();

        SecurityLogMonitor logs =
                new SecurityLogMonitor();

        ThreatAggregator aggregator =
                new ThreatAggregator();

        IncidentHandler incidentHandler =
                new IncidentHandler(network);

        SiemDashboard dashboard = new SiemDashboard();
        ThreatScorer scorer = new ThreatScorer();
        IncidentAlertService alerts = new IncidentAlertService();
        IpBlocker blocker = new IpBlocker();
        SecurityAuditLogger audit = new SecurityAuditLogger();

        registerRawListeners(
                ids,
                EventType.INTRUSION_DETECTED,
                dashboard,
                scorer,
                alerts,
                blocker,
                audit,
                aggregator
        );

        registerRawListeners(
                logs,
                EventType.FAILED_LOGIN,
                dashboard,
                scorer,
                alerts,
                blocker,
                audit,
                aggregator
        );

        aggregator.subscribe(
                EventType.THREAT_CLUSTER_DETECTED,
                incidentHandler
        );

        aggregator.subscribe(
                EventType.THREAT_CLUSTER_DETECTED,
                dashboard
        );

        aggregator.subscribe(
                EventType.THREAT_CLUSTER_DETECTED,
                audit
        );

        incidentHandler.subscribe(
                EventType.RISK_REPORT_CREATED,
                dashboard
        );

        incidentHandler.subscribe(
                EventType.RISK_REPORT_CREATED,
                audit
        );

        ExecutorService executor =
                Executors.newFixedThreadPool(threadCount);

        try {
            List<Future<?>> producers = List.of(
                    executor.submit(() -> {
                        for (int i = 0; i < 4; i++) {
                            ids.detect(
                                    "office",
                                    "192.168.1.50",
                                    9
                            );
                        }
                    }),

                    executor.submit(() -> {
                        for (int i = 0; i < 4; i++) {
                            logs.failedLogin(
                                    "data-center",
                                    "192.168.1.80",
                                    8
                            );
                        }
                    }),

                    executor.submit(() -> {
                        for (int i = 0; i < 4; i++) {
                            ids.detect(
                                    "data-center",
                                    "192.168.1.90",
                                    9
                            );
                        }
                    })
            );

            executor.shutdown();

            if (!executor.awaitTermination(
                    30, TimeUnit.SECONDS)) {
                throw new IllegalStateException(
                        "Producers did not finish"
                );
            }

            for (Future<?> producer : producers) {
                producer.get();
            }

        } finally {
            if (!executor.isTerminated()) {
                executor.shutdownNow();
            }
        }

        System.out.println(
                "Raw events scored: " + scorer.eventCount()
        );

        System.out.println(
                "Total severity: " + scorer.totalSeverity()
        );

        System.out.println(
                "High-severity alerts: " + alerts.alerts().size()
        );

        System.out.println(
                "Blocked IPs (simulated): " + blocker.blockedIps()
        );

        System.out.println(
                "Incident reports: " + incidentHandler.reports()
        );

        System.out.println(
                "Dashboard events: " + dashboard.events().size()
        );

        System.out.println(
                "Audit entries: " + audit.history().size()
        );

        System.out.println(
                "Network statistics: "
                        + network.accept(new SecurityStatisticsVisitor())
        );

        System.out.println(
                "Network JSON: "
                        + network.accept(new JsonExportVisitor())
        );
    }

    private static void registerRawListeners(
            SecurityEventSource source,
            EventType type,
            SiemDashboard dashboard,
            ThreatScorer scorer,
            IncidentAlertService alerts,
            IpBlocker blocker,
            SecurityAuditLogger audit,
            ThreatAggregator aggregator) {

        source.subscribe(type, dashboard);
        source.subscribe(type, scorer);
        source.subscribe(type, alerts);
        source.subscribe(type, blocker);
        source.subscribe(type, audit);
        source.subscribe(type, aggregator);
    }
}
