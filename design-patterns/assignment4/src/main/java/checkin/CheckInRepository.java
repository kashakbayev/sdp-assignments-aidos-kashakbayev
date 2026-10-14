
package checkin;

import storage.Database;
import storage.StatusRepository;

public class CheckInRepository extends StatusRepository {
    public CheckInRepository(Database db) {
        super(db, "A");
    }
}
