
package observer;

public final class SecurityLogMonitor
        extends SecurityEventSource {

    public void failedLogin(String segmentId,
                            String sourceIp, int severity) {
        publish(new SecurityEvent(
                EventType.FAILED_LOGIN,
                segmentId,
                sourceIp,
                "Repeated failed login attempts",
                severity
        ));
    }
}
