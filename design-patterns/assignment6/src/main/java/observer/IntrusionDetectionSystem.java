
package observer;

public final class IntrusionDetectionSystem
        extends SecurityEventSource {

    public void detect(String segmentId,
                       String sourceIp, int severity) {
        publish(new SecurityEvent(
                EventType.INTRUSION_DETECTED,
                segmentId,
                sourceIp,
                "Suspicious network traffic",
                severity
        ));
    }
}
