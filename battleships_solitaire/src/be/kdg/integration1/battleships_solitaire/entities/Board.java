package be.kdg.integration1.battleships_solitaire.entities;

import static be.kdg.integration1.battleships_solitaire.logic.Utility.repeat;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Board {

    private int id;
    private final int boardSize;
    private final int shipAmount;
    private int score;

    private PlayerTile[][] playerTiles;
    private Tile[][] answerTiles;
    private List<Ship> ships;

    private LocalDateTime currentTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Duration duration;

    public Board(int boardSize) {
        this(-1, boardSize);
    }

    public Board(int id, int boardSize) {
        this.id = id;
        if (!(boardSize == 5 || boardSize == 7 || boardSize == 9))
            throw new IllegalArgumentException();
        this.boardSize = boardSize;
        this.shipAmount = Ship.getShipAmountByDifficulty(Difficulty.valueOf(boardSize));
        System.out.print("Generating board ");
        generate();
        revealHints();
        System.out.println(" .");
        score = Difficulty.valueOf(boardSize).getStartingPoints();
    }

    public Board(int id, int boardSize, int score, List<PlayerTile> playerTiles, List<Ship> ships, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.boardSize = boardSize;
        this.shipAmount = ships.size();
        this.score = score;
        this.ships = ships;
        this.startTime = startTime;
        this.duration = duration;
        generateTiles(playerTiles);
    }

    public void generate() {
        generateTiles();
        generateRandomShips();
    }

    public void generateRandomShips() {
        Random random = new Random();
        this.ships = new ArrayList<>(shipAmount);
        List<Ship.Type> shipTypes = getShipTypes(Difficulty.valueOf(boardSize));

        int attempts = 0;
        final int MAX_ATTEMPTS = 32;

        while (ships.size() < shipAmount) { // Add ships
            Ship.Type shipType = shipTypes.getFirst();
            boolean isVertical = random.nextBoolean();
            int x = random.nextInt(boardSize - (isVertical ? 0 : shipType.getSize()));
            int y = random.nextInt(boardSize - (isVertical ? shipType.getSize() : 0));

            Ship newShip = new Ship(x + 1, y + 1, shipType, isVertical);
            Tile[] newShipTiles = generateTilesFromShip(newShip);
            if (canPlaceShip(newShipTiles)) {
                placeShip(newShipTiles);
                ships.add(newShip);
                shipTypes.removeFirst();
                System.out.print(".");
                attempts = 0;
            } else {
                System.out.print("_");
                attempts++;
            }

            // if it takes too many attempts just restart the generation process
            if (attempts >= MAX_ATTEMPTS) {
                System.out.println(",");
                generate();
                break;
            }
        }
    }

    private void generateShipsFromList() {
        for (Ship ship : ships) {
            placeShip(generateTilesFromShip(ship));
        }
    }

    private List<Ship.Type> getShipTypes(Difficulty difficulty) {
        // Generate a list of ship types there should be per difficulty
        List<Ship.Type> shipTypes = new ArrayList<>(shipAmount);
        switch (difficulty) {
            case null: break;
            case HARD:
                shipTypes.add(Ship.Type.BATTLESHIP);
                shipTypes.add(Ship.Type.CRUISER);
            case MEDIUM:
                shipTypes.add(Ship.Type.CRUISER);
                shipTypes.add(Ship.Type.DESTROYER);
                shipTypes.add(Ship.Type.SUBMARINE);
            case EASY:
            default:
                repeat(2, () -> shipTypes.add(Ship.Type.DESTROYER));
                repeat(3, () -> shipTypes.add(Ship.Type.SUBMARINE));
        }
        Collections.sort(shipTypes);
        Collections.reverse(shipTypes);
        return shipTypes;
    }

    private boolean canPlaceShip(Tile[] tiles) {
        int x, y;
        for (Tile tile : tiles) {
            x = tile.getX() - 1;
            y = tile.getIntY() - 1;
            if (x < 0 || y < 0 || x >= boardSize || y >= boardSize) {
                return false; // out of the board
            }
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    try {
                        if (answerTiles[x + dx][y + dy].getType() == Tile.Type.SHIP_PART) {
                            return false;
                        }
                    } catch (IndexOutOfBoundsException ignored) {
                        // We can ignore index out of bounds because we are already sure that the ship tiles themselves are on the board
                    }
                }
            }
        }
        return true;
    }

    private Tile[] generateTilesFromShip(Ship ship) {
        Tile[] tiles = new Tile[ship.getSize()];
        for (int i = 0; i < ship.getSize(); i++) {
            if (ship.isVertical()) {
                tiles[i] = new Tile(ship.getX(), ship.getIntY() + i);
            } else {
                tiles[i] = new Tile(ship.getX() + i, ship.getIntY());
            }
            tiles[i].markAs(Tile.Type.SHIP_PART);
            tiles[i].setCorrespondingShip(ship);
        }
        return tiles;
    }

    private void placeShip(Tile[] tiles) {
        int x, y;
        for (Tile tile : tiles) {
            x = tile.getX() - 1;
            y = tile.getIntY() - 1;
            answerTiles[x][y].markAs(tile.getType());
            answerTiles[x][y].setCorrespondingShip(tile.getCorrespondingShip());
        }
    }

    public void generateTiles() {
        // System.out.println("Generating Tiles...");
        this.playerTiles = new PlayerTile[boardSize][boardSize];
        this.answerTiles = new Tile[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                playerTiles[i][j] = new PlayerTile(i + 1, j + 1);
                answerTiles[i][j] = new Tile(i + 1, j + 1);
                answerTiles[i][j].markAs(Tile.Type.WATER);
            }
        }
    }

    public void generateTiles(List<PlayerTile> playerTiles) {
        // System.out.println("Generating Tiles...");
        generateTiles();
        generateShipsFromList();
        for (PlayerTile tile : playerTiles) {
            this.playerTiles[tile.getX() - 1][tile.getIntY() - 1] = tile;
            if (tile.getType() == Tile.Type.SHIP_PART) {
                this.playerTiles[tile.getX() - 1][tile.getIntY() - 1].setCorrespondingShip(answerTiles[tile.getX() - 1][tile.getIntY() - 1].getCorrespondingShip());
            }
        }
    }

    public void revealHints() {
        Random random = new Random();
        int hints = Difficulty.valueOf(boardSize).getDefaultHints();
        int x, y;
        for (int i = 0; i < hints; i++) {
            x = random.nextInt(boardSize);
            y = random.nextInt(boardSize);
            if (playerTiles[x][y].isRevealed()) {
                i--;
            } else {
                playerTiles[x][y].setCorrespondingShip(answerTiles[x][y].getCorrespondingShip());
                playerTiles[x][y].setRevealedAs(answerTiles[x][y].getType());
            }
        }
    }

    public boolean isGameOver() {
        // first we can check if all actual ship parts are found
        for (Ship ship : ships) {
            if (ship.getRemainingParts() > 0) {
                return false;
            }
        }
        // then we can check if there are no extra ship pieces
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                if (playerTiles[x][y].getType() == Tile.Type.SHIP_PART
                        && playerTiles[x][y].getCorrespondingShip() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public void markTileAsShip(int x, int y) {
        try {
            playerTiles[x][y].setCorrespondingShip(answerTiles[x][y].getCorrespondingShip());
            playerTiles[x][y].markAs(Tile.Type.SHIP_PART);
            // SCORE: If the player guess this ship pieces right, they will earn 2 points,
            // or else they will lose 1 point
            score += answerTiles[x][y].getType() == Tile.Type.SHIP_PART ? 2 : -1;
        } catch (IllegalStateException e) {
            System.out.println("You cannot mark an already revealed tile.");
        }
    }

    public void markTileAsWater(int x, int y) {
        try {
            playerTiles[x][y].setCorrespondingShip(null);
            playerTiles[x][y].markAs(Tile.Type.WATER);
            // SCORE: If the player guesses this water tile, they will earn 1 point,
            // or else they will lose 1 point
            score += answerTiles[x][y].getType() == Tile.Type.WATER ? 1 : -1;
        } catch (IllegalStateException e) {
            System.out.println("You cannot mark an already revealed tile.");
        }
    }

    public void unmarkTile(int x, int y) {
        try {
            // SCORE: If the player unmarks a tile the score resets
            if (playerTiles[x][y].getType() != null) {
                if (playerTiles[x][y].getType() == answerTiles[x][y].getType()) {
                    score -= answerTiles[x][y].getType() == Tile.Type.SHIP_PART ? 2 : 1;
                } else {
                    score++;
                }
            }
            playerTiles[x][y].setCorrespondingShip(null);
            playerTiles[x][y].markAs(null);
        } catch (IllegalStateException e) {
            System.out.println("You cannot unmark an already revealed tile.");
        }
    }

    public void revealRandomTile() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(boardSize);
            y = rand.nextInt(boardSize);
        } while (playerTiles[x][y].isRevealed()
                || playerTiles[x][y].getType() == answerTiles[x][y].getType());
        System.out.println(playerTiles[x][y].getType() != null
                ? "Fixed a tile."
                : "Revealed a tile.");
        playerTiles[x][y].setCorrespondingShip(answerTiles[x][y].getCorrespondingShip());
        playerTiles[x][y].setRevealedAs(answerTiles[x][y].getType());
        score -= 2;
    }

    private int getMarkedShipsInColumn(Tile[][] tiles, int x) {
        int count = 0;
        for (int y = 0; y < boardSize; y++) {
            if (tiles[x][y].isShip()) {
                count++;
            }
        }
        return count;
    }

    private int getMarkedShipsInRow(Tile[][] tiles, int y) {
        int count = 0;
        for (int x = 0; x < boardSize; x++) {
            if (tiles[x][y].isShip()) {
                count++;
            }
        }
        return count;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getScore() {
        return score;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public int getId() {
        return id;
    }

    public PlayerTile[][] getPlayerTiles() {
        return playerTiles;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void updateDuration() {
        Duration currentDuration = Duration.between(currentTime, LocalDateTime.now());
        currentTime = LocalDateTime.now();
        duration = duration.plus(currentDuration);
    }

    public LocalDateTime getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(LocalDateTime currentTime) {
        this.currentTime = currentTime;
    }

    @Override
    public String toString() {
        return tilesToString(playerTiles);
    }

    public String answersToString() {
        return tilesToString(answerTiles);
    }

    public String tilesToString(Tile[][] tiles) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(
                "\n\n[%s] Score: \033[1;33m%d\033[0m\n",
                Difficulty.valueOf(boardSize).getName().toUpperCase(),
                score
        ));
        sb.append(String.format(
                "Game duration: %02dh:%02dm:%02ds\n",
                duration.toHours(),
                duration.toMinutesPart(),
                duration.toSecondsPart()
        ));
        // First row and upper board line
        sb.append(" ".repeat(4));
        for (int x = 1; x <= boardSize; x++) {
            sb.append(" ").append(x).append(" ");
        }
        sb.append("\n").append(" ".repeat(3));
        sb.append("┌").append("─┴─".repeat(boardSize)).append("┐\n");
        for (int y = 1; y <= boardSize; y++) {
            sb.append(" ").append((char) ('A' + y - 1)).append(" ┤");
            for (int x = 1; x <= boardSize; x++) {
                sb.append(" ").append(tiles[x - 1][y - 1]).append(" ");
            }
            sb.append("│ ");
            sb.append(getMarkedShipsInRow(tiles, y - 1));
            sb.append("/");
            sb.append(getMarkedShipsInRow(answerTiles, y - 1));
            sb.append("\n");
        }
        sb.append("   └").append("───".repeat(boardSize)).append("┘\n");
        sb.append("rem:");
        for (int x = 1; x <= boardSize; x++) {
            int remainding = getMarkedShipsInColumn(answerTiles, x - 1) - getMarkedShipsInColumn(tiles, x - 1);
            sb.append(String.format("%2s ", remainding));
        }
        sb.append("\n");

        // Ships information
        sb.append("Ships: ");
        int lastShipSize = ships.getFirst().getSize();
        for (int i = 0; i < shipAmount; i++) {
            sb.append(lastShipSize == ships.get(i).getSize() ? "" : "| ");
            sb.append(ships.get(i)).append(" ");
            lastShipSize = ships.get(i).getSize();
        }
        sb.append("\n");

        return sb.toString();
    }

}