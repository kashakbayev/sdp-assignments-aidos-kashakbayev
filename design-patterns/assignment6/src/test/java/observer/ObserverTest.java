
package observer;

import observer.listeners.IncidentAlertService;
import observer.listeners.IpBlocker;
import observer.listeners.SecurityAuditLogger;
import observer.listeners.SiemDashboard;
import observer.listeners.ThreatScorer;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class ObserverTest {

    @Test
    void deliversOnlySubscribedEventType() {
        IntrusionDetectionSystem source =
                new IntrusionDetectionSystem();

        AtomicInteger count = new AtomicInteger();

        source.subscribe(
                EventType.FAILED_LOGIN,
                event -> count.incrementAndGet()
        );

        source.detect("office", "1.1.1.1", 9);

        assertEquals(0, count.get());
    }

    @Test
    void listenerUnsubscribesInsideCallback() {
        IntrusionDetectionSystem source =
                new IntrusionDetectionSystem();

        AtomicInteger count = new AtomicInteger();

        AtomicReference<Observer<SecurityEvent>> reference =
                new AtomicReference<>();

        Observer<SecurityEvent> listener = event -> {
            count.incrementAndGet();
            source.unsubscribe(
                    EventType.INTRUSION_DETECTED,
                    reference.get()
            );
        };

        reference.set(listener);

        source.subscribe(
                EventType.INTRUSION_DETECTED, listener
        );

        source.detect("office", "1.1.1.1", 9);
        source.detect("office", "1.1.1.1", 9);

        assertEquals(1, count.get());
    }

    @Test
    void listenerCanSubscribeAnotherListenerInsideCallback() {
        IntrusionDetectionSystem source =
                new IntrusionDetectionSystem();

        AtomicInteger count = new AtomicInteger();

        Observer<SecurityEvent> late =
                event -> count.incrementAndGet();

        source.subscribe(
                EventType.INTRUSION_DETECTED,
                event -> source.subscribe(
                        EventType.INTRUSION_DETECTED,
                        late
                )
        );

        source.detect("office", "1.1.1.1", 9);
        assertEquals(0, count.get());

        source.detect("office", "1.1.1.1", 9);
        assertEquals(1, count.get());
    }

    @Test
    void duplicateSubscriptionIsIgnored() {
        IntrusionDetectionSystem source =
                new IntrusionDetectionSystem();

        AtomicInteger count = new AtomicInteger();

        Observer<SecurityEvent> listener =
                event -> count.incrementAndGet();

        source.subscribe(
                EventType.INTRUSION_DETECTED,
                listener
        );

        source.subscribe(
                EventType.INTRUSION_DETECTED,
                listener
        );

        source.detect("office", "1.1.1.1", 8);

        assertEquals(1, count.get());
    }

    @Test
    void twoSourcesHaveSeparateSubscriberRegistries() {
        IntrusionDetectionSystem ids =
                new IntrusionDetectionSystem();

        SecurityLogMonitor logs =
                new SecurityLogMonitor();

        SiemDashboard dashboard = new SiemDashboard();

        ids.subscribe(
                EventType.INTRUSION_DETECTED,
                dashboard
        );

        ids.detect("office", "1.1.1.1", 8);
        logs.failedLogin("office", "2.2.2.2", 8);

        assertEquals(1, dashboard.events().size());
    }

    @Test
    void aggregatorPublishesAfterTwoHighSeverityEvents() {
        IntrusionDetectionSystem ids =
                new IntrusionDetectionSystem();

        SecurityLogMonitor logs =
                new SecurityLogMonitor();

        ThreatAggregator aggregator = new ThreatAggregator();

        AtomicInteger clusters = new AtomicInteger();

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
                event -> clusters.incrementAndGet()
        );

        ids.detect("office", "1.1.1.1", 9);
        logs.failedLogin("office", "2.2.2.2", 8);

        assertEquals(1, clusters.get());
        assertEquals(2, aggregator.highSeverityCount("office"));
    }

    @Test
    void aggregatorIgnoresLowSeverityEvents() {
        IntrusionDetectionSystem ids =
                new IntrusionDetectionSystem();

        ThreatAggregator aggregator = new ThreatAggregator();

        ids.subscribe(
                EventType.INTRUSION_DETECTED,
                aggregator
        );

        ids.detect("office", "1.1.1.1", 7);

        assertEquals(0, aggregator.highSeverityCount("office"));
    }

    @Test
    void ipBlockerOnlyBlocksHighSeverityIps() {
        IntrusionDetectionSystem ids =
                new IntrusionDetectionSystem();

        IpBlocker blocker = new IpBlocker();

        ids.subscribe(
                EventType.INTRUSION_DETECTED,
                blocker
        );

        ids.detect("office", "1.1.1.1", 6);
        ids.detect("office", "2.2.2.2", 9);

        assertEquals(
                java.util.Set.of("2.2.2.2"),
                blocker.blockedIps()
        );
    }

    @Test
    void scoringAndAuditAndAlertsWork() {
        SecurityLogMonitor logs = new SecurityLogMonitor();

        ThreatScorer scorer = new ThreatScorer();
        SecurityAuditLogger audit = new SecurityAuditLogger();
        IncidentAlertService alerts = new IncidentAlertService();

        logs.subscribe(EventType.FAILED_LOGIN, scorer);
        logs.subscribe(EventType.FAILED_LOGIN, audit);
        logs.subscribe(EventType.FAILED_LOGIN, alerts);

        logs.failedLogin("office", "1.1.1.1", 5);
        logs.failedLogin("office", "1.1.1.1", 8);

        assertEquals(2, scorer.eventCount());
        assertEquals(13, scorer.totalSeverity());
        assertEquals(2, audit.history().size());
        assertEquals(1, alerts.alerts().size());
    }

    @Test
    void stressTestFourThreadsOneThousandEventsEach()
            throws InterruptedException {

        IntrusionDetectionSystem ids =
                new IntrusionDetectionSystem();

        AtomicInteger received = new AtomicInteger();

        ids.subscribe(
                EventType.INTRUSION_DETECTED,
                event -> received.incrementAndGet()
        );

        ExecutorService pool =
                Executors.newFixedThreadPool(4);

        try {
            for (int producer = 0; producer < 4; producer++) {
                pool.submit(() -> {
                    for (int i = 0; i < 1000; i++) {
                        ids.detect(
                                "office", "1.1.1.1", 5
                        );
                    }
                });
            }

            pool.shutdown();

            assertTrue(
                    pool.awaitTermination(
                            10, TimeUnit.SECONDS
                    )
            );

            assertEquals(4000, received.get());

        } finally {
            pool.shutdownNow();
        }
    }
}
