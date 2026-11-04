
package observer.listeners;

import observer.Observer;
import observer.SecurityEvent;

import java.util.concurrent.atomic.LongAdder;

public final class ThreatScorer
        implements Observer<SecurityEvent> {

    private final LongAdder totalSeverity = new LongAdder();
    private final LongAdder eventCount = new LongAdder();

    @Override
    public void onEvent(SecurityEvent event) {
        totalSeverity.add(event.severity());
        eventCount.increment();
    }

    public long totalSeverity() {
        return totalSeverity.sum();
    }

    public long eventCount() {
        return eventCount.sum();
    }
}
