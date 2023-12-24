package services;

import domain.Service;
import domain.Speciality;
import exceptions.InvalidDataException;
import persistence.ServiceRepository;
import services.csv.ServiceCSVServices;
import services.db.ConnectionManager;
import services.db.ServiceDBServices;

import java.util.ArrayList;
import java.util.Vector;

public class ServiceServices {

    private ServiceRepository serviceRepository = new ServiceRepository();

    public Vector<Service> getAllServices() {
        Vector<Service> services = new Vector<>();
        for (int i = 0; i < serviceRepository.getSize(); i++) {
            services.add(serviceRepository.get(i));
        }
        return services;
    }

    private boolean isValidService(String name, double price, String spec) throws InvalidDataException {
        if(price < 0)
            throw new InvalidDataException("Invalid price!");
        if(name.equals(""))
            throw new InvalidDataException("Invalid name!");

        boolean ok = false;
        for(Speciality speciality : Speciality.values()) {
            if(speciality.toString().equals(spec)) { ok = true; }
        }
        if(!ok) {
            throw new InvalidDataException("Invalid speciality!");
        }
        return true;
    }

    public void addNewService(String name, double price, String spec) throws InvalidDataException{

        if(isValidService(name, price, spec)) {
            Speciality speciality = Speciality.valueOf(spec);
            Service service = new Service(name, price, speciality);
            serviceRepository.add(service);
        }
    }

    public void addNewService(Service service) throws InvalidDataException {
        serviceRepository.add(service);
    }

    public void loadFromCSVFile() {
        ServiceCSVServices csvFileService = ServiceCSVServices.getInstance();
        Vector<Service> services = new Vector<>(csvFileService.read());
        for (Service service : services)
            serviceRepository.add(service);
    }

    public void addServiceToDB(ConnectionManager con, Service service) throws InvalidDataException{
        ServiceDBServices dbService = new ServiceDBServices(con);
        if (isValidService(service.getName(), service.getPrice(), service.getSpeciality().name()))
            dbService.insertItem(service);
    }

    public void updateServiceInDb(ConnectionManager con, int id, Service service) throws InvalidDataException{
        ServiceDBServices dbService = new ServiceDBServices(con);
        if (isValidService(service.getName(), service.getPrice(), service.getSpeciality().name()))
            dbService.updateItem(id, service);
    }

    public void deleteServiceFromDB(ConnectionManager con, int id) throws InvalidDataException{
        ServiceDBServices dbServices = new ServiceDBServices(con);
        dbServices.deleteItem(id);
    }

    public ArrayList<Service> getALLServicesFromDB(ConnectionManager con) {
        ServiceDBServices dbService = new ServiceDBServices(con);
        ArrayList<Service> dbServices = dbService.getAllItems();
        return dbServices;
    }


}
