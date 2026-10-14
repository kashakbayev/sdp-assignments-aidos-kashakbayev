
package clearance;

import storage.Database;
import storage.StatusRepository;

public class ClearanceRepository extends StatusRepository {
    public ClearanceRepository(Database db) {
        super(db, "C");
    }
}
