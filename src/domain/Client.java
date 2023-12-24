package domain;

import java.util.Objects;

public class Client extends Person {
    public int id;
    public static int _id = 0;
    private Date birthDate;

    public Client(String firstName, String lastName, String phoneNumber, String email, Date birthDate) {
        super(firstName, lastName, phoneNumber, email);
        this.id = ++_id;
        this.birthDate = birthDate;
    }

    public Client() {}

    public Client(Client client) {
        if (client != null) {
            this.id = client.id;
            this.firstName = client.firstName;
            this.lastName = client.lastName;
            this.phoneNumber = client.phoneNumber;
            this.email = client.email;
            this.birthDate = client.birthDate;
        }
    }

    @Override
    public String toString() {
        return "Client { id=" + id + ", " + super.toString() + ", birthDate=" + birthDate + " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Client client = (Client) o;
        return Objects.equals(birthDate, client.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), birthDate);
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
