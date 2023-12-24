package persistence;

import domain.Appointment;
import domain.DateTime;
import domain.Medic;
import domain.Service;
import services.csv.AppointmentCSVServices;

import java.util.Date;
import java.util.Vector;

public class AppointmentRepository implements GenericRepository<Appointment> {

    private static final Vector<Appointment> storage = new Vector<>();

    @Override
    public void add(Appointment entity) {
        storage.add(entity);
        AppointmentCSVServices.getInstance().write(entity);
    }

    @Override
    public Appointment get(int index) {
        return storage.get(index);
    }

    @Override
    public void update(int index, Appointment entity) {
        storage.set(index, entity);
    }

    public void updateDate(int index, DateTime dateTime) {
        storage.get(index).setDateTime(dateTime);
        AppointmentCSVServices.getInstance().update(storage.get(index));
    }

    public void updateService(int index, Service service) {
        storage.get(index).setService(service);
    }

    public void updateMedic(int index, Medic medic) {
        storage.get(index).setMedic(medic);
    }

    @Override
    public void delete(int index) {
        Appointment delApp = storage.remove(index);
        AppointmentCSVServices.getInstance().delete(delApp);
    }

    @Override
    public int getSize() {
        return storage.size();
    }
}
