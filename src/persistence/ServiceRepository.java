package persistence;

import domain.Service;
import services.csv.GenericCSVServices;
import services.csv.ServiceCSVServices;

import java.io.IOException;
import java.util.Vector;

public class ServiceRepository implements GenericRepository<Service> {

    public static final Vector<Service> storage = new Vector<Service>();

    @Override
    public void add(Service entity) {
        storage.add(entity);
        ServiceCSVServices.getInstance().write(entity);
    }

    @Override
    public Service get(int index) {
        return storage.get(index);
    }

    @Override
    public void update(int index, Service entity) {
        storage.set(index, entity);
    }

    @Override
    public void delete(int index) {
        Service service =  storage.remove(index);
        ServiceCSVServices.getInstance().deleteService(service);
    }

    @Override
    public int getSize() {
        return storage.size();
    }

    public boolean serviceExistsInRepo(Service service) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).equals(service))
                return true;
        }
        return false;
    }
}
