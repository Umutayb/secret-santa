package santa.model;

public class Person {
    String name;
    String email;
    String address;
    String phoneNumber;

    public Person(String name, String email, String addressString, String phoneNumber) {
        this.phoneNumber = phoneNumber;
        this.address = addressString;
        this.email = email;
        this.name = name;
    }

    public Person() {}

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
