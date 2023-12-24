package services;

import domain.*;
import exceptions.InvalidDataException;
import persistence.AppointmentRepository;
import persistence.ClientRepository;
import persistence.MedicRepository;
import persistence.ServiceRepository;
import services.csv.AppointmentCSVServices;
import services.db.AppointmentDBServices;
import services.db.ConnectionManager;

import javax.management.ValueExp;
import java.util.ArrayList;
import java.util.Vector;

public class AppointmentServices {
    private AppointmentRepository appointmentRepository = new AppointmentRepository();
    private ClientRepository clientRepository = new ClientRepository();
    private ServiceRepository serviceRepository = new ServiceRepository();
    private MedicRepository medicRepository = new MedicRepository();

    public void addAppointment(Appointment appointment) throws InvalidDataException {
        if (appointment == null)
            throw new InvalidDataException("appointment is null");
        appointmentRepository.add(appointment);
    }

    public void addAppointment(int clientIndex, int medicIndex, int serviceIndex,
                               DateTime dateTime) throws InvalidDataException {
//        daca clientul/medicul/serviciul nu exista in repository, atunci nu poate fi creata programarea
        if (clientIndex < 0 || clientIndex >= clientRepository.getSize())
            throw new InvalidDataException("client index is invalid");
        if (medicIndex < 0 || medicIndex >= medicRepository.getSize())
            throw new InvalidDataException("medic index is invalid");
        if (serviceIndex < 0 || serviceIndex >= serviceRepository.getSize())
            throw new InvalidDataException("service index is invalid");

        Client client = clientRepository.get(clientIndex);
        Medic medic = medicRepository.get(medicIndex);
        Service service = serviceRepository.get(serviceIndex);

        if (medic.getSpeciality() != service.getSpeciality())
            throw new InvalidDataException("medic does not have the same speciality as the service");

        Appointment appointment = new Appointment(client, medic, service, dateTime);
        appointmentRepository.add(appointment);
    }

    public Vector<Appointment> getAllAppointments(){
        Vector<Appointment> result = new Vector<>();
        for (int i = 0; i < appointmentRepository.getSize(); i++)
            result.add(appointmentRepository.get(i));
        return result;
    }

//    programarile unui anumit client
    public Vector<Appointment> getAppointmentsByClient(int index) throws InvalidDataException {
        if (index < 0 || index >= clientRepository.getSize())
            throw new InvalidDataException("client index is invalid");

        Vector<Appointment> result = new Vector<>();
        for (int i = 0; i < appointmentRepository.getSize(); i++)
            if (appointmentRepository.get(i).getClient().equals(clientRepository.get(index)))
                result.add(appointmentRepository.get(i));
        return result;
    }

//    programarile unui anumit medic
    public Vector<Appointment> getAppointmentsByMedic(int index) throws InvalidDataException {
        if (index < 0 || index >= medicRepository.getSize())
            throw new InvalidDataException("medic index is invalid");

        Vector<Appointment> result = new Vector<>();
        for (int i = 0; i < appointmentRepository.getSize(); i++)
            if (appointmentRepository.get(i).getMedic().equals(medicRepository.get(index)))
                result.add(appointmentRepository.get(i));
        return result;
    }

    public boolean deleteAppointment(int index) {
        if (index >= 0 && index < appointmentRepository.getSize()){
            appointmentRepository.delete(index);
            return true;
        }
        return false;
    }

    public boolean updateMedic(int index, int medicIndex) {
        if(index < 0 || index >= appointmentRepository.getSize())
            return false;

        Appointment oldApp = new Appointment(appointmentRepository.get(index));
        Appointment newApp = new Appointment(oldApp); // evit modificarea directa + copie
        newApp.setMedic(medicRepository.get(medicIndex));
        appointmentRepository.delete(index);
        appointmentRepository.add(newApp);
        return true;
    }

    public boolean updateService(int index, int serviceIndex) {
        if (index < 0 || index >= serviceRepository.getSize())
            return false;

        Appointment oldApp = new Appointment(appointmentRepository.get(index));
        Appointment newApp = new Appointment(oldApp);
        newApp.setService(serviceRepository.get(serviceIndex));
        appointmentRepository.delete(index);
        appointmentRepository.add(newApp);
        return true;
    }

    public void updateDate(int index, DateTime date) throws InvalidDataException{
        if (index < 0 || index >= appointmentRepository.getSize())
            throw new InvalidDataException("appointment index is invalid");

        Appointment oldApp = new Appointment(appointmentRepository.get(index));
        Appointment newApp = new Appointment(oldApp);
        newApp.setDateTime(date);
        appointmentRepository.delete(index);

//        daca data se schimba inainte de ora 9 sau dupa ora 18, programarea ramane la fel
        if (date.getHour() < 9 || date.getHour() > 18) {
            appointmentRepository.add(oldApp);
            throw new InvalidDataException("the new data is invalid");
        }
        appointmentRepository.add(newApp);
    }

    public void loadFromCSVFile() {
        AppointmentCSVServices csvFileService = AppointmentCSVServices.getInstance();
        Vector<Appointment> appts = new Vector<>(csvFileService.read());
        for (Appointment app : appts) {
            appointmentRepository.add(app);
        }
    }

    public void addAppointmentToDB(ConnectionManager con, int clientId, int medicId,
                                   int serviceId, DateTime app_date) throws InvalidDataException {
        AppointmentDBServices dbService = new AppointmentDBServices(con);
        dbService.insertItem(clientId, medicId, serviceId, app_date.toString());
    }

    public void updateAppointmentInDB(ConnectionManager con, int appId, int clientId,
                                      int medicId, int serviceId, DateTime app_date) throws InvalidDataException{
        AppointmentDBServices dbService = new AppointmentDBServices(con);
        dbService.updateItem(appId, clientId, medicId, serviceId, app_date);
    }

    public void deleteAppointmentFromDB(ConnectionManager con, int appId) {
        AppointmentDBServices dbService = new AppointmentDBServices(con);
        dbService.deleteItem(appId);
    }

    public ArrayList<Appointment> getAppointmentFromDB(ConnectionManager con) {
        AppointmentDBServices dbServices = new AppointmentDBServices(con);
        ArrayList<Appointment> dbApp =  dbServices.getAllItems();
        return dbApp;
    }
}
