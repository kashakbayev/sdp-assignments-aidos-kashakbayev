
package model;

import visitor.Visitor;

public interface NetworkElement {
    String id();
    <R> R accept(Visitor<R> visitor);
}
