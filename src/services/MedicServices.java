package services;

import domain.Medic;
import domain.Room;
import domain.Speciality;
import exceptions.InvalidDataException;
import persistence.MedicRepository;
import persistence.RoomRepository;
import services.csv.MedicCSVServices;
import services.db.ConnectionManager;
import services.db.MedicDBServices;

import java.util.ArrayList;
import java.util.Vector;

public class MedicServices {
    private MedicRepository medicRepository = new MedicRepository();
    private RoomRepository roomRepository = new RoomRepository();

    public Vector<Medic> getAllMedics() {
        Vector<Medic> medics = new Vector<>();
        for (int i = 0; i < medicRepository.getSize(); i++)
            medics.add(medicRepository.get(i));
        return medics;
    }

    public Vector<Medic> getMedicsWithSpeciality(Speciality speciality) {
        Vector<Medic> medics = new Vector<>();
        for (int i = 0; i < medicRepository.getSize(); i++)
            if (medicRepository.get(i).getSpeciality().equals(speciality))
                medics.add(medicRepository.get(i));
        return medics;
    }

    public void addNewMedic(Medic medic) throws InvalidDataException {
        if (medic == null)
            throw new InvalidDataException("medic is null");

        Medic medic1 = new Medic(medic);
        medicRepository.add(medic1);
    }

    private boolean isValidMedic(String firstName, String lastName, String phoneNumber, String email,
                                 String spec) throws InvalidDataException {
        if(firstName == null || firstName.equals("") || !firstName.matches("[a-zA-Z]+") || lastName == null || lastName.equals("") || !firstName.matches("[a-zA-Z]+"))
            throw new InvalidDataException("invalid name");

        if(phoneNumber.length() != 10 || !phoneNumber.matches("^0\\d{9}$"))
            throw new InvalidDataException("phone number must be 10 digits long and starts with 0.");

        if(!email.matches("[a-z0-9]+@[a-z]+.[a-z]+"))
            throw new InvalidDataException("email is not valid.");

        boolean ok = false;
        for (Speciality speciality1 : Speciality.values()) {
            if (speciality1.toString().equals(spec)) {ok = true;}
        }
        if (!ok) {
            throw new InvalidDataException("invalid speciality");
        }
        return true;
    }

    public void addNewMedic(String firstName, String lastName, String phoneNumber,
                            String email, String spec, int roomIndex) throws InvalidDataException {

        if (isValidMedic(firstName, lastName, phoneNumber, email, spec)) {
            Speciality speciality1 = Speciality.valueOf(spec);

            if(roomIndex < 0 || roomIndex >= roomRepository.getSize())
                throw new InvalidDataException("invalid room index");

            Medic medic1 = new Medic(firstName, lastName, phoneNumber, email, speciality1, roomRepository.get(roomIndex));
            medicRepository.add(medic1);
        }
    }

    public void deleteMedic(int index) throws InvalidDataException{
        if (index < 0 || index >= medicRepository.getSize())
            throw new InvalidDataException("invalid medic index");

        medicRepository.delete(index);
    }

    public void addRoom(Room room) {
        roomRepository.add(room);
    }

    public void loadFromCSVFile() {
        MedicCSVServices csvServices = MedicCSVServices.getInstance();
        Vector<Medic> medics = new Vector<>(csvServices.read());
        for (Medic medic : medics)
            medicRepository.add(medic);
    }

    public void addMedicToDB(ConnectionManager con, Medic medic) throws InvalidDataException {
        MedicDBServices dbService = new MedicDBServices(con);
        if (isValidMedic(medic.getFirstName(), medic.getLastName(), medic.getPhoneNumber(),
                medic.getEmail(), medic.getSpeciality().toString()))
            dbService.insertItem(medic);
    }

    public void updateMedicInDB(ConnectionManager con, int id, Medic medic) throws InvalidDataException {
        MedicDBServices dbService = new MedicDBServices(con);
        if (isValidMedic(medic.getFirstName(), medic.getLastName(), medic.getPhoneNumber(),
                medic.getEmail(), medic.getSpeciality().toString()))
            dbService.updateItem(id, medic);
    }

    public void deleteMedicInDB(ConnectionManager con, int id) throws InvalidDataException{
        MedicDBServices dbService = new MedicDBServices(con);
        dbService.deleteItem(id);
    }

    public ArrayList<Medic> getMedicsFromDB(ConnectionManager con){
        MedicDBServices dbService = new MedicDBServices(con);
        ArrayList<Medic> dbMedics = dbService.getAllItems();
        return dbMedics;
    }
}