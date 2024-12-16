package be.kdg.integration1.battleships_solitaire.entities;

import static be.kdg.integration1.battleships_solitaire.logic.Utility.repeat;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Board {

    private final int id;
    private final int boardSize;
    private final int shipAmount;
    private PlayerTile[][] playerTiles;
    private Tile[][] answerTiles;
    private List<Ship> ships;

    private LocalDateTime currentStartTime;
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
        generateTiles();
        System.out.print(".");
        generateShips();
        System.out.print(".");
        revealHints();
        System.out.println(".");
    }

    public void generateShips() {
        Random random = new Random();
        this.ships = new ArrayList<>(shipAmount);
        List<Ship.Type> shipTypes = getShipTypes(Difficulty.valueOf(boardSize));

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
            } else {
                System.out.print("_");
            }
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
        } catch (IllegalStateException e) {
            System.out.println("You cannot mark an already revealed tile.");
        }
    }

    public void markTileAsWater(int x, int y) {
        try {
            playerTiles[x][y].setCorrespondingShip(null);
            playerTiles[x][y].markAs(Tile.Type.WATER);
        } catch (IllegalStateException e) {
            System.out.println("You cannot mark an already revealed tile.");
        }
    }

    public void unmarkTile(int x, int y) {
        try {
            playerTiles[x][y].setCorrespondingShip(null);
            playerTiles[x][y].markAs(null);
        } catch (IllegalStateException e) {
            System.out.println("You cannot unmark an already revealed tile.");
        }
    }

    public void revealRandomTile() {
        Random rand = new Random();
        int x, y;
        while (true) {
            x = rand.nextInt(boardSize);
            y = rand.nextInt(boardSize);
            if (!playerTiles[x][y].isRevealed()
                    && playerTiles[x][y].getType()
                    != answerTiles[x][y].getType()) {
                break;
            }
        }
        System.out.println(playerTiles[x][y].getType() != null
                ? "Fixed a tile."
                : "Revealed a tile.");
        playerTiles[x][y].setCorrespondingShip(answerTiles[x][y].getCorrespondingShip());
        playerTiles[x][y].setRevealedAs(answerTiles[x][y].getType());
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

    public int getId() {
        return id;
    }

    public Tile[][] getPlayerTiles() {
        return playerTiles;
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
        for (int i = 0; i < shipAmount; i++) {
            sb.append(ships.get(i)).append(" ");
        }
        sb.append("\n");

        return sb.toString();
    }

}