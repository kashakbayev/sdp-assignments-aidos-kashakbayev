
# Airport Operations

Assignment 4 - Facade Design Pattern

## Technologies

- Java 17
- Maven
- SQLite
- JUnit 5
- JaCoCo

## Subsystem A - Check-in

Kiosk validates the passenger booking.

BoardingPassIssuer issues a boarding pass.

CheckInRecord manages the check-in status.

CheckInRepository persists the status.

## Subsystem B - Processing

BaggageHandler checks baggage weight.

SecurityScreening verifies the security result.

ProcessingRecord manages the processing status.

ProcessingRepository persists the status.

## Subsystem C - Clearance

PassportControl verifies the passport.

CustomsDeclaration checks customs clearance.

GateController verifies gate availability.

ClearanceRecord manages the clearance status.

ClearanceRepository persists the status.

## Facade Pattern

AirportFacade coordinates all three subsystems.

AirportAdminFacade provides administrative operations.

## Successful Workflow

A -> B -> C -> COMPLETED

## Compensation

B fails:
A -> B fails -> Cancel A

C fails:
A -> B -> C fails -> Revoke B -> Cancel A

## Persistence

SQLite stores each subsystem's status and workflow progress.

The application can resume incomplete workflows after restarting.

## Principle of Least Knowledge

Without Facade:

Kiosk kiosk = new Kiosk();
BoardingPassIssuer issuer = new BoardingPassIssuer();
BaggageHandler baggage = new BaggageHandler();
SecurityScreening security = new SecurityScreening();
PassportControl passport = new PassportControl();
CustomsDeclaration customs = new CustomsDeclaration();
GateController gate = new GateController();

The client must coordinate every subsystem and handle failures.

With Facade:

AirportFacade airport = new AirportFacade(db);
airport.processPassenger(request);

The client communicates with one facade.

## Facade vs Adapter

The Facade pattern provides a simplified interface to multiple independent subsystems. In this project, the airport facade coordinates check-in, baggage processing, security screening, and final boarding clearance.

The Adapter pattern is used when two interfaces are incompatible and one needs to be converted into another. Our problem is not interface incompatibility. It is the coordination of several operations, including persistence and failure compensation. Therefore, Facade is the appropriate pattern.

## Running

mvn clean compile
mvn exec:java

## Testing

mvn clean verify

## Coverage

Open target/site/jacoco/index.html

## Reset

Stop the program and delete airport.db.

## Requirements

Java 17 or higher and Maven.
