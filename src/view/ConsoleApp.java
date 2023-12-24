package view;

import domain.*;
import persistence.AppointmentRepository;
import persistence.ClientRepository;
import persistence.MedicRepository;
import persistence.ServiceRepository;
import services.*;
import services.csv.*;
import exceptions.InvalidDataException;
import services.db.ConnectionManager;

import javax.swing.*;
import java.io.BufferedReader;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

public class ConsoleApp {
    private Scanner s = new Scanner(System.in);

    private ClientServices clientService = new ClientServices();
    private ServiceServices serviceService = new ServiceServices();
    private MedicServices medicService = new MedicServices();
    private AppointmentServices appService = new AppointmentServices();

    private ClientRepository clientRepository = new ClientRepository();
    private ServiceRepository serviceRepository = new ServiceRepository();
    private MedicRepository medicRepository = new MedicRepository();
    private AppointmentRepository appRepository = new AppointmentRepository();

    public static void main(String args[]) throws ClassNotFoundException, SQLException, InvalidDataException {
        ConsoleApp app = new ConsoleApp();
        app.loadCSVFiles();
        Room room0 = new Room(1, 1, 40);
        Room room1 = new Room(2, 1, 35);
        app.medicService.addRoom(room0);
        app.medicService.addRoom(room1);

        ConnectionManager con = new ConnectionManager("jdbc:mysql://localhost:3306/medoffice","root","root");

        while (true) {
            app.showMenu();
            int option = app.readOption();
            app.execute(option);
        }
    }

    private void audit(ConnectionManager con, String action) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        AuditServices auditServices = new AuditServices();
        auditServices.write(con, action, dtf.format(now));
    }

    private void loadCSVFiles() {
        clientService.loadFromCSVFile();
        serviceService.loadFromCSVFile();
        medicService.loadFromCSVFile();
        appService.loadFromCSVFile();
    }

    private void showMenu() {
        System.out.println("-------------- MEDICAL OFFICE --------------");
        System.out.println("choose one of the options below:");

//        CLIENT
        System.out.println("1. add client");
        System.out.println("2. list all clients");
        System.out.println("3. update client (lastname / phone / email / birthdate)");
        System.out.println("4. delete client");

//        MEDIC
        System.out.println("5. add medic");
        System.out.println("6. list all medics");

//        SERVICE
        System.out.println("7. add service");
        System.out.println("8. list all services");

//        APPOINTMENT
        System.out.println("9. add appointment");
        System.out.println("10. list all appointments (all / client / medic)");
        System.out.println("11. update appointment (date / service / medic)");
        System.out.println("12. delete appointment");

//        DB
        System.out.println("13/14/15/16. add/update/delete/get client in/from DB");
        System.out.println("17/18/19/20. add/update/delete/get medic in/from DB");
        System.out.println("21/22/23/24. add/update/delete/get service in/from DB");
        System.out.println("25/26/27/28. add/update/delete/get appointment in/from DB");

//        end
        System.out.println("0. exit");
        System.out.println("-------------------- END -------------------");
        System.out.println("your option:");
    }

    private int readOption() {
        try {
            int option = readInt();
            if (option >= 0 && option <= 28)
                return option;
        } catch (InvalidDataException e) {
//            nothing to do
        }
        System.out.println("invalid option. try again.");
        return readOption();
    }

    private int readInt() throws InvalidDataException {
        String line = s.nextLine();
        if (line.matches("^\\d+$")) {
            return Integer.parseInt(line);
        } else {
            throw new InvalidDataException("invalid number.");
        }
    }

    private void execute(int option) throws ClassNotFoundException, SQLException {
        ConnectionManager con = new ConnectionManager("jdbc:mysql://localhost:3306/medoffice","root","root");

        switch (option) {
            case 1:
                addClientToCSV();
                audit(con, "added new client");
                break;
            case 2:
                printAllClients();
                audit(con, "printed all clients");
                break;
            case 3:
                updtClient();
                audit(con, "updated client");
                break;
            case 4:
                delClient();
                audit(con, "deleted client");
                break;
            case 5:
                addMedicToCSV();
                audit(con, "added new medic");
                break;
            case 6:
                printAllMedics();
                audit(con, "printed all medics");
                break;
            case 7:
                addServiceToCSV();
                audit(con, "added new service");
                break;
            case 8:
                printAllServices();
                audit(con, "printed all services");
                break;
            case 9:
                readAppointment();
                audit(con, "added new appointment");
                break;
            case 10:
                listAllAppointments();
                audit(con, "printed all appointments");
                break;
            case 11:
                updateAppointment();
                audit(con, "updated appointment");
                break;
            case 12:
                System.out.print("enter the index of the appointment to delete:");
                int index = s.nextInt();
                s.nextLine();
                appService.deleteAppointment(index);
                audit(con, "deleted appointment");
                break;
            case 13:
                addClientInDB(con);
                audit(con, "added client to DB");
                break;
            case 14:
                updateClientInDB(con);
                audit(con, "updated client in DB");
                break;
            case 15:
                deleteClientInDB(con);
                audit(con, "deleted client from DB");
                break;
            case 16:
                getAllClientsFromDB(con);
                audit(con, "loaded clients from DB");
                break;
            case 17:
                addMedicInDB(con);
                audit(con, "added medic to DB");
                break;
            case 18:
                updateMedicInDB(con);
                audit(con, "updated medic in DB");
                break;
            case 19:
                deleteMedicFromDB(con);
                audit(con, "deleted medic from DB");
                break;
            case 20:
                getAllMedicsFromDB(con);
                audit(con, "loaded medics from DB");
                break;
            case 21:
                addServiceInDB(con);
                audit(con, "added service to DB");
                break;
            case 22:
                updateServiceInDB(con);
                audit(con, "updated service in DB");
                break;
            case 23:
                deleteServiceFromDB(con);
                audit(con, "deleted service from DB");
                break;
            case 24:
                getAllServicesFromDB(con);
                audit(con, "loaded services from DB");
            case 25:
                addAppInDB(con);
                audit(con, "added appointment to DB");
                break;
            case 26:
                updateAppInDB(con);
                audit(con, "updated appointment in DB");
                break;
            case 27:
                deleteAppFromDB(con);
                audit(con, "deleted appointment from DB");
                break;
            case 28:
                getAllAppsFromDB(con);
                audit(con, "loaded appointments from DB");
                break;

            case 0:
                System.exit(0);
        }
    }

    void getAllClientsFromDB(ConnectionManager con) {
        ArrayList<Client> clients = clientService.getClientsFromDB(con);
        for (Client client : clients)
            System.out.println(client);
    }

    void deleteClientInDB(ConnectionManager con) {
        System.out.println("enter the id of the client to delete");
        int id = Integer.parseInt(s.nextLine());
        clientService.deleteClientFromDB(con, id);
        System.out.println("client deleted successfully in DB");
    }

    void updateClientInDB(ConnectionManager con) {
        try {
            System.out.println("enter the id of the client to update");
            System.out.println("id:");
            int id = Integer.parseInt(s.nextLine());
            Client client = readClient();
            clientService.updateClientInDB(con, id, client);
            System.out.println("client updated successfully in DB");
        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
        }
    }

    void addClientInDB(ConnectionManager con) {
        try {
            Client client = readClient();
            clientService.addClientToDB(con, client);
            clientRepository.add(client);
            System.out.println("client added successfully in DB");
        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
        }
    }

    void delClient(){
        try {
            System.out.println("enter the index of the client to delete:");
            int index = s.nextInt();
            s.nextLine();
            clientService.deleteClient(index);
        } catch (InvalidDataException e) {
            System.out.println("index out of bounds");
        }
    }

    void updtClient() {
        System.out.println("enter the index of the client to update");
        int index = s.nextInt();
        s.nextLine();
        Client existingClient = clientRepository.get(index);
        System.out.println(existingClient);
        System.out.println("what do you want to update? 1-lastname, 2-phone, 3-email, 4-birthdate");
        System.out.println("your option:");
        int option = s.nextInt();
        s.nextLine();
        System.out.println("enter updated client information:");
        switch (option) {
            case 1:
                System.out.print("name:");
                String name = s.nextLine();
                existingClient.setLastName(name);
                break;
            case 2:
                System.out.print("phone:");
                String phone = s.nextLine();
                existingClient.setPhoneNumber(phone);
                break;
            case 3:
                System.out.print("email:");
                String email = s.nextLine();
                existingClient.setEmail(email);
                break;
            case 4:
                try {
                    System.out.println("birthDate:");
                    System.out.println("Day: ");
                    int day = Integer.parseInt(s.nextLine());
                    System.out.println("Month: ");
                    int month = Integer.parseInt(s.nextLine());
                    System.out.println("Year: ");
                    int year = Integer.parseInt(s.nextLine());
                    Date birthday = new Date(day, month, year);
                    existingClient.setBirthDate(birthday);
                } catch (InvalidDataException e) {
                    System.out.println("invalid date");
                }
                break;
            default:
                System.out.println("invalid option");
                return;
        }
        clientRepository.update(index, existingClient);
        ClientCSVServices.getInstance().update(existingClient);
    }

    void printAllClients() {
        Vector<Client> clients = clientService.getAllClients();
        if (clients.size() == 0) {
            System.out.println("no clients found.");
        }
        for (Client client : clients)
            System.out.println(client);
    }

    void addClientToCSV() {
        try {
            clientService.addNewClient(readClient());
        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
        }
    }

    Client readClient() {
        try {
            System.out.print("First name: ");
            String firstName = s.nextLine();
            System.out.print("Last name: ");
            String lastName = s.nextLine();
            System.out.print("Phone: ");
            String phone = s.nextLine();
            System.out.print("Email: ");
            String email = s.nextLine();
            System.out.println("BirthDate: ");
            System.out.println("Day: ");
            int day = Integer.parseInt(s.nextLine());
            System.out.println("Month: ");
            int month = Integer.parseInt(s.nextLine());
            System.out.println("Year: ");
            int year = Integer.parseInt(s.nextLine());
            Date birthday = new Date(day, month, year);

            return new Client(firstName, lastName, phone, email, birthday);
        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    void getAllMedicsFromDB(ConnectionManager con) {
        ArrayList<Medic> medics = medicService.getMedicsFromDB(con);
        for (Medic medic : medics)
            System.out.println(medic);
    }

    void deleteMedicFromDB(ConnectionManager con) {
        try {
            System.out.println("enter the id of the medic to delete:");
            int id = Integer.parseInt(s.nextLine());
            medicService.deleteMedicInDB(con, id);
        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
        }
    }

    void updateMedicInDB(ConnectionManager con) {
        try {
            System.out.println("enter the id of the medic to update:");
            int id = Integer.parseInt(s.nextLine());
            Medic medic = readMedic();
            medicService.updateMedicInDB(con, id, medic);
        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
        }
    }

    void addMedicInDB(ConnectionManager con) {
        try {
            Medic medic = readMedic();
            medicService.addMedicToDB(con, medic);
            medicRepository.add(medic);
        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
        }
    }

    void printAllMedics() {
        Vector<Medic> medics = medicService.getAllMedics();
        if (medics.size() == 0) {
            System.out.println("no medics found");
        }
        for (Medic medic : medics)
            System.out.println(medic);
    }

    void addMedicToCSV() {
        try {
            medicService.addNewMedic(readMedic());
        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
        }
    }

    Medic readMedic() {
        try {
            System.out.print("First name: ");
            String firstName = s.nextLine();
            System.out.print("Last name: ");
            String lastName = s.nextLine();
            System.out.print("Phone: ");
            String phone = s.nextLine();
            System.out.print("Email: ");
            String email = s.nextLine();
            System.out.print("Speciality: ");
            String spec = s.nextLine();
            System.out.print("room index:");
            int roomId = Integer.parseInt(s.nextLine());

            Medic medic = new Medic(firstName, lastName, phone, email, Speciality.valueOf(spec), roomId);
            return medic;
        } catch (NumberFormatException ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    void getAllServicesFromDB(ConnectionManager con) {
        ArrayList<Service> services = serviceService.getALLServicesFromDB(con);
        for (Service service : services)
            System.out.println(service);
    }

    void deleteServiceFromDB(ConnectionManager con) {
        try {
            System.out.println("enter the id of the service to delete:");
            int id = Integer.parseInt(s.nextLine());
            serviceService.deleteServiceFromDB(con, id);
            System.out.println("service deleted successfully in DB");
        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
        }
    }

    void updateServiceInDB(ConnectionManager con) {
        try {
            System.out.println("enter the id of the service to update:");
            int id = Integer.parseInt(s.nextLine());
            Service service = readService();
            serviceService.updateServiceInDb(con, id, service);
            serviceRepository.update(id, service);
            System.out.println("service updated successfully in DB");
        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
        }
    }

    void addServiceInDB(ConnectionManager con) {
        try {
            Service service = readService();
            serviceService.addServiceToDB(con, service);
            serviceRepository.add(service);
            System.out.println("service added successfully in DB");
        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
        }
    }

    void printAllServices() {
        Vector<Service> services = serviceService.getAllServices();
        if (services.size() == 0) {
            System.out.println("no services found");
        }
        for(Service service : services)
            System.out.println(service);
    }

    void addServiceToCSV() {
        try {
            serviceService.addNewService(readService());
        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
        }
    }

    Service readService() {
        try {
            System.out.print("Name:");
            String name = s.nextLine();
            System.out.print("Price:");
            double price = Double.parseDouble(s.nextLine());
            System.out.print("Speciality:");
            String spec = s.nextLine();

            return new Service(name, price, Speciality.valueOf(spec));

        } catch (NumberFormatException numberFormat) {
            System.out.println("invalid price");
        }
        return null;
    }

    void getAllAppsFromDB(ConnectionManager con){
        ArrayList<Appointment> appointments = appService.getAppointmentFromDB(con);
        for (Appointment app : appointments)
            System.out.println(app);
    }

    void deleteAppFromDB(ConnectionManager con) {
        try {
            System.out.println("enter the id of the appointment to delete:");
            int appId = Integer.parseInt(s.nextLine());
            appService.deleteAppointmentFromDB(con, appId);
        } catch (NumberFormatException e){
            System.out.println(e.getMessage());
        }
    }

    void updateAppInDB(ConnectionManager con) {
        try {
            System.out.println("appointment id:");
            int appId = Integer.parseInt(s.nextLine());

            System.out.println("client id:");
            int clientId = Integer.parseInt(s.nextLine());

            System.out.println("medic id:");
            int medicId = Integer.parseInt(s.nextLine());

            System.out.println("service id:");
            int serviceId = Integer.parseInt(s.nextLine());

            System.out.println("date:");
            System.out.println("day:");
            int day = Integer.parseInt(s.nextLine());
            System.out.println("month:");
            int month = Integer.parseInt(s.nextLine());
            System.out.println("year:");
            int year = Integer.parseInt(s.nextLine());
            System.out.println("time:");
            System.out.println("hour:");
            int hour = Integer.parseInt(s.nextLine());
            System.out.println("minute:");
            int minute = Integer.parseInt(s.nextLine());
            DateTime dateTime = new DateTime(day, month, year, hour, minute);

            appService.updateAppointmentInDB(con, appId, clientId, medicId, serviceId, dateTime);
        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
        }
    }

    void addAppInDB(ConnectionManager con) {
        try {
            System.out.println("client id:");
            int clientId = Integer.parseInt(s.nextLine());

            System.out.println("medic id:");
            int medicId = Integer.parseInt(s.nextLine());

            System.out.println("service id:");
            int serviceId = Integer.parseInt(s.nextLine());

            System.out.println("date:");
            System.out.println("day:");
            int day = Integer.parseInt(s.nextLine());
            System.out.println("month:");
            int month = Integer.parseInt(s.nextLine());
            System.out.println("year:");
            int year = Integer.parseInt(s.nextLine());
            System.out.println("time:");
            System.out.println("hour:");
            int hour = Integer.parseInt(s.nextLine());
            System.out.println("minute:");
            int minute = Integer.parseInt(s.nextLine());
            DateTime dateTime = new DateTime(day, month, year, hour, minute);

            appService.addAppointmentToDB(con, clientId, medicId, serviceId, dateTime);
        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
        }
    }

    void updateAppointment() {
        System.out.println("what do you want to update? 1-date 2-service 3-medic");
        System.out.println("your option:");
        int option = s.nextInt();
        s.nextLine();
        System.out.println("appointment index:");
        int index = s.nextInt();
        s.nextLine();
        switch (option) {
            case 1:
                try {
                    System.out.println("date");
                    System.out.print("day:");
                    int day = s.nextInt();
                    System.out.print("month:");
                    int month = s.nextInt();
                    System.out.print("year:");
                    int year = s.nextInt();
                    System.out.println("time ");
                    System.out.print("hour:");
                    int hour = s.nextInt();
                    System.out.print("minute:");
                    int minute = s.nextInt();
                    DateTime date = new DateTime(day, month, year, hour, minute);
                    appService.updateDate(index, date);
                } catch (InvalidDataException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 2:
                System.out.println("service index");
                int serviceIndex = s.nextInt();
                s.nextLine();
                appService.updateService(index, serviceIndex);
                break;
            case 3:
                System.out.println("medic index:");
                int medicIndex = s.nextInt();
                s.nextLine();
                appService.updateMedic(index, medicIndex);
                break;
        }
    }

    void listAllAppointments() {
        System.out.println("filter appointments by 0-all, 1-client, 2-medic:");
        int option = s.nextInt();
        s.nextLine();
        switch (option){
            case 0:
                printAllAppointments();
                break;
            case 1:
                try {
                    System.out.print("please enter client index:");
                    int index = s.nextInt();
                    s.nextLine();
                    printAppointmentsByClient(index);
                } catch (InvalidDataException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 2:
                try {
                    System.out.println("please enter medic index:");
                    int index2 = s.nextInt();
                    s.nextLine();
                    printAppointmentsByMedic(index2);
                } catch (InvalidDataException e) {
                    System.out.println(e.getMessage());
                }
                break;
        }
    }

    void printAllAppointments(){
        Vector<Appointment> appointments = appService.getAllAppointments();
        if (appointments.size() == 0)
            System.out.println("no appointments found");
        for (Appointment appointment : appointments)
            System.out.println(appointment);
    }

    void printAppointmentsByClient(int index) throws InvalidDataException{
        Vector<Appointment> appointments = appService.getAppointmentsByClient(index);
        if (appointments.size() == 0)
            System.out.println("no appointments found");
        for (Appointment appointment : appointments)
            System.out.println(appointment);
    }

    void printAppointmentsByMedic(int index) throws InvalidDataException {
        Vector<Appointment> appointments = appService.getAppointmentsByMedic(index);
        if (appointments.size() == 0)
            System.out.println("no appointments found");
        for (Appointment appointment : appointments)
            System.out.println(appointment);
    }

    private void readAppointment() {
        try {
            System.out.print("client index: ");
            int clientIndex = Integer.parseInt(s.nextLine());

            System.out.println("medic index: ");
            int medicIndex = Integer.parseInt(s.nextLine());

            System.out.println("service index: ");
            int serviceIndex = Integer.parseInt(s.nextLine());

            System.out.println("Date");
            System.out.print("Day: ");
            int day = Integer.parseInt(s.nextLine());
            System.out.print("Month: ");
            int month = Integer.parseInt(s.nextLine());
            System.out.print("Year: ");
            int year = Integer.parseInt(s.nextLine());
            System.out.println("Time");
            System.out.print("Hour: ");
            int hour = Integer.parseInt(s.nextLine());
            System.out.print("Minute: ");
            int minute = Integer.parseInt(s.nextLine());
            DateTime date = new DateTime(day, month, year, hour, minute);

            appService.addAppointment(clientIndex, medicIndex, serviceIndex, date);

        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
        }
    }

}