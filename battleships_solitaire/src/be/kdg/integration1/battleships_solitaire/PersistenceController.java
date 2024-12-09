package be.kdg.integration1.battleships_solitaire;

import java.sql.*;

public class PersistenceController {

    public final String URL = "jdbc:postgresql://localhost:5432/";
    public final String TABLE = "ascii2";
    public final String USERNAME = "postgres";
    public final String PASSWORD = "Student_1234";

    private Connection connection;

    public PersistenceController() {
        try {
            connection = DriverManager.getConnection(URL + TABLE, USERNAME, PASSWORD);
            createTables();
            System.out.println("There are " + getGameCount() + " game(s) in the database;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // executes all create table statements needed
    private void createTables() {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS players (
                                        player_id INTEGER
                                        GENERATED ALWAYS AS IDENTITY
                                        CONSTRAINT pk_player_id PRIMARY KEY,
                                        name VARCHAR(12)
                                        CONSTRAINT nn_player_name NOT NULL
                                        CONSTRAINT uq_player_name UNIQUE
                                        CONSTRAINT ch_player_name
                                        CHECK ( name = UPPER(name) ),
                                        birthdate DATE
                                        CONSTRAINT nn_birthdate NOT NULL
                                        CONSTRAINT ch_birthdate
                                        CHECK ( birthdate < NOW() ),
                                        join_date DATE DEFAULT CURRENT_DATE
                                        CONSTRAINT nn_join_date NOT NULL)
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS games (
                                         game_id INTEGER
                                             GENERATED ALWAYS AS IDENTITY
                                             CONSTRAINT pk_game_id PRIMARY KEY,
                                         start TIMESTAMP(0)
                                             CONSTRAINT nn_start NOT NULL,
                                         "end" TIMESTAMP(0),
                                         duration INTERVAL(0)
                                             CONSTRAINT nn_duration NOT NULL,
                                         score NUMERIC(3)
                                             CONSTRAINT nn_score NOT NULL,
                                         board_size NUMERIC(2)
                                             CONSTRAINT nn_board_size NOT NULL
                                             CONSTRAINT ch_board_size
                                                 CHECK ( board_size IN (5, 7, 9) ),
                                         player_id INTEGER
                                             CONSTRAINT nn_player_id NOT NULL
                                             CONSTRAINT fk_player_id REFERENCES games(game_id)
                                             ON DELETE SET NULL)
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS tiles (
                                        tile_id INTEGER
                                            GENERATED ALWAYS AS IDENTITY
                                            CONSTRAINT pk_tile_id PRIMARY KEY,
                                        x NUMERIC(2)
                                            CONSTRAINT nn_x NOT NULL,
                                        y NUMERIC(2)
                                            CONSTRAINT nn_y NOT NULL,
                                        is_ship BOOLEAN,
                                        game_id INTEGER
                                            CONSTRAINT nn_game_id NOT NULL
                                            CONSTRAINT fk_tile_game_id REFERENCES games(game_id)
                                            ON DELETE CASCADE)
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS ship_types (
                                      ship_type_id INTEGER
                                          GENERATED ALWAYS AS IDENTITY
                                          CONSTRAINT pk_ship_type_id PRIMARY KEY,
                                      length NUMERIC(1)
                                          CONSTRAINT nn_length NOT NULL,
                                      name VARCHAR(12)
                                          CONSTRAINT nn_ship_name NOT NULL)
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS ships (
                                       ship_id INTEGER
                                           GENERATED ALWAYS AS IDENTITY
                                           CONSTRAINT pk_ship_id PRIMARY KEY,
                                       ship_type_id INTEGER
                                           CONSTRAINT nn_ship_type_id NOT NULL
                                           CONSTRAINT fk_ship_type_id REFERENCES ship_types(ship_type_id)
                                           ON DELETE SET NULL)
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS placements (
                        game_id INTEGER
                            CONSTRAINT nn_placement_game_id NOT NULL
                            CONSTRAINT fk_game_id REFERENCES games(game_id),
                        ship_id INTEGER
                            CONSTRAINT nn_placement_ship_id NOT NULL
                            CONSTRAINT fk_ship_id REFERENCES ships(ship_id),
                        x NUMERIC(2)
                            CONSTRAINT nn_ship_x NOT NULL,
                        y NUMERIC(2)
                            CONSTRAINT nn_ship_y NOT NULL,
                        is_vertical BOOLEAN,
                        CONSTRAINT pk_placements PRIMARY KEY (game_id, ship_id))
                    """);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void savePlayer(Player player) {
        try {
            String query = "INSERT INTO players (name, birthdate) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getName());
            statement.setDate(2, player.getBirthdate());
            statement.execute(query);
        } catch (SQLException e) {
            System.err.println(e.getMessage() + "\n-> Could not save player in the database!");
        }
    }

    public Player loadPlayer(String name) {
        return null;
    }

    public int getGameCount() {
        int gameCount = 0;
        String query = "SELECT COUNT(*) FROM games";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next())
                gameCount = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gameCount;
    }

}
