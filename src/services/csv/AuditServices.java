package services.csv;

import services.db.ConnectionManager;

import java.io.FileWriter;
import java.io.IOException;

public class AuditServices {
    public void write(ConnectionManager con, String action, String date) {
        try (FileWriter fileWriter = new FileWriter("files/audit.csv", true)){
            fileWriter.write(action + "," + date + "\n");
            fileWriter.flush();
        } catch (IOException ex) {
            System.out.println("error writing to file.");
        }
    }
}
