
package observer.listeners;

import observer.Observer;
import observer.SecurityEvent;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class IncidentAlertService
        implements Observer<SecurityEvent> {

    private final ConcurrentLinkedQueue<SecurityEvent> alerts =
            new ConcurrentLinkedQueue<>();

    @Override
    public void onEvent(SecurityEvent event) {
        if (event.severity() >= 8) {
            alerts.add(event);
        }
    }

    public List<SecurityEvent> alerts() {
        return List.copyOf(alerts);
    }
}
