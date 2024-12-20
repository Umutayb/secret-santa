package santa;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.SerializationFeature;
import context.ContextStore;
import santa.model.Participants;
import santa.model.Person;
import santa.model.SecretSanta;
import utils.FileUtilities;
import utils.Printer;
import utils.email.EmailUtilities;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static utils.MappingUtilities.Json.mapper;

public class SecretSantaScript {

    static List<Person> participants;
    static List<Person> receivers;
    static List<String> pairs;
    static boolean logsOn;
    static Printer log;

    static {
        ContextStore.loadProperties("application.properties");
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        logsOn = Boolean.parseBoolean(ContextStore.get("keep-email-logs", "false"));
        pairs = new ArrayList<>();
        participants = getParticipants();
        receivers = new ArrayList<>(participants);
        log = new Printer(SecretSantaScript.class);
    }

    public static void main(String[] args) {
        log.info("Getting started...");
        EmailUtilities emailUtilities = new EmailUtilities(ContextStore.get("host"));

        String dueDate = ContextStore.get("due-date");
        String organiserEmail = ContextStore.get("organiser-email");
        String budget = ContextStore.get("budget");

        log.info("Matching people and sending out the emails...");
        try {
            for(Person person : participants){
                SecretSanta santa = new SecretSanta(person, getRecipient(person));
                String htmlDirectory = "src/main/java/santa/email/email.html";

                String htmlContent = FileUtilities.getString(htmlDirectory);

                assert htmlContent != null;
                htmlContent = htmlContent.replace("{{Your Name}}", santa.getSecretSanta().getName());
                htmlContent = htmlContent.replace("{{Recipient Name}}", santa.getRecipient().getName());
                htmlContent = htmlContent.replace("{{Budget}}", budget);
                htmlContent = htmlContent.replace("{{Due Date}}", dueDate);
                htmlContent = htmlContent.replace("{{Organiser Email}}", organiserEmail);
                htmlContent = htmlContent.replace("{{Recipient Address}}", santa.getRecipient().getAddress());
                htmlContent = htmlContent.replace("{{Recipient Phone}}", santa.getRecipient().getPhoneNumber());

                emailUtilities.sendEmail(
                        "Secret Santa!",
                        htmlContent,
                        "text/html; charset=utf-8",
                        santa.getSecretSanta().getEmail(),
                        ContextStore.get("sender-email"),
                        ContextStore.get("sender-email-application-password"),
                        null
                );
            }
            log.success("All Secret Santa emails (" + participants.size() + " people) are sent!");
            if (logsOn)
                for (String pair:pairs)
                    log.info(pair);
        }
        catch (Exception exception){
            log.error(exception.getLocalizedMessage(), exception);
        }
    }

    static Person getRecipient(Person santa){
        Person receiver;
        int randomIndex;
        do {
            randomIndex = new Random().nextInt(receivers.size());
            receiver = receivers.get(randomIndex);
        }
        while (receiver.getName().equals(santa.getName()));

        receivers.remove(randomIndex);

        if (logsOn) {
            log.info(santa.getName() + " matched with " + receiver.getName());
            pairs.add(santa.getName() + " matched with " + receiver.getName());
        }
        return receiver;
    }

    static List<Person> getParticipants() {
        try(FileReader file = new FileReader((String) ContextStore.get("participants-directory"))) {
            return mapper.readValue(file, Participants.class).getParticipants();
        }
        catch (IOException e) {throw new RuntimeException(e);}
    }
}
