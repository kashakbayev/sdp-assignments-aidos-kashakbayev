
package facade;

import checkin.*;
import processing.*;
import clearance.*;
import storage.*;
import model.*;
import exception.AirportException;

public class AirportFacade {
    private final CheckInRepository checkIns;
    private final ProcessingRepository processing;
    private final ClearanceRepository clearances;
    private final WorkflowRepository workflows;

    private final Kiosk kiosk = new Kiosk();
    private final BoardingPassIssuer issuer = new BoardingPassIssuer();
    private final BaggageHandler baggage = new BaggageHandler();
    private final SecurityScreening security = new SecurityScreening();
    private final PassportControl passport = new PassportControl();
    private final CustomsDeclaration customs = new CustomsDeclaration();
    private final GateController gate = new GateController();

    public AirportFacade(Database db) {
        checkIns = new CheckInRepository(db);
        processing = new ProcessingRepository(db);
        clearances = new ClearanceRepository(db);
        workflows = new WorkflowRepository(db);
    }

    public String processPassenger(PassengerRequest request) {
        if (request == null || request.id() == null || request.id().isBlank() ||
                request.name() == null || request.name().isBlank()) {
            throw new AirportException("Missing passenger details");
        }

        if (workflows.get(request.id()) != null) {
            throw new AirportException("Passenger ID already exists");
        }

        workflows.save(request, "PENDING_A");
        return resume(request.id());
    }

    public String resume(String id) {
        WorkflowRecord workflow = workflows.get(id);

        if (workflow == null) {
            throw new AirportException("Unknown passenger");
        }

        PassengerRequest request = workflow.request();
        String stage = workflow.status();

        if (stage.equals("COMPLETED") ||
                stage.equals("COMPENSATED") ||
                stage.equals("FAILED_A")) {
            return stage;
        }

        if (stage.equals("COMPENSATING")) {
            compensate(id);
            return "COMPENSATED";
        }

        try {
            if (stage.equals("PENDING_A")) {
                String saved = checkIns.get(id);

                if (saved == null) {
                    kiosk.execute(request.bookingValid());

                    CheckInRecord record = new CheckInRecord();
                    issuer.execute(record);

                    checkIns.save(id, record.status().name());
                } else if (!saved.equals("ISSUED")) {
                    throw new AirportException("Invalid check-in state");
                }

                workflows.save(request, "PENDING_B");
                stage = "PENDING_B";
            }

            if (stage.equals("PENDING_B")) {
                String saved = processing.get(id);

                if (saved == null) {
                    baggage.execute(request.weight());
                    security.execute(request.securityPassed());

                    ProcessingRecord record = new ProcessingRecord();
                    record.move(ProcessingRecord.Status.CLEARED);

                    processing.save(id, record.status().name());
                } else if (!saved.equals("CLEARED")) {
                    throw new AirportException("Invalid processing state");
                }

                workflows.save(request, "PENDING_C");
                stage = "PENDING_C";
            }

            if (stage.equals("PENDING_C")) {
                String saved = clearances.get(id);

                if (saved == null) {
                    try {
                        passport.execute(request.passportValid());
                        customs.execute(request.customsCleared());
                        gate.execute(request.gateOpen());

                        ClearanceRecord record = new ClearanceRecord();
                        record.move(ClearanceRecord.Status.APPROVED);

                        clearances.save(id, record.status().name());
                    } catch (AirportException e) {
                        ClearanceRecord record = new ClearanceRecord();
                        record.move(ClearanceRecord.Status.REJECTED);

                        clearances.save(id, record.status().name());
                        throw e;
                    }
                } else if (!saved.equals("APPROVED")) {
                    throw new AirportException("Final clearance rejected");
                }

                workflows.save(request, "COMPLETED");
                return "COMPLETED";
            }

            throw new AirportException("Unknown workflow state");

        } catch (AirportException e) {
            if (checkIns.get(id) == null) {
                workflows.save(request, "FAILED_A");
                return "FAILED_A: " + e.getMessage();
            }

            workflows.save(request, "COMPENSATING");
            compensate(id);

            return "COMPENSATED: " + e.getMessage();
        }
    }

    public void compensate(String id) {
        WorkflowRecord workflow = workflows.get(id);

        if (workflow == null) {
            throw new AirportException("Unknown passenger");
        }

        if (workflow.status().equals("COMPENSATED")) {
            return;
        }

        if (workflow.status().equals("COMPLETED") ||
                "APPROVED".equals(clearances.get(id))) {
            throw new AirportException(
                    "Completed boarding cannot be reversed"
            );
        }

        if ("CLEARED".equals(processing.get(id))) {
            ProcessingRecord record = new ProcessingRecord(
                    ProcessingRecord.Status.CLEARED
            );

            record.move(ProcessingRecord.Status.REVOKED);
            processing.save(id, record.status().name());
        }

        if ("ISSUED".equals(checkIns.get(id))) {
            CheckInRecord record = new CheckInRecord(
                    CheckInRecord.Status.ISSUED
            );

            record.move(CheckInRecord.Status.CANCELLED);
            checkIns.save(id, record.status().name());
        }

        workflows.save(workflow.request(), "COMPENSATED");
    }
}
