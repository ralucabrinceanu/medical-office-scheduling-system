package persistence;

import domain.Appointment;
import domain.Client;
import services.csv.AppointmentCSVServices;
import services.csv.ClientCSVServices;

import java.util.Vector;

public class ClientRepository implements GenericRepository<Client> {
    private static final Vector<Client> storage = new Vector<Client>();

    @Override
    public void add(Client entity) {
        storage.add(entity);
        ClientCSVServices.getInstance().write(entity);
    }

    @Override
    public Client get(int index) {
        return storage.get(index);
    }

    @Override
    public void update(int index, Client entity) {
        storage.set(index, entity);
    }

    @Override
    public void delete(int index) {
        Client delClient = storage.remove(index);
        ClientCSVServices.getInstance().deleteClient(delClient);
    }

    @Override
    public int getSize() {
        return storage.size();
    }

    public boolean ClientExistsInRepo(Client client) {
        for(int i = 0; i < storage.size(); i++) {
            if(storage.get(i).equals(client))
                return true;
        }
        return false;
    }
}
