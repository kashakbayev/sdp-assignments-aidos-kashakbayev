package Assignment_I;

public class EmailDirector {
    public void makeWelcomeEmail(EmailBuilder e) {
        e.setFrom("Prof. Smith").setTo("Student").setSubject("Welcome to my course").setBody("You have successfully registered course CSCI261");
    }

    public void makeReminderEmail(EmailBuilder e) {
        e.setFrom("Prof. Adam").setTo("Student2").setSubject("Deadline extension").setBody("Your deadline was extended");
    }
}
