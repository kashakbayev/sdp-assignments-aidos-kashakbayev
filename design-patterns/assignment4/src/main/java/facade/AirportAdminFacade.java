
package facade;

import checkin.CheckInRepository;
import processing.ProcessingRepository;
import clearance.ClearanceRepository;
import storage.*;
import model.WorkflowRecord;
import java.util.List;

public class AirportAdminFacade {
    private final WorkflowRepository workflows;
    private final CheckInRepository checkIns;
    private final ProcessingRepository processing;
    private final ClearanceRepository clearances;
    private final AirportFacade airport;

    public AirportAdminFacade(Database db) {
        workflows = new WorkflowRepository(db);
        checkIns = new CheckInRepository(db);
        processing = new ProcessingRepository(db);
        clearances = new ClearanceRepository(db);
        airport = new AirportFacade(db);
    }

    public String inspect(String id) {
        WorkflowRecord record = workflows.get(id);

        if (record == null) {
            return "Not found";
        }

        return id +
                " | " + record.request().name() +
                " | workflow=" + record.status() +
                " | A=" + checkIns.get(id) +
                " | B=" + processing.get(id) +
                " | C=" + clearances.get(id);
    }

    public List<String> allIds() {
        return workflows.ids();
    }

    public void compensate(String id) {
        airport.compensate(id);
    }

    public String resume(String id) {
        return airport.resume(id);
    }
}
