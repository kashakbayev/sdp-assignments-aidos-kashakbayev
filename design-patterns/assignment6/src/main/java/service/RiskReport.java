
package service;

import java.util.List;
import java.util.Objects;

public record RiskReport(
        String segmentId,
        int riskPoints,
        List<String> vulnerableDeviceIds
) {
    public RiskReport {
        Objects.requireNonNull(segmentId);
        vulnerableDeviceIds = List.copyOf(vulnerableDeviceIds);
    }
}
