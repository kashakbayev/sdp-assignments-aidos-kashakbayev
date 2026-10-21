# Assignment 5 — Airport Operations Strategy Pattern

## Overview

This Java application demonstrates the **Strategy design pattern** through two independent airport operations: assigning runways to flights and routing baggage to conveyor belts. Each operation supports multiple algorithms that can be selected automatically or changed during program execution.

The project uses **Java, Maven, and JUnit 5**. It belongs to the `assignment5` Maven module within the `design-patterns` repository.

## Project Structure

```text
assignment5/
├── pom.xml
└── src/
    ├── main/java/
    │   ├── Main.java
    │   ├── runway/
    │   │   ├── Flight.java
    │   │   ├── Runway.java
    │   │   ├── RunwayStrategy.java
    │   │   ├── LeastBusyRunwayStrategy.java
    │   │   ├── LongestRunwayStrategy.java
    │   │   ├── WindAlignedRunwayStrategy.java
    │   │   ├── RunwayContext.java
    │   │   ├── RunwaySelector.java
    │   │   ├── WindDataSource.java
    │   │   └── SimulatedWindDataSource.java
    │   └── baggage/
    │       ├── Baggage.java
    │       ├── BaggageBelt.java
    │       ├── BaggageStrategy.java
    │       ├── LeastLoadedBaggageStrategy.java
    │       ├── FastestBaggageStrategy.java
    │       ├── ZoneMatchedBaggageStrategy.java
    │       ├── BaggageContext.java
    │       └── BaggageSelector.java
    └── test/java/
        ├── runway/
        │   ├── runway.RunwayStrategyTest.java
        │   ├── runway.RunwaySelectorTest.java
        │   └── runway.FakeWindDataSource.java
        └── baggage/
            ├── baggage.BaggageStrategyTest.java
            └── baggage.BaggageSelectorTest.java
```

## Strategy Families

### Runway Assignment

All runway algorithms select a runway that meets the flight's minimum required runway length.

| Strategy | Selection logic |
|---|---|
| `LeastBusyRunwayStrategy` | Selects the suitable runway with the fewest waiting aircraft. |
| `LongestRunwayStrategy` | Selects the suitable runway with the greatest length. |
| `WindAlignedRunwayStrategy` | Selects the suitable runway most closely aligned with the current wind direction. |

`RunwaySelector` chooses the wind-aligned strategy when wind speed is at least 20 knots, the least-busy strategy when at least 8 aircraft are waiting, and the longest-runway strategy otherwise. Wind speed takes priority over congestion.

### Baggage Routing

| Strategy | Selection logic |
|---|---|
| `LeastLoadedBaggageStrategy` | Selects the belt with the fewest queued bags. |
| `FastestBaggageStrategy` | Minimizes estimated processing time using `(queuedBags + 1) / bagsPerMinute`. |
| `ZoneMatchedBaggageStrategy` | Selects the least-loaded belt in the baggage destination zone. |

`BaggageSelector` chooses the fastest strategy for transfer baggage departing within 45 minutes, the least-loaded strategy during high traffic, and the zone-matched strategy otherwise. Urgent transfers take priority over high traffic.

## Class Responsibilities

| Class | Responsibility |
|---|---|
| `Main` | Demonstrates algorithm comparison, automatic selection, manual selection, runtime switching. |
| `Flight` | Stores flight identification plus minimum runway length. |
| `Runway` | Stores runway dimensions, heading, queue size. |
| `RunwayStrategy` | Defines the runway selection contract. |
| `LeastBusyRunwayStrategy` | Selects the suitable runway with the smallest aircraft queue. |
| `LongestRunwayStrategy` | Selects the suitable runway with the greatest length. |
| `WindAlignedRunwayStrategy` | Selects a suitable runway aligned with measured wind direction. |
| `RunwayContext` | Delegates runway assignment to the currently configured strategy. |
| `RunwaySelector` | Selects a runway algorithm using wind speed plus traffic volume. |
| `WindDataSource` | Defines access to wind direction plus wind speed measurements. |
| `SimulatedWindDataSource` | Supplies predefined wind measurements. |
| `Baggage` | Stores baggage identification, destination zone, transfer status, departure timing. |
| `BaggageBelt` | Stores belt identification, destination zone, queue size, processing speed. |
| `BaggageStrategy` | Defines the baggage belt selection contract. |
| `LeastLoadedBaggageStrategy` | Selects the baggage belt with the smallest queue. |
| `FastestBaggageStrategy` | Selects the belt with the lowest estimated processing time. |
| `ZoneMatchedBaggageStrategy` | Selects a baggage belt serving the requested destination zone. |
| `BaggageContext` | Delegates baggage routing to the currently configured strategy. |
| `BaggageSelector` | Selects a baggage algorithm using urgency plus traffic conditions. |
| `RunwayStrategyTest` | Verifies runway algorithms, runtime switching, shared safety checks. |
| `RunwaySelectorTest` | Verifies each automatic runway selection rule. |
| `FakeWindDataSource` | Supplies fixed wind measurements during unit tests. |
| `BaggageStrategyTest` | Verifies baggage algorithms, runtime switching, shared validity checks. |
| `BaggageSelectorTest` | Verifies each automatic baggage selection rule. |

## Isolating External Data

`WindAlignedRunwayStrategy` depends on the `WindDataSource` interface rather than a concrete weather provider. `SimulatedWindDataSource` supplies sample measurements during the demo. Unit tests use `FakeWindDataSource` to supply predictable values. This isolates the selection algorithm from changes to the data source.

## Why Strategy Instead of State or Template Method?

The **Strategy pattern** is appropriate because the system must perform the same operation using different interchangeable algorithms. For runway assignment, the system can optimize for queue length, runway length, or wind alignment. For baggage routing, it can optimize for queue length, estimated processing time, or destination zone. These choices are separate classes implementing a common interface. The corresponding context simply delegates the operation to its current strategy. The program can replace that strategy through `setStrategy()` without modifying the context, supporting the Open/Closed Principle.

The **State pattern** also uses composition and delegation, but its purpose is to model changes in an object's behavior as its internal state changes. Our strategies are selected because of operational requirements, not because a context progresses through a sequence of states. For example, assigning a wind-aligned runway does not transition the runway context into a new lifecycle stage.

The **Template Method pattern** defines an algorithm's overall sequence in a base class while subclasses override selected steps. That inheritance-based structure does not fit this project because each selection algorithm is independent. Least-busy selection, longest-runway selection, and wind alignment use different decision criteria rather than variations of one fixed sequence. Strategy keeps those algorithms separate, makes them easy to test, and permits runtime replacement through composition.

## Testing

JUnit 5 covers every concrete strategy, all selector rules, context switching, fixed-value weather data, and shared behavior through `@ParameterizedTest`.

From the `assignment5` directory:

```bash
mvn clean test
```

From the parent `design-patterns` directory:

```bash
mvn -pl assignment5 -am test
```

## Running the Demo

From the `assignment5` directory:

```bash
mvn compile
java -cp target/classes Main
```

The demo processes the same flight through all three runway strategies, then processes the same baggage item through all three baggage strategies. It also demonstrates manual selection, automatic selection, and changing strategies at runtime.
