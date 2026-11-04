
package observer;

import java.time.Instant;
import java.util.Objects;

public record SecurityEvent(
        EventType type,
        String segmentId,
        String sourceIp,
        String message,
        int severity,
        Instant occurredAt
) {
    public SecurityEvent {
        Objects.requireNonNull(type);
        Objects.requireNonNull(segmentId);
        Objects.requireNonNull(sourceIp);
        Objects.requireNonNull(message);
        Objects.requireNonNull(occurredAt);

        if (segmentId.isBlank() || sourceIp.isBlank()
                || severity < 0 || severity > 10) {
            throw new IllegalArgumentException("Invalid security event");
        }
    }

    public SecurityEvent(EventType type, String segmentId,
                         String sourceIp, String message,
                         int severity) {
        this(type, segmentId, sourceIp,
                message, severity, Instant.now());
    }
}
