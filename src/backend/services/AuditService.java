package backend.services;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class AuditService {
    private static final String FILE_NAME = "audit.csv";
    private static AuditService instance;

    private AuditService() {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write("operation,table,timestamp\n"); // Header, written only once ideally
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }

    public void log(String operation, String table) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write(operation + "," + table + "," + LocalDateTime.now() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
