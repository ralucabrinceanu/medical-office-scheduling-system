package services;

import domain.Client;
import domain.Date;
import exceptions.InvalidDataException;
import persistence.ClientRepository;
import services.csv.ClientCSVServices;
import services.db.ClientDBServices;
import services.db.ConnectionManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

public class ClientServices {
    private ClientRepository clientRepository = new ClientRepository();

    public Vector<Client> getAllClients() {
        Vector<Client> clients = new Vector<>();
        for(int i = 0; i < clientRepository.getSize(); i++)
            clients.add(clientRepository.get(i));
        return clients;
    }

    public void addNewClient(Client client) throws InvalidDataException {
        if(client == null)
            throw new InvalidDataException("client is null");

        Client newClient = new Client(client);
        clientRepository.add(newClient);
    }

    private boolean isValidClient(String firstName, String lastName, String phoneNumber, String email, Date birthDate) throws InvalidDataException {
        if(firstName == null || firstName.equals("") || !firstName.matches("[a-zA-Z]+") || lastName == null || lastName.equals("") || !lastName.matches("[a-zA-Z]+"))
            throw new InvalidDataException("invalid name");

        if(phoneNumber.length() != 10 || !phoneNumber.matches("^0\\d{9}$"))
            throw new InvalidDataException("phone number must be 10 digits long and starts with 0.");

        if(!email.matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-z]+"))
            throw new InvalidDataException("email is not valid.");

        return true;
    }

    public void addNewClient(String firstName, String lastName, String phoneNumber, String email, Date birthDate) throws InvalidDataException {

        if (isValidClient(firstName, lastName, phoneNumber, email, birthDate)) {
            Client newClient = new Client(firstName, lastName, phoneNumber, email, birthDate);
            clientRepository.add(newClient);
        }
    }

    public void updateClient(int index, Client client) throws InvalidDataException {
        if(index < 0 || index >= clientRepository.getSize())
            throw new InvalidDataException("index is out of bounds");
        clientRepository.update(index, client);
    }

    public void deleteClient(int index) throws InvalidDataException {
        if(index < 0 || index >= clientRepository.getSize())
            throw new InvalidDataException("index out of bounds");
        clientRepository.delete(index);
    }

    public void loadFromCSVFile(){
        ClientCSVServices csvFileService = ClientCSVServices.getInstance();
        Vector<Client> clients = new Vector<>(csvFileService.read());
        for(Client client : clients)
            clientRepository.add(client);
    }

    public void addClientToDB(ConnectionManager conn, Client client) throws InvalidDataException{
        if (client == null)
            throw new InvalidDataException("client is null");

        ClientDBServices dbService = new ClientDBServices(conn);
        if (isValidClient(client.getFirstName(), client.getLastName(), client.getPhoneNumber(),
                client.getEmail(), client.getBirthDate()))
            dbService.insertItem(client);
    }

    public ArrayList<Client> getClientsFromDB(ConnectionManager conn) {
        ClientDBServices dbService = new ClientDBServices(conn);
        ArrayList<Client> dbClients = dbService.getAllItems();
        return dbClients;
    }

    public void updateClientInDB(ConnectionManager conn, int id, Client client) throws InvalidDataException{
        ClientDBServices dbService = new ClientDBServices(conn);
        if (isValidClient(client.getFirstName(), client.getLastName(), client.getPhoneNumber(),
                client.getEmail(), client.getBirthDate()))
            dbService.updateItem(id, client);
    }


    public void deleteClientFromDB(ConnectionManager conn, int id) {
        ClientDBServices dbService = new ClientDBServices(conn);
        dbService.deleteItem(id);
    }
}