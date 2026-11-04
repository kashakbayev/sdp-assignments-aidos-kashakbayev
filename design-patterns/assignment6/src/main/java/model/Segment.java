
package model;

import visitor.Visitor;

import java.util.List;
import java.util.Objects;

public record Segment(String id, List<SecurityDevice> devices)
        implements NetworkElement {

    public Segment {
        Objects.requireNonNull(id);
        devices = List.copyOf(devices);

        if (id.isBlank()) {
            throw new IllegalArgumentException("Segment ID is empty");
        }
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitSegment(this);
    }
}
