package santa.model;

public class SecretSanta {
    Person secretSanta;
    Person recipient;

    public SecretSanta(Person secretSanta, Person recipient) {
        this.secretSanta = secretSanta;
        this.recipient = recipient;
    }

    public Person getSecretSanta() {
        return secretSanta;
    }

    public Person getRecipient() {
        return recipient;
    }
}
