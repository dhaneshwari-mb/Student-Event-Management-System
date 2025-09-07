import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationService {

    // Simulate Email Sending
    public static void sendEmail(String to, String subject, String body) {
        String timestamp = getTimestamp();
        System.out.println("\n📧 Email Notification Sent");
        System.out.println("-------------------------------");
        System.out.println("To      : " + to);
        System.out.println("Subject : " + subject);
        System.out.println("Body    : " + body);
        System.out.println("Status  : ✅ Delivered Successfully at " + timestamp);
        System.out.println("-------------------------------\n");
    }

    // Simulate SMS Sending
    public static void sendSMS(String phone, String message) {
        String timestamp = getTimestamp();
        System.out.println("\n📱 SMS Notification Sent");
        System.out.println("-------------------------------");
        System.out.println("To      : " + phone);
        System.out.println("Message : " + message);
        System.out.println("Status  : ✅ Delivered Successfully at " + timestamp);
        System.out.println("-------------------------------\n");
    }

    // Helper: Current Date-Time
    private static String getTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }
}

