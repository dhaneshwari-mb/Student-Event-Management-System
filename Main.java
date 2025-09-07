import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String DB_URL = "jdbc:sqlite:student_events.db";
    private static final String ADMIN_PASSWORD = "dhane1203";

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        createTables();

        while (true) {
            System.out.println("\n==== Student Event Management System ====");
            System.out.println("Select Event:");
            System.out.println("1. Placement");
            System.out.println("2. Symposium");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            int eventChoice = scanner.nextInt();

            if (eventChoice == 3) {
                System.out.println("Exiting system...");
                break;
            }

            int eventId = (eventChoice == 1) ? 1 : 2;

            System.out.println("\nLogin as:");
            System.out.println("1. Admin");
            System.out.println("2. User");
            int role = scanner.nextInt();

            if (role == 1) {
                handleAdmin(eventId);
            } else {
                handleUser(eventId);
            }
        }
    }

    private static void createTables() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS students (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "roll TEXT," +
                    "email TEXT," +
                    "phone TEXT)");

            stmt.execute("CREATE TABLE IF NOT EXISTS events (" +
                    "id INTEGER PRIMARY KEY," +
                    "name TEXT)");

            stmt.execute("CREATE TABLE IF NOT EXISTS registrations (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "student_id INTEGER," +
                    "event_id INTEGER," +
                    "attendance INTEGER DEFAULT 0," +
                    "FOREIGN KEY(student_id) REFERENCES students(id)," +
                    "FOREIGN KEY(event_id) REFERENCES events(id))");

            // Insert default events only once
            stmt.execute("INSERT OR IGNORE INTO events (id, name) VALUES (1, 'Placement')");
            stmt.execute("INSERT OR IGNORE INTO events (id, name) VALUES (2, 'Symposium')");

        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }

    private static void handleAdmin(int eventId) {
        System.out.print("Enter admin password: ");
        String password = scanner.next();

        if (!password.equals(ADMIN_PASSWORD)) {
            System.out.println("❌ Wrong password! Access denied.");
            return;
        }

        while (true) {
            System.out.println("\n==== Admin Menu ====");
            System.out.println("1. Add Student & Register");
            System.out.println("2. Add Event (not required usually)");
            System.out.println("3. View Participants");
            System.out.println("4. Mark Attendance");
            System.out.println("5. Remove Participant");
            System.out.println("6. Exit Admin Menu");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1: registerStudent(eventId, true); break;
                case 2: addEvent(); break;
                case 3: viewParticipants(eventId); break;
                case 4: markAttendance(eventId); break;
                case 5: removeParticipant(eventId); break;
                case 6: return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    private static void handleUser(int eventId) {
        while (true) {
            System.out.println("\n==== User Menu ====");
            System.out.println("1. Register");
            System.out.println("2. Exit User Menu");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1: registerStudent(eventId, false); break;
                case 2: return;
                default: System.out.println("Invalid choice!");
            }
        }
    }

    private static void registerStudent(int eventId, boolean byAdmin) {
        scanner.nextLine(); // consume newline
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter roll number: ");
        String roll = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            // Insert into students table
            String sql = "INSERT INTO students (name, roll, email, phone) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, name);
            pstmt.setString(2, roll);
            pstmt.setString(3, email);
            pstmt.setString(4, phone);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            int studentId = -1;
            if (rs.next()) {
                studentId = rs.getInt(1);
            }

            // Register student for event
            String regSql = "INSERT INTO registrations (student_id, event_id) VALUES (?, ?)";
            PreparedStatement regPstmt = conn.prepareStatement(regSql);
            regPstmt.setInt(1, studentId);
            regPstmt.setInt(2, eventId);
            regPstmt.executeUpdate();

            System.out.println("✅ Student registered successfully for event ID: " + eventId);

            // Send simulated notifications
            NotificationService.sendEmail(email, "Event Registration Confirmation",
                    "Hello " + name + ", you are registered for event ID: " + eventId);
            NotificationService.sendSMS(phone,
                    "Hi " + name + ", your registration for event ID " + eventId + " is confirmed!");

        } catch (SQLException e) {
            System.out.println("Error registering student: " + e.getMessage());
        }
    }

    private static void addEvent() {
        scanner.nextLine();
        System.out.print("Enter new event name: ");
        String eventName = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "INSERT INTO events (name) VALUES (?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, eventName);
            pstmt.executeUpdate();
            System.out.println("✅ Event added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding event: " + e.getMessage());
        }
    }

    private static void viewParticipants(int eventId) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT s.name, s.roll, s.email, s.phone, r.attendance " +
                    "FROM students s JOIN registrations r ON s.id = r.student_id " +
                    "WHERE r.event_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, eventId);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\nParticipants for Event ID " + eventId + ":");
            while (rs.next()) {
                System.out.println("Name: " + rs.getString("name") +
                        ", Roll: " + rs.getString("roll") +
                        ", Email: " + rs.getString("email") +
                        ", Phone: " + rs.getString("phone") +
                        ", Attendance: " + (rs.getInt("attendance") == 1 ? "Present" : "Absent"));
            }
        } catch (SQLException e) {
            System.out.println("Error viewing participants: " + e.getMessage());
        }
    }

    private static void markAttendance(int eventId) {
        scanner.nextLine();
        System.out.print("Enter roll number to mark attendance: ");
        String roll = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "UPDATE registrations SET attendance = 1 WHERE student_id = " +
                    "(SELECT id FROM students WHERE roll = ?) AND event_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roll);
            pstmt.setInt(2, eventId);
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Attendance marked for Roll: " + roll);
            } else {
                System.out.println("⚠️ Student not found in this event.");
            }
        } catch (SQLException e) {
            System.out.println("Error marking attendance: " + e.getMessage());
        }
    }

    private static void removeParticipant(int eventId) {
        scanner.nextLine();
        System.out.print("Enter roll number to remove: ");
        String roll = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = "DELETE FROM registrations WHERE student_id = " +
                    "(SELECT id FROM students WHERE roll = ?) AND event_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, roll);
            pstmt.setInt(2, eventId);
            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Participant removed: " + roll);
            } else {
                System.out.println("⚠️ Student not found in this event.");
            }
        } catch (SQLException e) {
            System.out.println("Error removing participant: " + e.getMessage());
        }
    }
}
