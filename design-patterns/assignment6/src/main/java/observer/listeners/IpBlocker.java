
package observer.listeners;

import observer.Observer;
import observer.SecurityEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class IpBlocker
        implements Observer<SecurityEvent> {

    private final Set<String> blockedIps =
            ConcurrentHashMap.newKeySet();

    @Override
    public void onEvent(SecurityEvent event) {
        if (event.severity() >= 8) {
            blockedIps.add(event.sourceIp());
        }
    }

    public Set<String> blockedIps() {
        return Set.copyOf(blockedIps);
    }
}
