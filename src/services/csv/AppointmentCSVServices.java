package services.csv;

import domain.*;
import exceptions.InvalidDataException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class AppointmentCSVServices implements GenericCSVServices<Appointment> {
    private static final AppointmentCSVServices INSTANCE = new AppointmentCSVServices();
    private AppointmentCSVServices(){}
    public static AppointmentCSVServices getInstance() {return INSTANCE;}

    @Override
    public void write(Appointment object) {
        try (FileWriter fileWriter = new FileWriter("files/appointments.csv", true)) {
            Client client = new Client(object.getClient());
            Medic medic = new Medic(object.getMedic());
            Service service = new Service(object.getService());
            DateTime date = new DateTime(object.getDateTime());
            fileWriter.write(
                    object.getId() + "," +
                            client.getFirstName() + "," + client.getLastName() + "," +
                            client.getPhoneNumber() +"," + client.getEmail() + "," +
                            client.getBirthDate().getDay() + "," + client.getBirthDate().getMonth() + "," +
                            client.getBirthDate().getYear() + "," +

                            medic.getFirstName() + ","+ medic.getLastName() + "," +
                            medic.getPhoneNumber() + "," + medic.getEmail() + "," + medic.getSpeciality().name() + ","+
                            medic.getRoom().getNumber() + "," + medic.getRoom().getFloor() + "," + medic.getRoom().getSurface() + ","+

                            service.getName() + "," +service.getPrice() + "," + service.getSpeciality().name() + "," +

                            date.getDay() + "," + date.getMonth() + "," + date.getYear() + "," +
                            date.getHour() + "," + date.getMinutes() +
                            "\n"
            );
            fileWriter.flush();
        } catch (IOException e) {
            System.out.println("AppointmentCSVServices: error writing to file");
        }
    }

    @Override
    public Vector<Appointment> read() {
        Vector<Appointment> appointments = new Vector<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("files/appointments.csv"))){
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(",");
                domain.Date clientDate = new Date(Integer.parseInt(tokens[5]), Integer.parseInt(tokens[6]), Integer.parseInt(tokens[7]));
                Client client = new Client(tokens[1], tokens[2], tokens[3], tokens[4], clientDate);

                Room medicRoom = new Room(Integer.parseInt(tokens[13]), Integer.parseInt(tokens[14]), Double.parseDouble(tokens[15]));
                Medic medic = new Medic();
                medic.setFirstName(tokens[8]);
                medic.setLastName(tokens[9]);
                medic.setPhoneNumber(tokens[10]);
                medic.setEmail(tokens[11]);
                medic.setSpeciality(Speciality.valueOf(tokens[12]));
                medic.setRoom(medicRoom);

                Service service = new Service();
                service.setName(tokens[16]);
                service.setPrice(Double.parseDouble(tokens[17]));
                service.setSpeciality(Speciality.valueOf(tokens[18]));

                DateTime date = new DateTime(Integer.parseInt(tokens[19]), Integer.parseInt(tokens[20]), Integer.parseInt(tokens[21]),
                        Integer.parseInt(tokens[22]), Integer.parseInt(tokens[23]));

                Appointment appointment = new Appointment(client, medic, service, date);
                appointment.setId(Integer.parseInt(tokens[0]));
                appointments.add(appointment);
            }
        } catch (IOException ex) {
            System.out.println("AppointmentCSVServices: error reading from file");
        } catch (InvalidDataException ex) {
            System.out.println("invalid date");
        }
        return appointments;
    }

    public void delete(Appointment appointment) {
        Vector<Appointment> appts = read();

        boolean removed = appts.removeIf(a -> a.getId() == appointment.getId());

        if (removed) {
            try (FileWriter fileWriter = new FileWriter("files/appointments.csv", false)) {
                for (Appointment a : appts) {
                    write(a);
                }
            } catch (IOException ex) {
                System.out.println("AppointmentCSVServices: error writing to file");
            }
        } else {
            System.out.println("appointment not found for deletion");
        }
    }

    public void update(Appointment updateApp) {
        Vector<Appointment> appts = read();

        for (int i = 0; i < appts.size(); i++) {
            if(appts.get(i).getId() == updateApp.getId()) {
                appts.set(i, updateApp);
                break;
            }
        }

        try (FileWriter fileWriter = new FileWriter("files/appointment.csv", true)) {
            for (Appointment a : appts) {
                write(a);
            }
        } catch (IOException ex) {
            System.out.println("AppointmentsCSVServices: error writting to file");
        }
    }

}
