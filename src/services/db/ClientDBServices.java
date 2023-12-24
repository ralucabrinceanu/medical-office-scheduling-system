package services.db;

import domain.Client;
import domain.Date;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ClientDBServices implements GenericDBServices<Client> {
    private ConnectionManager connectionManager;

    public ClientDBServices(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void insertItem(Client item) {
        try {
            PreparedStatement stmt = connectionManager.prepareStatement(
                    "INSERT INTO clients VALUES (?, ?, ?, ?, ?, STR_TO_DATE(?, '%d/%m/%Y'))"
            );
            stmt.setInt(1, item.getId());
            stmt.setString(2, item.getFirstName());
            stmt.setString(3, item.getLastName());
            stmt.setString(4, item.getPhoneNumber());
            stmt.setString(5, item.getEmail());
            stmt.setString(6, item.getBirthDate().toString());
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateItem(int id, Client item) {
        try {
            PreparedStatement stmt = connectionManager.prepareStatement(
                    "UPDATE clients SET first_name = ?, last_name = ?, phone = ?, " +
                            "email = ?, birth_date = STR_TO_DATE(?, '%d/%m/%Y') " +
                            "WHERE id_client = ?"
            );
            stmt.setString(1, item.getFirstName());
            stmt.setString(2, item.getLastName());
            stmt.setString(3, item.getPhoneNumber());
            stmt.setString(4, item.getEmail());
            stmt.setString(5, item.getBirthDate().toString());
            stmt.setInt(6, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void deleteItem(int id) {
        try {
            PreparedStatement stmt = connectionManager.prepareStatement(
                    "DELETE FROM clients WHERE id_client = ?"
            );
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ArrayList<Client> getAllItems() {
        try {
            PreparedStatement stmt = connectionManager.prepareStatement(
                    "SELECT * FROM clients"
            );
            ResultSet res = stmt.executeQuery();
            ArrayList<Client> clients = new ArrayList<>();
            while(res.next()) {
                Client client = new Client();
                client.setId(res.getInt("id_client"));
                client.setFirstName(res.getString("first_name"));
                client.setLastName(res.getString("last_name"));
                client.setPhoneNumber(res.getString("phone"));
                client.setEmail(res.getString("email"));
                String date = res.getString("birth_date");
                String[] dateParts = date.split("-");
                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int day = Integer.parseInt(dateParts[2]);
                Date birthDate = new Date(day, month, year);
                client.setBirthDate(birthDate);

                clients.add(client);
            }
            return clients;
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
