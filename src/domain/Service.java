package domain;

import java.util.Objects;

public class Service {
    public int id;
    public static int _id = 0;
    private String name;
    private double price;
    private Speciality speciality;


    public Service(String name, double price, Speciality speciality) {
        this.name = name;
        this.price = price;
        this.speciality = speciality;
        this.id = ++_id;
    }

    public Service() {
    }

    public Service(Service service) {
        if (service != null) {
            this.name = service.name;
            this.price = service.price;
            this.speciality = service.speciality;
            this.id = service.id;
        }
    }

    @Override
    public String toString() {
        return "Service { id=" + id + ", name='" + name + ", price=" + price +
                ", speciality=" + speciality + " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return price == service.price && Objects.equals(name, service.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
