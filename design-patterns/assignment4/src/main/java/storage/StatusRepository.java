
package storage;

import java.sql.*;

public abstract class StatusRepository {
    private final Database db;
    private final String section;

    protected StatusRepository(Database db, String section) {
        this.db = db;
        this.section = section;
    }

    public void save(String id, String status) {
        String sql = "INSERT INTO records(section,id,status) VALUES(?,?,?) " +
                "ON CONFLICT(section,id) DO UPDATE SET status=excluded.status";

        try (PreparedStatement p = db.connection().prepareStatement(sql)) {
            p.setString(1, section);
            p.setString(2, id);
            p.setString(3, status);
            p.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public String get(String id) {
        String sql = "SELECT status FROM records WHERE section=? AND id=?";

        try (PreparedStatement p = db.connection().prepareStatement(sql)) {
            p.setString(1, section);
            p.setString(2, id);

            try (ResultSet r = p.executeQuery()) {
                if (r.next()) {
                    return r.getString(1);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
