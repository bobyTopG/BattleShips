package be.kdg.integration1.battleships_solitaire.logic;

import be.kdg.integration1.battleships_solitaire.entities.Board;
import be.kdg.integration1.battleships_solitaire.entities.Player;
import be.kdg.integration1.battleships_solitaire.entities.Tile;
import be.kdg.integration1.battleships_solitaire.view.TerminalUIHandler;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The class {@code PersistenceController} deals with everything related to the database.
 * Here we can execute queries and fetch persistent data.
 * Sometimes it is also called {@code Repository} in other projects.
 */
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
            // System.out.println("There are " + getGameCount() + " saved game(s) in the database");
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
            statement.setDate(2, Date.valueOf(player.getBirthdate()));
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Could not save player in the database!");
        }
    }

    public Player fetchPlayer(String name) {
        try {
            name = name.toUpperCase();
            String query = "SELECT * FROM players WHERE name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Player(
                        resultSet.getInt("player_id"),
                        resultSet.getString("name"),
                        resultSet.getDate("birthdate").toLocalDate(),
                        resultSet.getDate("join_date").toLocalDate()
                );
            } else {
                // if there isn't such a Player yet, we just create it
                LocalDate birthdate = new TerminalUIHandler().askForBirthdate();
                savePlayer(new Player(name, birthdate));
                return fetchPlayer(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Could not load player from the database!");
        }
        return null;
    }

    public Player fetchPlayer(int id) {
        try {
            String query = "SELECT * FROM players WHERE player_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Player(
                        resultSet.getInt("player_id"),
                        resultSet.getString("name"),
                        resultSet.getDate("birthdate").toLocalDate(),
                        resultSet.getDate("join_date").toLocalDate()
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Could not load player from the database!");
        }
        return null;
    }

    public void saveGame(Player player, Board board) {
        try {
            /* Saving the game session */
            String gameQuery = "INSERT INTO games (player_id, board_size, score, start, duration) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(gameQuery);
            statement.setInt(1, player.getId());
            statement.setInt(2, board.getBoardSize());
            statement.setInt(3, 0); // TODO: score from the board?
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now())); // TODO: start from the board?
            statement.setString(5, "5 min"); // TODO: duration from the board?
            statement.executeUpdate();
            statement.close();
            /* Get the game back as it is in the database */
            // TODO: get the game from the db
            /* Saving the tiles of the board */
            String tilesQuery = "INSERT INTO tiles (game_id, x, y, is_ship) VALUES (?, ?, ?, ?);";
            statement = connection.prepareStatement(tilesQuery);
            for (Tile[] row : board.getPlayerTiles()) {
                for (Tile tile : row) {
                    statement.setInt(1, board.getId());
                    statement.setInt(2, tile.getX());
                    statement.setString(3, String.valueOf(tile.getCharY()));
                    if (tile.isShip() || tile.isWater()) {
                        statement.setBoolean(4, tile.isShip());
                    } else {
                        statement.setObject(4, null);
                    }
                    statement.executeUpdate();
                }
            }
            /* Saves ships to the board, too */
            // TODO: implement this last part, argh
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Could not save player in the database!");
        }
        // TODO:? we can try to separate the queries into different methods
    }

    public Board fetchGame(Player player) {
        Board board = null;
        /* TODO */
        return board;
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
