package services.db;

import domain.Service;
import domain.Speciality;

import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceDBServices implements GenericDBServices<Service> {
    private ConnectionManager connectionManager;

    public ServiceDBServices(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void insertItem(Service item) {
        try {
            PreparedStatement stmt = connectionManager.prepareStatement(
                    "INSERT INTO services VALUES (?, ?, ?, ?)"
            );
            stmt.setInt(1, item.getId());
            stmt.setString(2, item.getName());
            stmt.setDouble(3, item.getPrice());
            stmt.setString(4, item.getSpeciality().name());
            stmt.executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void updateItem(int id, Service item) {
        try {
            PreparedStatement stmt = connectionManager.prepareStatement(
                    "UPDATE services SET name_serv = ?, price = ?, speciality = ? WHERE id_serv = ?"
            );
            stmt.setString(1, item.getName());
            stmt.setDouble(2, item.getPrice());
            stmt.setString(3, item.getSpeciality().name());
            stmt.setInt(4, id);
            stmt.executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void deleteItem(int id) {
        try {
            PreparedStatement stmt = connectionManager.prepareStatement(
              "DELETE FROM services WHERE id_serv = ?"
            );
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public ArrayList<Service> getAllItems() {
        try {
            PreparedStatement stmt = connectionManager.prepareStatement(
                    "SELECT * FROM services"
            );
            ResultSet res = stmt.executeQuery();
            ArrayList<Service> services = new ArrayList<>();
            while (res.next()) {
                Service service = new Service();
                service.setId(res.getInt("id_serv"));
                service.setName(res.getString("name_serv"));
                service.setPrice(res.getDouble("price"));
                String spec = res.getString("speciality");
                service.setSpeciality(Speciality.valueOf(spec));
                services.add(service);
            }
            return services;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
}
