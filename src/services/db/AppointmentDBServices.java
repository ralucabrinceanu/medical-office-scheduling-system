package services.db;

import domain.*;
import exceptions.InvalidDataException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AppointmentDBServices implements GenericDBServices<Appointment> {
    private ConnectionManager connectionManager;

    public AppointmentDBServices(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void insertItem(Appointment item) {

    }

    public void insertItem(int clientId, int medicId, int serviceId, String dateTime) {
        try {
            PreparedStatement stmt = connectionManager.prepareStatement(
                    "INSERT INTO appointments " +
                            "VALUES (null, ?, ?, ?, STR_TO_DATE(?, '%d/%m/%Y %H:%i'))"
            );
            stmt.setInt(1, clientId);
            stmt.setInt(2, medicId);
            stmt.setInt(3, serviceId);
            stmt.setString(4, dateTime);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateItem(int id, Appointment item) {
        try {
            PreparedStatement stmt = connectionManager.prepareStatement(
                    "UPDATE appointments SET id_client = ?, id_medic = ?, " +
                            "id_serv = ?, app_date = STR_TO_DATE(?, '%d/%m/%Y %H:%i') " +
                            "WHERE id_app = ?"
            );
            stmt.setInt(1, item.getClient().getId());
            stmt.setInt(2, item.getMedic().getId());
            stmt.setInt(3, item.getService().getId());
            stmt.setString(4, item.getDateTime().toString());
            stmt.setInt(5, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateItem(int id, int clientId, int medicId, int serviceId, DateTime dateTime) {
        try {
            PreparedStatement stmt = connectionManager.prepareStatement(
                    "UPDATE appointments SET id_client = ?, id_medic = ?, " +
                            "id_serv = ?, app_date = STR_TO_DATE(?, '%d/%m/%Y %H:%i') " +
                            "WHERE id_app = ?"
            );
            stmt.setInt(1, clientId);
            stmt.setInt(2, medicId);
            stmt.setInt(3, serviceId);
            stmt.setString(4, dateTime.toString());
            stmt.setInt(5, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteItem(int id) {
        try {
            PreparedStatement stmt = connectionManager.prepareStatement(
                    "DELETE FROM appointments WHERE id_app = ?"
            );
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public ArrayList<Appointment> getAllItems() {
        try {
            PreparedStatement stmt = connectionManager.prepareStatement(
                    "SELECT * FROM appointments"
            );

            ResultSet res = stmt.executeQuery();
            ArrayList<Appointment> appointments = new ArrayList<>();

            ClientDBServices clientDBServices = new ClientDBServices(connectionManager);
            ArrayList<Client> clients = clientDBServices.getAllItems();

            MedicDBServices medicDBServices = new MedicDBServices(connectionManager);
            ArrayList<Medic> medics = medicDBServices.getAllItems();

            ServiceDBServices serviceDBServices = new ServiceDBServices(connectionManager);
            ArrayList<Service> services = serviceDBServices.getAllItems();

            while (res.next()) {
                Appointment appointment = new Appointment();

                for (Client client : clients)
                    if (client.getId() == res.getInt("id_client"))
                        appointment.setClient(client);

                for (Medic medic : medics)
                    if (medic.getId() == res.getInt("id_medic"))
                        appointment.setMedic(medic);

                for (Service service : services)
                    if (service.getId() == res.getInt("id_serv"))
                        appointment.setService(service);


                String date = res.getString("app_date");
                String[] dateParts = date.split("-");
                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                String[] timeParts = dateParts[2].split(" ");
                int day = Integer.parseInt(timeParts[0]);
                String[] time = timeParts[1].split(":");
                int hour = Integer.parseInt(time[0]);
                int minute = Integer.parseInt(time[1]);
                DateTime app_date = new DateTime(day, month, year, hour, minute);
                appointment.setDateTime(app_date);

                appointments.add(appointment);
            }
            return appointments;
        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        } catch (InvalidDataException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
