
package model;

import visitor.Visitor;

import java.util.List;
import java.util.Objects;

public record Network(String id, List<Segment> segments)
        implements NetworkElement {

    public Network {
        Objects.requireNonNull(id);
        segments = List.copyOf(segments);

        if (id.isBlank()) {
            throw new IllegalArgumentException("Network ID is empty");
        }
    }

    @Override
    public <R> R accept(Visitor<R> visitor) {
        return visitor.visitNetwork(this);
    }
}
