
package observer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class ThreatAggregator
        extends SecurityEventSource
        implements Observer<SecurityEvent> {

    private final ConcurrentMap<String, AtomicInteger>
            highSeverityBySegment = new ConcurrentHashMap<>();

    @Override
    public void onEvent(SecurityEvent event) {
        if ((event.type() != EventType.INTRUSION_DETECTED
                && event.type() != EventType.FAILED_LOGIN)
                || event.severity() < 8) {
            return;
        }

        int count = highSeverityBySegment
                .computeIfAbsent(
                        event.segmentId(),
                        ignored -> new AtomicInteger()
                )
                .incrementAndGet();

        if (count % 2 == 0) {
            publish(new SecurityEvent(
                    EventType.THREAT_CLUSTER_DETECTED,
                    event.segmentId(),
                    event.sourceIp(),
                    "Two more high-severity events in "
                            + event.segmentId(),
                    event.severity()
            ));
        }
    }

    public int highSeverityCount(String segmentId) {
        var count = highSeverityBySegment.get(segmentId);
        return count == null ? 0 : count.get();
    }
}
