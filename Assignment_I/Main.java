package Assignment_I;

public class Main {

    public void main(String[] args) {
        EmailDirector director = new EmailDirector();

        EmailObjectBuilder objBuilder = new EmailObjectBuilder();
        director.makeWelcomeEmail(objBuilder);
        Email welcomeEmail = objBuilder.getResult();
        System.out.println(welcomeEmail.toString());

        EmailPreviewBuilder previewBuilder = new EmailPreviewBuilder();
        director.makeReminderEmail(previewBuilder);
        String preview = previewBuilder.getResult();
        System.out.println(preview);
    }

}
