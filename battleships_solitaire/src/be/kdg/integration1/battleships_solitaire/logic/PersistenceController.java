package be.kdg.integration1.battleships_solitaire.logic;

import be.kdg.integration1.battleships_solitaire.entities.*;
import be.kdg.integration1.battleships_solitaire.view.TerminalUIHandler;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The class {@code PersistenceController} deals with everything related to the database.
 * Here we can execute queries and fetch persistent data.
 * Sometimes it is also called {@code Repository} in other projects.
 */
public class PersistenceController {

    public final boolean USE_SERVER_DATABASE = true;
    public final String LOCAL_URL = "jdbc:postgresql://localhost:5432/";
    public final String SERVER_URL = "jdbc:postgresql://10.134.177.18:5432/";
    public final String TABLE = "ascii2";
    public final String USERNAME = "postgres";
    public final String PASSWORD = "Student_1234";

    private static Connection connection;
    private Leaderboard leaderboard = new Leaderboard();

    public PersistenceController() {
        connectRemoteDatabase();
        createTables();
        insertShipTypes();
    }

    private void connectRemoteDatabase() {
        if (connection != null) return;
        if (USE_SERVER_DATABASE) {
            try {
                connection = DriverManager.getConnection(SERVER_URL + TABLE, USERNAME, PASSWORD);
            } catch (SQLException e1) {
                System.err.println("Couldn't connect to the remote database!");
                connectLocalDatabase();
            }
        } else {
            connectLocalDatabase();
        }
    }

    private void connectLocalDatabase() {
        try {
            connection = DriverManager.getConnection(LOCAL_URL + TABLE, USERNAME, PASSWORD);
        } catch (SQLException e2) {
            System.err.println("Couldn't connect to a local database!");
            System.exit(1);
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
                        CONSTRAINT nn_join_date NOT NULL);
                        ALTER TABLE players
                        ADD COLUMN IF NOT EXISTS password VARCHAR(44);
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS games (
                         game_id    INTEGER
                         GENERATED ALWAYS AS IDENTITY
                         CONSTRAINT pk_game_id PRIMARY KEY,
                         start      TIMESTAMP(0)
                         CONSTRAINT nn_start NOT NULL,
                         "end"      TIMESTAMP(0),
                         duration   INTERVAL(0)
                         CONSTRAINT nn_duration NOT NULL,
                         score      NUMERIC(3)
                         CONSTRAINT nn_score NOT NULL,
                         board_size NUMERIC(2)
                         CONSTRAINT nn_board_size NOT NULL
                         CONSTRAINT ch_board_size
                         CHECK ( board_size IN (5, 7, 9) ),
                         player_id  INTEGER
                         CONSTRAINT nn_player_id NOT NULL
                         CONSTRAINT fk_player_id REFERENCES players (player_id))
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS tiles (
                        tile_id INTEGER
                        GENERATED ALWAYS AS IDENTITY
                        CONSTRAINT pk_tile_id PRIMARY KEY,
                        x       NUMERIC(2)
                        CONSTRAINT nn_x NOT NULL,
                        y       CHAR(1)
                        CONSTRAINT nn_y NOT NULL
                        CONSTRAINT ch_y CHECK ( y = UPPER(y) ),
                        is_ship BOOLEAN,
                        is_revealed BOOLEAN
                        CONSTRAINT nn_is_revealed NOT NULL,
                        game_id INTEGER
                        CONSTRAINT nn_game_id NOT NULL
                        CONSTRAINT fk_tile_game_id REFERENCES games (game_id)
                        ON DELETE CASCADE,
                        CONSTRAINT uq_tile UNIQUE (tile_id, x, y))
                    """);
            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS ship_types (
                          ship_type_id INTEGER
                              GENERATED ALWAYS AS IDENTITY
                              CONSTRAINT pk_ship_type_id PRIMARY KEY,
                          length NUMERIC(1)
                              CONSTRAINT nn_length NOT NULL,
                          name VARCHAR(12)
                              CONSTRAINT nn_ship_name NOT NULL,
                          CONSTRAINT uq_ship UNIQUE (length, name))
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
                            CONSTRAINT fk_game_id REFERENCES games(game_id)
                                 ON DELETE CASCADE,
                        ship_id INTEGER
                            CONSTRAINT nn_placement_ship_id NOT NULL
                            CONSTRAINT fk_ship_id REFERENCES ships(ship_id),
                        x NUMERIC(2)
                            CONSTRAINT nn_ship_x NOT NULL,
                        y CHAR(1)
                            CONSTRAINT nn_ship_y NOT NULL,
                        is_vertical BOOLEAN,
                        CONSTRAINT pk_placements PRIMARY KEY (game_id, ship_id))
                    
                    """);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertShipTypes(){
        try {
            String query = "INSERT INTO ship_types (length, name) VALUES (?, ?) ON CONFLICT (length, name) DO NOTHING";
            for (Ship.Type shipType : Ship.Type.values() ) {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, shipType.getSize());
                statement.setString(2, shipType.name());
                statement.executeUpdate();
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Ship Types could not be added to the database!");
        }
    }

    public void savePlayer(Player player) {
        try {
            String query = "INSERT INTO players (name, birthdate, password) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, player.getName());
            statement.setDate(2, Date.valueOf(player.getBirthdate()));
            statement.setString(3, player.getPassword());
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
                        resultSet.getDate("join_date").toLocalDate(),
                        resultSet.getString("password")
                );
            } else {
                // if there isn't such a Player yet, we just create it
                LocalDate birthdate = new TerminalUIHandler().askForBirthdate();
                String password = new TerminalUIHandler().askForNewPassword();
                if (password != null) {
                    password = Utility.hashPassword(password);
                }
                savePlayer(new Player(name, birthdate, password));
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
                        resultSet.getDate("join_date").toLocalDate(),
                        resultSet.getString("password")
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
        checkId(player);
        removeUnfinishedGames(player);
        if (board.getId() < 0) {
            saveNewGame(player, board);
        } else {
            removeGame(board);
            saveNewGame(player, board);
        }
    }

    public void removeUnfinishedGames(Player player) {
        checkId(player);
        try {
            String query = "DELETE FROM games WHERE player_id = ? AND \"end\" IS NULL;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, player.getId());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            System.err.println("Could not remove unfinished games of player with id [" + player.getId() + "]!");
        }
    }

    public void saveNewGame(Player player, Board board) {
        checkId(player);
        try {
            // a special INSERT query which also returns a value, in this case the new ID it saved the game with
            String query = "INSERT INTO games (player_id, board_size, score, start, \"end\", duration) VALUES (?, ?, ?, ?, ?, ?) RETURNING game_id;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, player.getId());
            statement.setInt(2, board.getBoardSize());
            statement.setInt(3, board.getScore());
            statement.setTimestamp(4, Timestamp.valueOf(board.getStartTime()));
            statement.setTimestamp(5, board.getEndTime() == null ? null : Timestamp.valueOf(board.getEndTime()));
            statement.setObject(6, Utility.convert(board.getDuration()));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) { // important!
                board.setId(resultSet.getInt("game_id"));
            } else {
                throw new SQLException();
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println("Could not save game in the database!");
        }
        saveTiles(board);
        saveShips(board);
    }

    private void saveTiles(Board board) {
        checkId(board);
        try {
            /* Saving the tiles of the board */
            String tilesQuery = "INSERT INTO tiles (game_id, x, y, is_ship, is_revealed) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement statement = connection.prepareStatement(tilesQuery);
            for (PlayerTile[] row : board.getPlayerTiles()) {
                for (PlayerTile tile : row) {
                    statement.setInt(1, board.getId());
                    statement.setInt(2, tile.getX());
                    statement.setString(3, String.valueOf(tile.getCharY()));
                    if (tile.isShip() || tile.isWater()) {
                        statement.setObject(4, tile.isShip());
                    } else {
                        statement.setObject(4, null);
                    }
                    statement.setBoolean(5, tile.isRevealed());
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.err.println("Could not save tiles in the database!");
            e.printStackTrace();
        }
    }

    private void saveShips(Board board) {
        checkId(board);
        try {
            for (Ship ship : board.getShips()) {
                String shipQuery = "INSERT INTO ships (ship_type_id) VALUES ((SELECT ship_type_id FROM ship_types WHERE length = ?)) RETURNING ship_id;";
                PreparedStatement statement = connection.prepareStatement(shipQuery);
                statement.setInt(1, ship.getSize());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    ship.setId(resultSet.getInt("ship_id"));
                    String placementQuery = "INSERT INTO placements (game_id, ship_id, x, y, is_vertical) VALUES (?, ?, ?, ?, ?);";
                    statement = connection.prepareStatement(placementQuery);
                    statement.setInt(1, board.getId());
                    statement.setInt(2, ship.getId());
                    statement.setInt(3, ship.getX());
                    statement.setString(4, String.valueOf(ship.getCharY()));
                    statement.setBoolean(5, ship.isVertical());
                    statement.executeUpdate();
                    statement.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Could not save ships in the database!");
        }
    }

    public void removeGame(Board board) {
        checkId(board);
        try {
            String query = "DELETE FROM games WHERE game_id = ? ;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, board.getId());
            statement.executeUpdate();
            statement.close();

            /* Removing the ships (no possible cascade*, triggers* possible) */
            List<Integer> shipIds = new ArrayList<>();
            for (Ship ship : board.getShips() ) {
                shipIds.add(ship.getId());
            }

            String shipsQuery = "DELETE FROM ships WHERE ship_id = ANY (?);";
            statement = connection.prepareStatement(shipsQuery);

            Array shipIdsArray = statement.getConnection().createArrayOf("NUMERIC", shipIds.toArray());
            statement.setArray(1, shipIdsArray);
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            System.err.println("Could not update game in the database!");
        }
    }

    private void checkId(Object o) {
        int id = switch (o) {
            case Player p -> p.getId();
            case Board b -> b.getId();
            default -> throw new IllegalStateException("Unexpected value: " + o.getClass().getSimpleName());
        };
        if (id < 0) {
            throw new IllegalArgumentException("ID cannot be negative!");
        }
    }

    public Board fetchGame(Player player) {
        try {
            // first getting the game itself
            String gameQuery = "SELECT game_id, board_size, score, start, EXTRACT(EPOCH FROM duration) AS duration FROM games WHERE player_id = ? AND \"end\" IS NULL;";
            PreparedStatement statement = connection.prepareStatement(gameQuery);
            statement.setInt(1, player.getId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int gameId = resultSet.getInt("game_id");
                int boardSize = resultSet.getInt("board_size");
                int score = resultSet.getInt("score");
                LocalDateTime start = resultSet.getTimestamp("start").toLocalDateTime();
                Duration duration = Duration.ofSeconds(resultSet.getInt("duration"));
                List<PlayerTile> tiles = fetchTiles(gameId);
                List<Ship> ships = fetchShips(gameId);
                return new Board(
                        gameId, boardSize, score,
                        tiles, ships,
                        start, duration
                );
            }
        } catch (SQLException e) {
            System.err.println("Could not fetch game from the database!");
        }
        return null;
    }

    private List<PlayerTile> fetchTiles(int gameId) {
        List<PlayerTile> tiles = new ArrayList<>();
        try {
            String tileQuery = "SELECT x, y, is_ship, is_revealed FROM tiles WHERE game_id = ?;";
            PreparedStatement statement = connection.prepareStatement(tileQuery);
            statement.setInt(1, gameId);
            ResultSet resultSet = statement.executeQuery();

            int x;
            char y;
            Tile.Type type;
            Boolean isShip;
            boolean revealed;

            while (resultSet.next()) {
                x = resultSet.getInt("x");
                y = resultSet.getString("y").charAt(0);
                revealed = resultSet.getBoolean("is_revealed");
                isShip = resultSet.getObject("is_ship", Boolean.class);
                type = isShip == null ? null : isShip ? Tile.Type.SHIP_PART : Tile.Type.WATER;
                tiles.add(new PlayerTile(x, y, revealed, type));
            }
        } catch (SQLException e) {
            System.err.println("Could not fetch tiles from the database!");
            e.printStackTrace();
        }
        return tiles;
    }

    private List<Ship> fetchShips(int gameId) {
        List<Ship> ships = new ArrayList<>();
        try {
            String tileQuery = "SELECT x, y, length, is_vertical " +
                    "FROM placements p " +
                    "JOIN ships s ON p.ship_id = s.ship_id " +
                    "JOIN ship_types st ON s.ship_type_id = st.ship_type_id " +
                    "WHERE game_id = ?";
            PreparedStatement statement = connection.prepareStatement(tileQuery);
            statement.setInt(1, gameId);
            ResultSet resultSet = statement.executeQuery();

            int x;
            char y;
            int size;
            boolean isVertical;

            while (resultSet.next()) {
                x = resultSet.getInt("x");
                y = resultSet.getString("y").charAt(0);
                size = resultSet.getInt("length");
                isVertical = resultSet.getBoolean("is_vertical");
                ships.add(new Ship(x, y, size, isVertical));
            }
        } catch (SQLException e) {
            System.err.println("Could not fetch ships from the database!");
        }
        return ships;
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

    // give resultset for fetch methods of the leaderboard
    public void fetchFromLeaderBoard(PreparedStatement statement) {
        this.leaderboard = new Leaderboard();
        try {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String playerName = resultSet.getString(1);
                Date gameDate = resultSet.getDate(2);
                String duration = resultSet.getString(3);
                Difficulty difficulty = Difficulty.valueOf(resultSet.getInt(4));
                int score = resultSet.getInt(5);
                // creates a new row in the leaderboard
                Leaderboard.LeaderboardRow row = this.leaderboard.new LeaderboardRow(playerName, gameDate, duration, difficulty, score);
                // adds new row to the leaderboard, which can then be printed
                this.leaderboard.addLeaderboardRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Data could not be retrieved from leaderboard!");
        }
    }

    public Leaderboard loadLeaderBoard(int amount) {
//        Leaderboard leaderboard = new Leaderboard();
        String query = "SELECT p.name, g.end, g.duration, g.board_size, g.score FROM games g " +
                       "JOIN players p ON p.player_id = g.player_id ORDER BY g.score DESC FETCH FIRST ? ROWS ONLY";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, amount);
            fetchFromLeaderBoard(statement);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Leaderboard could not be loaded!");

        }
        return this.leaderboard;
    }

    //get leaderboard per person
    public Leaderboard fetchLeaderboardPlayer(String name){
        this.leaderboard = new Leaderboard();
        try {
            name = name.toUpperCase();
            String query = "SELECT p.name, g.end, g.duration, g.board_size, g.score FROM games g " +
                           "JOIN players p ON p.player_id = g.player_id  WHERE p.name = ? " +
                           "ORDER BY g.score DESC FETCH FIRST 5 ROWS ONLY";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            fetchFromLeaderBoard(statement);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Player scores could not be retrieved!");
        }
        return this.leaderboard;
    }

    //get leaderboard for difficulties based on boardSize
    public Leaderboard fetchLeaderboardDifficulty(int boardSize) {
        this.leaderboard = new Leaderboard();
        try {
            String query = "SELECT p.name, g.end, g.duration, g.board_size, g.score FROM games g " +
                           "JOIN players p ON p.player_id = g.player_id  WHERE g.board_size = ? " +
                           "ORDER BY g.score DESC FETCH FIRST 5 ROWS ONLY";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, boardSize);
            fetchFromLeaderBoard(statement);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Leaderboard of difficulty could not be retrieved!");
        }
        return this.leaderboard;
    }



}
