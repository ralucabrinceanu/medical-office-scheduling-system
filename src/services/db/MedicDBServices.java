package services.db;

import domain.Medic;
import domain.Room;
import domain.Speciality;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MedicDBServices implements GenericDBServices<Medic> {
    private ConnectionManager connectionManager;

    public MedicDBServices(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void insertItem(Medic item) {
        try {
            PreparedStatement stmt = connectionManager.prepareStatement(
              "INSERT INTO medics VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            stmt.setInt(1, item.getId());
            stmt.setString(2, item.getFirstName());
            stmt.setString(3, item.getLastName());
            stmt.setString(4, item.getPhoneNumber());
            stmt.setString(5, item.getEmail());
            stmt.setString(6, item.getSpeciality().name());
            stmt.setInt(7, item.getRoom().getNumber());
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void updateItem(int id, Medic item) {
        try {
            PreparedStatement stmt = connectionManager.prepareStatement(
                    "UPDATE medics SET first_name = ?, last_name = ?, phone = ?, " +
                            "email = ?, speciality = ?, id_cabinet = ? " +
                            "WHERE id_medic = ?"
            );
            stmt.setString(1, item.getFirstName());
            stmt.setString(2, item.getLastName());
            stmt.setString(3, item.getPhoneNumber());
            stmt.setString(4, item.getEmail());
            stmt.setString(5, item.getSpeciality().name());
            stmt.setInt(6, item.getRoom().getNumber());
            stmt.setInt(7, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deleteItem(int id) {
        try {
            PreparedStatement stmt = connectionManager.prepareStatement(
                    "DELETE FROM medics WHERE id_medic = ?"
            );
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public ArrayList<Medic> getAllItems() {
        try {
            PreparedStatement stmt = connectionManager.prepareStatement(
                    "SELECT * FROM medics"
            );
            ResultSet res = stmt.executeQuery();
            ArrayList<Medic> medics = new ArrayList<>();
            while (res.next()) {
                Medic medic = new Medic();
                medic.setId(res.getInt("id_medic"));
                medic.setFirstName(res.getString("first_name"));
                medic.setLastName(res.getString("last_name"));
                medic.setPhoneNumber(res.getString("phone"));
                medic.setEmail(res.getString("email"));
                medic.setSpeciality(Speciality.valueOf(res.getString("speciality")));
                Room room  = new Room();
                room.setNumber(res.getInt("id_cabinet"));
                medic.setRoom(room);

                medics.add(medic);
            }
            return medics;
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
