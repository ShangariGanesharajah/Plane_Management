public class Person {
    private final String name;
    private final String surname;
    private final String email;

    // Constructor
    public Person(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    // Method to print person's information
    public void printInformation() {
        System.out.println("Name: " + name + " " + surname + ", Email: " + email);
    }
}
