package services.csv;

import domain.Service;
import domain.Speciality;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class ServiceCSVServices implements GenericCSVServices<Service> {
    private static final ServiceCSVServices INSTANCE = new ServiceCSVServices();
    private ServiceCSVServices(){}
    public static ServiceCSVServices getInstance() {
        return INSTANCE;
    }

//    @Override
//    public void write(Service object) {
//        try (FileWriter fileWriter = new FileWriter("files/services.csv", true)){
//            fileWriter.write(object.getId() + ", " + object.getName() + "," +
//                    object.getPrice() + "," + object.getSpeciality().name() + "\n");
//            fileWriter.flush();
//        } catch (IOException ex) {
//            System.out.println("ServiceCSVServices: error writing to file.");
//        }
//    }

    @Override
    public void write(Service object) {
        Vector<Service> existingService =read();

        boolean serviceExists = false;
        for (Service service : existingService) {
            if (service.getId() == object.getId()) {
                serviceExists = true;
                break;
            }
        }

        if(!serviceExists) {
            try (FileWriter fileWriter = new FileWriter("files/services.csv", true)){
                fileWriter.write(object.getId() + ", " + object.getName() + "," +
                        object.getPrice() + "," + object.getSpeciality().name() + "\n");
                fileWriter.flush();
            } catch (IOException ex) {
                System.out.println("ServiceCSVServices: error writing to file.");
            }
        }

    }

    @Override
    public Vector<Service> read() {
        Vector<Service> services = new Vector<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("files/services.csv"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(",");
                Service service = new Service(tokens[1], Double.parseDouble(tokens[2]),
                        Speciality.valueOf(tokens[3]));
                service.setId(Integer.parseInt(tokens[0]));
                services.add(service);
            }
        } catch (IOException ex) {
            System.out.println("ServiceCSVServices: error reading from file.");
        }
//        services.clear();
        return services;
    }

    public void deleteService(Service service) {
        Vector<Service> services = read();

        boolean removed = services.removeIf(a -> a.getId() == service.getId());
        if (removed) {
            try (FileWriter fileWriter = new FileWriter("files/services.csv, false")) {
                for (Service a : services)
                {
                    write(a);
                }
            } catch (IOException ex) {
                System.out.println("ServiceCSVServices: error writing to file");
            }
        } else {
            System.out.println("service not found for deletion");
        }
    }

}
