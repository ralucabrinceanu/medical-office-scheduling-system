package services.csv;

import domain.Medic;
import domain.Room;
import domain.Service;
import domain.Speciality;
import exceptions.InvalidDataException;

import javax.management.relation.RoleUnresolved;
import java.io.*;
import java.util.Vector;

public class MedicCSVServices implements GenericCSVServices<Medic> {

    private static final MedicCSVServices INSTANCE = new MedicCSVServices();
    public MedicCSVServices(){}
    public static MedicCSVServices getInstance() {return INSTANCE;}

//    @Override
//    public void write(Medic object) {
//        try (FileWriter fileWriter = new FileWriter("files/medics.csv", true)) {
//            fileWriter.write( object.getId() + "," + object.getFirstName() + "," +
//                    object.getLastName() + "," + object.getPhoneNumber() + "," +
//                    object.getEmail() + "," + object.getSpeciality().name() + "\n");
//            fileWriter.flush();
//        } catch (IOException ex) {
//            System.out.println("MedicCSVServices: error writing to file");
//        }
//
//    }

    @Override
    public void write(Medic object) {
        Vector<Medic> existingMedic =read();

        boolean medicExists = false;
        for (Medic medic : existingMedic) {
            if (medic.getId() == object.getId()) {
                medicExists = true;
                break;
            }
        }

        if(!medicExists) {
            try (FileWriter fileWriter = new FileWriter("files/medics.csv", true)){
                fileWriter.write( object.getId() + "," + object.getFirstName() + "," +
                    object.getLastName() + "," + object.getPhoneNumber() + "," +
                    object.getEmail() + "," + object.getSpeciality().name() + "," +
                        object.getRoom().getNumber() + "," + object.getRoom().getFloor() + "," +
                        object.getRoom().getSurface() + "\n");
                fileWriter.flush();
            } catch (IOException ex) {
                System.out.println("MedicCSVServices: error writing to file.");
            }
        }

    }

    @Override
    public Vector<Medic> read(){
        Vector<Medic> medics = new Vector<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("files/medics.csv"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(",");
                Room room = new Room(Integer.parseInt(tokens[6]), Integer.parseInt(tokens[7]), Double.parseDouble(tokens[8]));
                Medic medic = new Medic(tokens[1], tokens[2], tokens[3], tokens[4], Speciality.valueOf(tokens[5]), room);
                medic.setId(Integer.parseInt(tokens[0]));
                medics.add(medic);
            }
        } catch (IOException ex) {
            System.out.println("MedicCSVServices: error reading from file");
        }
//        medics.clear();
        return medics;
    }

}
