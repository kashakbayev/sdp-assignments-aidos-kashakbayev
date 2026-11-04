
package observer.listeners;

import observer.Observer;
import observer.SecurityEvent;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class SiemDashboard
        implements Observer<SecurityEvent> {

    private final ConcurrentLinkedQueue<SecurityEvent> events =
            new ConcurrentLinkedQueue<>();

    @Override
    public void onEvent(SecurityEvent event) {
        events.add(event);
    }

    public List<SecurityEvent> events() {
        return List.copyOf(events);
    }
}
