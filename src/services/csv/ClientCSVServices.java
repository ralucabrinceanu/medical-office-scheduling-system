package services.csv;

import domain.Appointment;
import domain.Client;
import domain.Date;
import domain.Service;
import exceptions.InvalidDataException;
import persistence.ClientRepository;
import persistence.GenericRepository;

import java.io.*;
import java.util.HashSet;
import java.util.Vector;

public class ClientCSVServices implements GenericCSVServices<Client> {
    private static final ClientCSVServices INSTANCE = new ClientCSVServices();
    public ClientCSVServices() {}
    public static ClientCSVServices getInstance(){
        return INSTANCE;
    }

//    @Override
//    public void write(Client object) {
//        try(FileWriter fileWriter = new FileWriter("files/clients.csv", true)) {
//            fileWriter.write( object.getId() + "," + object.getFirstName() + "," +
//                    object.getLastName() + "," + object.getPhoneNumber() + "," +
//                    object.getEmail() + "," + object.getBirthDate().getDay() + "," +
//                    object.getBirthDate().getMonth() + "," +
//                    object.getBirthDate().getYear() + "\n");
//            fileWriter.flush();
//        } catch (IOException ex) {
//            System.out.println("ClientCSVServices: error writing to file");
//        }
//    }

    @Override
    public void write(Client object) {
        Vector<Client> existingClients = read();

        boolean cientExists = false;
        for (Client client : existingClients) {
            if (client.getId() == object.getId()) {
                cientExists = true;
                break;
            }
        }

        if(!cientExists) {
            try (FileWriter fileWriter = new FileWriter("files/clients.csv", true)){
                fileWriter.write( object.getId() + "," + object.getFirstName() + "," +
                    object.getLastName() + "," + object.getPhoneNumber() + "," +
                    object.getEmail() + "," + object.getBirthDate().getDay() + "," +
                    object.getBirthDate().getMonth() + "," +
                    object.getBirthDate().getYear() + "\n");
                fileWriter.flush();
            } catch (IOException ex) {
                System.out.println("ClientCSVServices: error writing to file.");
            }
        }

    }

    @Override
    public Vector<Client> read(){
        Vector<Client> clients = new Vector<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader("files/clients.csv"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] tokens = line.split(",");
                Date date = new Date(Integer.parseInt(tokens[5]), Integer.parseInt(tokens[6]), Integer.parseInt(tokens[7]));
                Client client = new Client(tokens[1], tokens[2], tokens[3], tokens[4], date);
                client.setId(Integer.parseInt(tokens[0]));
                clients.add(client);
            }
        } catch (IOException ex) {
            System.out.println("ClientCSVServices: error reading from file");
        } catch (InvalidDataException ex) {
            System.out.println("invalid date");
        }
        return clients;
    }

    public void update(Client updatedClient) {
        Vector<Client> existingClients= read();

        try (FileWriter fileWriter = new FileWriter("files/clients.csv")) {
            for (Client clnt : existingClients) {
                if (clnt.getId() == updatedClient.getId()) {
                    clnt.setFirstName(updatedClient.getFirstName());
                    clnt.setLastName(updatedClient.getLastName());
                    clnt.setPhoneNumber(updatedClient.getPhoneNumber());
                    clnt.setEmail(updatedClient.getEmail());
                    clnt.setBirthDate(updatedClient.getBirthDate());

                    fileWriter.write(clnt.getId() + "," + clnt.getFirstName() + "," +
                            clnt.getLastName() + "," + clnt.getPhoneNumber() + "," +
                            clnt.getEmail() + "," + clnt.getBirthDate().getDay() + "," +
                            clnt.getBirthDate().getMonth() + "," +
                            clnt.getBirthDate().getYear() + "\n");
                } else {
                    fileWriter.write( clnt.getId() + "," + clnt.getFirstName() + "," +
                            clnt.getLastName() + "," + clnt.getPhoneNumber() + "," +
                            clnt.getEmail() + "," + clnt.getBirthDate().getDay() + "," +
                            clnt.getBirthDate().getMonth() + "," +
                            clnt.getBirthDate().getYear() + "\n");
                }
            }
            fileWriter.flush();
        } catch (IOException ex) {
            System.out.println("ClientCSVServices: error writing to file - update client");
        }
    }

    public void deleteClient(Client client) {
        Vector<Client> clients = read();

        boolean removed = clients.removeIf(a -> a.getId() == client.getId());
        if(removed) {
            try(FileWriter fileWriter = new FileWriter("files/clients.csv", false)) {
                for(Client a : clients) {
                    write(a);
                }
            } catch (IOException ex) {
                System.out.println("ClientCSVServices: error writing to file");
            }
        } else {
            System.out.println("client not found for deletion");
        }

    }


}