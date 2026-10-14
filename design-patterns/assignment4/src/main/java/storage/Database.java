
package storage;

import java.sql.*;

public class Database implements AutoCloseable {
    private final Connection connection;

    public Database(String path) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + path);

            try (Statement s = connection.createStatement()) {
                s.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS records (" +
                                "section TEXT NOT NULL, " +
                                "id TEXT NOT NULL, " +
                                "status TEXT NOT NULL, " +
                                "PRIMARY KEY(section, id))"
                );

                s.executeUpdate(
                        "CREATE TABLE IF NOT EXISTS workflows (" +
                                "id TEXT PRIMARY KEY, " +
                                "name TEXT NOT NULL, " +
                                "weight REAL NOT NULL, " +
                                "passport INTEGER NOT NULL, " +
                                "booking INTEGER NOT NULL, " +
                                "security INTEGER NOT NULL, " +
                                "customs INTEGER NOT NULL, " +
                                "gate INTEGER NOT NULL, " +
                                "status TEXT NOT NULL)"
                );
            }
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public Connection connection() {
        return connection;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
