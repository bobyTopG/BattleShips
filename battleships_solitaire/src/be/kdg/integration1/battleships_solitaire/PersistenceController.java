package be.kdg.integration1.battleships_solitaire;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PersistenceController {
    private Connection connection;

    public PersistenceController() {
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/battleships", "postgres", "Student_1234");
            System.out.println("Connection available!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}



