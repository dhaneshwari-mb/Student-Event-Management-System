import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:sqlite:events.db";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void createTables() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {

            // Create events table
            stmt.execute("CREATE TABLE IF NOT EXISTS events (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "date TEXT NOT NULL," +
                    "venue TEXT)");

            // Create students table
            stmt.execute("CREATE TABLE IF NOT EXISTS students (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "email TEXT)");

            // Create registrations table
            stmt.execute("CREATE TABLE IF NOT EXISTS registrations (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "student_id INTEGER," +
                    "event_id INTEGER," +
                    "FOREIGN KEY(student_id) REFERENCES students(id)," +
                    "FOREIGN KEY(event_id) REFERENCES events(id))");

            System.out.println("✅ Tables created successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Insert sample data for demo
    public static void insertSampleData() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute("INSERT INTO events(name, date, venue) VALUES('Placement Drive','2025-09-05','Auditorium')");
            stmt.execute("INSERT INTO students(name,email) VALUES('Alice','alice@example.com')");
            stmt.execute("INSERT INTO registrations(student_id,event_id) VALUES(1,1)");
            System.out.println("✅ Sample data inserted!");
        } catch (SQLException e) {
            // ignore if data already exists
        }
    }

    public static void main(String[] args) {
        createTables();
        insertSampleData();
    }
}
