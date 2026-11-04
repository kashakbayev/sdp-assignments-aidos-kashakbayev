
package observer.listeners;

import observer.Observer;
import observer.SecurityEvent;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class SecurityAuditLogger
        implements Observer<SecurityEvent> {

    private final ConcurrentLinkedQueue<SecurityEvent> history =
            new ConcurrentLinkedQueue<>();

    @Override
    public void onEvent(SecurityEvent event) {
        history.add(event);
    }

    public List<SecurityEvent> history() {
        return List.copyOf(history);
    }
}
