package domain;

import java.util.Objects;

public class Appointment {
    public int id;
    public static int _id = 0;
    private Client client;
    private Medic medic;
    private Service service;
    private DateTime dateTime;

    public Appointment(Client client, Medic medic, Service service, DateTime dateTime) {
        this.client = client;
        this.medic = medic;
        this.service = service;
        this.dateTime = dateTime;
        this.id = ++_id;
    }

    public Appointment() {}

    public Appointment(Appointment appointment) {
        if (appointment != null) {
            this.client = appointment.getClient();
            this.medic = appointment.getMedic();
            this.service = appointment.getService();
            this.dateTime = appointment.getDateTime();
            this.id = appointment.getId();
        }
    }

    @Override
    public String toString() {
        return "Appointment { id=" + id +
                ", client=" + client.getLastName() + " " + client.getFirstName() +
                ", medic=" + medic.getLastName() + " " + medic.getFirstName() +
                ", service=" + service.getName() +
                ", date=" + dateTime +
                " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(client, that.client) &&
                Objects.equals(medic, that.medic) &&
                Objects.equals(service, that.service) &&
                Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, medic, service);
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Medic getMedic() {
        return medic;
    }

    public void setMedic(Medic medic) {
        this.medic = medic;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
