
package storage;

import model.PassengerRequest;
import model.WorkflowRecord;
import java.sql.*;
import java.util.*;

public class WorkflowRepository {
    private final Database db;

    public WorkflowRepository(Database db) {
        this.db = db;
    }

    public void save(PassengerRequest r, String status) {
        String sql = "INSERT INTO workflows VALUES(?,?,?,?,?,?,?,?,?) " +
                "ON CONFLICT(id) DO UPDATE SET status=excluded.status";

        try (PreparedStatement p = db.connection().prepareStatement(sql)) {
            p.setString(1, r.id());
            p.setString(2, r.name());
            p.setDouble(3, r.weight());
            p.setInt(4, r.passportValid() ? 1 : 0);
            p.setInt(5, r.bookingValid() ? 1 : 0);
            p.setInt(6, r.securityPassed() ? 1 : 0);
            p.setInt(7, r.customsCleared() ? 1 : 0);
            p.setInt(8, r.gateOpen() ? 1 : 0);
            p.setString(9, status);
            p.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public WorkflowRecord get(String id) {
        String sql = "SELECT * FROM workflows WHERE id=?";

        try (PreparedStatement p = db.connection().prepareStatement(sql)) {
            p.setString(1, id);

            try (ResultSet r = p.executeQuery()) {
                if (!r.next()) {
                    return null;
                }

                PassengerRequest request = new PassengerRequest(
                        r.getString("id"),
                        r.getString("name"),
                        r.getDouble("weight"),
                        r.getInt("passport") != 0,
                        r.getInt("booking") != 0,
                        r.getInt("security") != 0,
                        r.getInt("customs") != 0,
                        r.getInt("gate") != 0
                );

                return new WorkflowRecord(request, r.getString("status"));
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<String> ids() {
        List<String> result = new ArrayList<>();
        String sql = "SELECT id FROM workflows ORDER BY id";

        try (Statement s = db.connection().createStatement();
             ResultSet r = s.executeQuery(sql)) {

            while (r.next()) {
                result.add(r.getString(1));
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }

        return result;
    }
}
