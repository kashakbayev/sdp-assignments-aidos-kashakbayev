
package processing;

import storage.Database;
import storage.StatusRepository;

public class ProcessingRepository extends StatusRepository {
    public ProcessingRepository(Database db) {
        super(db, "B");
    }
}
