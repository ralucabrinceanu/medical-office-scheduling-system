package domain;

import java.util.Objects;

public class Medic extends Person {
    public int id;
    public static int _id = 0;
    private Speciality speciality;
    private Room room;
    
    public Medic(String firstName, String lastName, String phoneNumber, String email, Speciality speciality, Room room) {
        super(firstName, lastName, phoneNumber, email);
        this.id = ++_id;
        this.speciality = speciality;
        this.room = room;
    }
    public Medic(String firstName, String lastName, String phoneNumber, String email, Speciality speciality, int roomNumber) {
        super(firstName, lastName, phoneNumber, email);
        this.id = ++_id;
        this.speciality = speciality;
        this.room = new Room();
        this.room.setNumber(roomNumber);
    }


    public Medic() {}

    public Medic(Medic medic) {
        if (medic != null) {
            this.id = medic.id;
            this.firstName = medic.firstName;
            this.lastName = medic.lastName;
            this.phoneNumber = medic.phoneNumber;
            this.email = medic.email;
            this.speciality = medic.speciality;
            this.room = medic.room;
        }
    }


    @Override
    public String toString() {
        return "Medic{ id=" + id + ", " + super.toString() +
                ", speciality=" + speciality +
                ", cabinet=Room#" + room.getNumber() +
                " }";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Medic medic = (Medic) o;
        return speciality == medic.speciality && Objects.equals(room, medic.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), speciality, room);
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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
