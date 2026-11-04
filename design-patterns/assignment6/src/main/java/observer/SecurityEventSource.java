
package observer;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class SecurityEventSource
        implements Subject<SecurityEvent> {

    private final ConcurrentMap<EventType,
            CopyOnWriteArrayList<Observer<SecurityEvent>>> listeners =
            new ConcurrentHashMap<>();

    @Override
    public final void subscribe(
            EventType type, Observer<SecurityEvent> observer) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(observer);

        listeners.computeIfAbsent(
                type, ignored -> new CopyOnWriteArrayList<>()
        ).addIfAbsent(observer);
    }

    @Override
    public final void unsubscribe(
            EventType type, Observer<SecurityEvent> observer) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(observer);

        var current = listeners.get(type);
        if (current != null) {
            current.remove(observer);
        }
    }

    protected final void publish(SecurityEvent event) {
        Objects.requireNonNull(event);

        var current = listeners.get(event.type());
        if (current != null) {
            for (var observer : current) {
                observer.onEvent(event);
            }
        }
    }
}
