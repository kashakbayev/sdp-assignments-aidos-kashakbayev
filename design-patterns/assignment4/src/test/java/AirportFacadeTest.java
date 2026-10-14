
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.*;
import facade.*;
import checkin.*;
import processing.*;
import clearance.*;
import storage.*;
import model.*;
import exception.AirportException;

public class AirportFacadeTest {
    private Path path;
    private Database db;

    @BeforeEach
    void setup() throws Exception {
        path = Files.createTempFile("airport-test-", ".db");
        db = new Database(path.toString());
    }

    @AfterEach
    void cleanup() throws Exception {
        db.close();
        Files.deleteIfExists(path);
    }

    private PassengerRequest passenger(
            String id,
            double weight,
            boolean passportValid
    ) {
        return new PassengerRequest(
                id, "Alex", weight, passportValid,
                true, true, true, true
        );
    }

    @Test
    void completesBoarding() {
        String result = new AirportFacade(db)
                .processPassenger(passenger("1", 18, true));

        assertEquals("COMPLETED", result);
        assertEquals("ISSUED", new CheckInRepository(db).get("1"));
        assertEquals("CLEARED", new ProcessingRepository(db).get("1"));
        assertEquals("APPROVED", new ClearanceRepository(db).get("1"));
    }

    @Test
    void baggageFailureCompensatesA() {
        String result = new AirportFacade(db)
                .processPassenger(passenger("2", 30, true));

        assertTrue(result.startsWith("COMPENSATED"));
        assertEquals("CANCELLED", new CheckInRepository(db).get("2"));
        assertNull(new ProcessingRepository(db).get("2"));

        assertEquals(
                "COMPENSATED",
                new WorkflowRepository(db).get("2").status()
        );
    }

    @Test
    void passportFailureCompensatesBThenA() {
        String result = new AirportFacade(db)
                .processPassenger(passenger("3", 18, false));

        assertTrue(result.startsWith("COMPENSATED"));
        assertEquals("REVOKED", new ProcessingRepository(db).get("3"));
        assertEquals("CANCELLED", new CheckInRepository(db).get("3"));
        assertEquals("REJECTED", new ClearanceRepository(db).get("3"));
    }

    @Test
    void continuesFromPersistedBStageAfterRestart() {
        PassengerRequest request = passenger("4", 18, true);

        new CheckInRepository(db).save("4", "ISSUED");
        new WorkflowRepository(db).save(request, "PENDING_B");

        db.close();
        db = new Database(path.toString());

        assertEquals("COMPLETED", new AirportFacade(db).resume("4"));
        assertEquals("CLEARED", new ProcessingRepository(db).get("4"));
    }

    @Test
    void continuesFromPersistedCStageAfterRestart() {
        PassengerRequest request = passenger("5", 18, true);

        new CheckInRepository(db).save("5", "ISSUED");
        new ProcessingRepository(db).save("5", "CLEARED");
        new WorkflowRepository(db).save(request, "PENDING_C");

        db.close();
        db = new Database(path.toString());

        assertEquals("COMPLETED", new AirportFacade(db).resume("5"));
        assertEquals("APPROVED", new ClearanceRepository(db).get("5"));
    }

    @Test
    void rejectsBookingBeforeCommittingA() {
        PassengerRequest request = new PassengerRequest(
                "6", "Alex", 18, true,
                false, true, true, true
        );

        String result = new AirportFacade(db).processPassenger(request);

        assertTrue(result.startsWith("FAILED_A"));
        assertNull(new CheckInRepository(db).get("6"));
    }

    @Test
    void securityFailureCompensatesA() {
        PassengerRequest request = new PassengerRequest(
                "7", "Alex", 18, true,
                true, false, true, true
        );

        String result = new AirportFacade(db).processPassenger(request);

        assertTrue(result.startsWith("COMPENSATED"));
        assertEquals("CANCELLED", new CheckInRepository(db).get("7"));
    }

    @Test
    void customsFailureCompensatesBothStages() {
        PassengerRequest request = new PassengerRequest(
                "8", "Alex", 18, true,
                true, true, false, true
        );

        String result = new AirportFacade(db).processPassenger(request);

        assertTrue(result.startsWith("COMPENSATED"));
        assertEquals("CANCELLED", new CheckInRepository(db).get("8"));
        assertEquals("REVOKED", new ProcessingRepository(db).get("8"));
    }

    @Test
    void gateFailureCompensatesBothStages() {
        PassengerRequest request = new PassengerRequest(
                "9", "Alex", 18, true,
                true, true, true, false
        );

        String result = new AirportFacade(db).processPassenger(request);

        assertTrue(result.startsWith("COMPENSATED"));
        assertEquals("CANCELLED", new CheckInRepository(db).get("9"));
        assertEquals("REVOKED", new ProcessingRepository(db).get("9"));
    }

    @Test
    void entitiesRejectIllegalTransitions() {
        assertThrows(
                IllegalStateException.class,
                () -> new CheckInRecord().move(
                        CheckInRecord.Status.CANCELLED
                )
        );

        assertThrows(
                IllegalStateException.class,
                () -> new ProcessingRecord().move(
                        ProcessingRecord.Status.REVOKED
                )
        );

        ClearanceRecord record = new ClearanceRecord();
        record.move(ClearanceRecord.Status.APPROVED);

        assertThrows(
                IllegalStateException.class,
                () -> record.move(ClearanceRecord.Status.REJECTED)
        );
    }

    @Test
    void adminListsAndInspectsPassengers() {
        new AirportFacade(db)
                .processPassenger(passenger("10", 18, true));

        AirportAdminFacade admin = new AirportAdminFacade(db);

        assertEquals(1, admin.allIds().size());
        assertTrue(admin.inspect("10").contains("COMPLETED"));
        assertEquals("Not found", admin.inspect("unknown"));
    }

    @Test
    void duplicateIdsAreRejected() {
        AirportFacade airport = new AirportFacade(db);

        airport.processPassenger(passenger("11", 18, true));

        assertThrows(
                AirportException.class,
                () -> airport.processPassenger(passenger("11", 18, true))
        );
    }

    @Test
    void completedBoardingCannotBeCompensated() {
        new AirportFacade(db)
                .processPassenger(passenger("12", 18, true));

        assertThrows(
                AirportException.class,
                () -> new AirportAdminFacade(db).compensate("12")
        );
    }

    @Test
    void manuallyCompensatesPendingWorkflow() {
        PassengerRequest request = passenger("13", 18, true);

        new WorkflowRepository(db).save(request, "PENDING_B");
        new CheckInRepository(db).save("13", "ISSUED");

        new AirportAdminFacade(db).compensate("13");

        assertEquals("CANCELLED", new CheckInRepository(db).get("13"));

        assertEquals(
                "COMPENSATED",
                new WorkflowRepository(db).get("13").status()
        );
    }

    @Test
    void finishesInterruptedCompensation() {
        PassengerRequest request = passenger("14", 18, false);

        new WorkflowRepository(db).save(request, "COMPENSATING");
        new CheckInRepository(db).save("14", "ISSUED");
        new ProcessingRepository(db).save("14", "REVOKED");

        assertEquals(
                "COMPENSATED",
                new AirportFacade(db).resume("14")
        );

        assertEquals("CANCELLED", new CheckInRepository(db).get("14"));
    }

    @Test
    void handlesRejectedCStateAfterRestart() {
        PassengerRequest request = passenger("15", 18, false);

        new WorkflowRepository(db).save(request, "PENDING_C");
        new CheckInRepository(db).save("15", "ISSUED");
        new ProcessingRepository(db).save("15", "CLEARED");
        new ClearanceRepository(db).save("15", "REJECTED");

        db.close();
        db = new Database(path.toString());

        String result = new AirportFacade(db).resume("15");

        assertTrue(result.startsWith("COMPENSATED"));
        assertEquals("REVOKED", new ProcessingRepository(db).get("15"));
    }

    @Test
    void rejectsInvalidWeights() {
        BaggageHandler handler = new BaggageHandler();

        assertThrows(
                AirportException.class,
                () -> handler.execute(-1)
        );

        assertThrows(
                AirportException.class,
                () -> handler.execute(30)
        );

        assertThrows(
                AirportException.class,
                () -> handler.execute(Double.NaN)
        );

        assertDoesNotThrow(() -> handler.execute(18));
    }

    @Test
    void subsystemsCanWorkAlone() {
        CheckInRecord check = new CheckInRecord();

        new Kiosk().execute(true);
        new BoardingPassIssuer().execute(check);

        new CheckInRepository(db).save("16", check.status().name());

        ProcessingRecord processed = new ProcessingRecord();

        new BaggageHandler().execute(18);
        new SecurityScreening().execute(true);

        processed.move(ProcessingRecord.Status.CLEARED);

        new ProcessingRepository(db).save(
                "16", processed.status().name()
        );

        ClearanceRecord cleared = new ClearanceRecord();

        new PassportControl().execute(true);
        new CustomsDeclaration().execute(true);
        new GateController().execute(true);

        cleared.move(ClearanceRecord.Status.APPROVED);

        new ClearanceRepository(db).save(
                "16", cleared.status().name()
        );

        assertEquals("ISSUED", new CheckInRepository(db).get("16"));
        assertEquals("CLEARED", new ProcessingRepository(db).get("16"));
        assertEquals("APPROVED", new ClearanceRepository(db).get("16"));
    }

    @Test
    void doesNotRepeatCompletedWorkflow() {
        AirportFacade airport = new AirportFacade(db);

        airport.processPassenger(passenger("17", 18, true));

        assertEquals("COMPLETED", airport.resume("17"));
    }
}
