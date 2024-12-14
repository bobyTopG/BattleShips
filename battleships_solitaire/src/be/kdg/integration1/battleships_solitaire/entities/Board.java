package be.kdg.integration1.battleships_solitaire.entities;

import static be.kdg.integration1.battleships_solitaire.logic.Utility.repeat;

import java.util.*;

public class Board {

    private final int id;
    private final int boardSize;
    private final int shipAmount;
    private Tile[][] playerTiles;
    private Tile[][] answerTiles;
    private List<Ship> ships;

    public Board(int boardSize) {
        this(-1, boardSize);
    }

    public Board(int id, int boardSize) {
        this.id = id;
        if (!(boardSize == 5 || boardSize == 7 || boardSize == 9))
            throw new IllegalArgumentException();
        this.boardSize = boardSize;
        this.shipAmount = Ship.getShipAmountByDifficulty(Difficulty.valueOf(boardSize));
        generateTiles();
        generateShips();
        // startUpTiles();
        System.out.println(this);
    }

    public void generateShips() {
        Random random = new Random();
        this.ships = new ArrayList<>(shipAmount);
        List<Ship.Type> shipTypes = getShipTypes(Difficulty.valueOf(boardSize));

        // TODO: Implement difficulty logic, too
        while (ships.size() < shipAmount) { // Add ships
            Ship.Type shipType = shipTypes.getFirst();
            boolean isVertical = random.nextBoolean();
            int x = random.nextInt(boardSize - (isVertical ? 0 : shipType.getSize())) + 1;
            int y = random.nextInt(boardSize - (isVertical ? shipType.getSize() : 0)) + 1;

            Ship newShip = new Ship(x, y, shipType, isVertical);

            if (canPlaceShip(newShip)) {
                placeShip(newShip);
                ships.add(newShip);
                shipTypes.removeFirst();
                // System.out.println("Placed: " + shipType + " at (" + x + ", " + y + ")");
            } else {
                // System.out.println("Failed to place: " + shipType + ", retrying...");
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
        return shipTypes;
    }

    private boolean canPlaceShip(Ship ship) {
        int size = ship.getSize();
        int x = ship.getX() - 1;
        int y = ship.getIntY() - 1;
        boolean isVertical = ship.isVertical();

        for (int i = 0; i < size; i++) {
            int cx = isVertical ? x + i : x;
            int cy = isVertical ? y : y + i;

            if (cx >= boardSize || cy >= boardSize || answerTiles[cx][cy].isShip()) {
                return false; // Out of bounds or overlapping
            }

            // Check surrounding tiles
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int adjX = cx + dx;
                    int adjY = cy + dy;
                    if (adjX > 0 && adjX < boardSize
                            && adjY > 0 && adjY < boardSize
                            && answerTiles[adjX][adjY].isShip()) {
                        return false; // Adjacent to another ship
                    }
                }
            }
        }

        return true;
    }

    private void placeShip(Ship ship) {
        int size = ship.getSize();
        int x = ship.getX() - 1;
        int y = ship.getIntY() - 1;
        boolean isVertical = ship.isVertical();
        for (int i = 0; i < size; i++) {
            int cx = isVertical ? x + i : x;
            int cy = isVertical ? y : y + i;
            answerTiles[cx][cy].markAs(Tile.Type.SHIP_PART);
        }
    }

    public void generateTiles() {
        // System.out.println("Generating Tiles...");
        this.playerTiles = new Tile[boardSize][boardSize];
        this.answerTiles = new Tile[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                playerTiles[i][j] = new Tile(i + 1, j + 1);
                answerTiles[i][j] = new Tile(i + 1, j + 1);
            }
        }
    }

    public void startUpTiles() {
        Random random = new Random();
        int placed = 0;
        while (placed < boardSize * 2) {
            int x = random.nextInt(boardSize);
            int y = random.nextInt(boardSize);
            if (!playerTiles[x][y].isShip()) { // Only place a hint if it's not already a hint or ship
                playerTiles[x][y].setHint(true);
                answerTiles[x][y].setHint(true);
                placed++;
            }
        }

        // Set other default tiles to '~'
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (!playerTiles[i][j].isShip()) {
                    playerTiles[i][j].markAs(Tile.Type.WATER);
                }
            }
        }
    }

    public boolean isGameOver() {
        // Only when all the ships are placed
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (playerTiles[i][j].isShip() && !answerTiles[i][j].isShip()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void markTileAsShip(int x, int y) {
        if (answerTiles[x][y].isShip()) {
            System.out.println("There is already a ship");
        } else if (playerTiles[x][y].isHint() || answerTiles[x][y].isRevealed()) {
            System.out.println("Hey! There is hint :)");
        } else {
            System.out.println("New ship added.");
            answerTiles[x][y].markAs(Tile.Type.SHIP_PART);
        }
    }

    public void markTileAsWater(int x, int y) {
        if (answerTiles[x][y].isWater()) {
            System.out.println("This is already water");
        } else if (playerTiles[x][y].isHint() || answerTiles[x][y].isRevealed()) {
            System.out.println("Hey! There is hint :)");
        } else {
            System.out.println("New water tile added.");
            answerTiles[x][y].markAs(Tile.Type.WATER);
        }
    }

    public void unmarkTile(int x, int y) {
        if (answerTiles[x][y].isShip() || answerTiles[x][y].isWater() && !answerTiles[x][y].isRevealed()) {
            System.out.println("Removing ship part...");
            answerTiles[x][y].markAs(null);
        } else if (playerTiles[x][y].isHint() || answerTiles[x][y].isRevealed()) {
            System.out.println("Hey! There is hint :)");
        } else {
            System.out.println("No correction needed.");
        }

    }

    public void revealTile() {
        Random rand = new Random();
        int x, y;
        boolean revealedExist = false;
        for (Tile[] row : playerTiles) {
            for (Tile tile : row) {
                if (!tile.isRevealed()) {
                    revealedExist = true;
                    break;
                }
            }
            if (!revealedExist) {
                System.out.println("Everything is revealed. :(");
                break;
            }
        }

        if (revealedExist) {
            do {
                x = rand.nextInt(boardSize);
                y = rand.nextInt(boardSize);
            } while (!playerTiles[x][y].isWater());

            answerTiles[x][y] = playerTiles[x][y];
            answerTiles[x][y].setRevealedAs(playerTiles[x][y].getType());

            System.out.printf("Revealed tile at X: %d, Y: %d\n", x + 1, y + 1);
        }
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

    public String printAnswer() {
        StringBuilder sb = new StringBuilder();

        // Add column numbers
        sb.append("    ");
        for (int col = 1; col <= boardSize; col++) {
            sb.append(String.format("%2d ", col));
        }
        sb.append("\n");

        // Add top border
        sb.append("    ").append("- -".repeat(boardSize)).append(" -\n");

        // Add grid with tiles
        for (int i = 0; i < boardSize; i++) {
            int rowShipCount = 0; // Count ships in the row
            sb.append(String.format("%2d |", i + 1)); // Add row number

            for (int j = 0; j < boardSize; j++) {
                Tile tile = playerTiles[i][j];

                // Count ships in the row
                if (tile.isShip()) rowShipCount++;

                // Append tile to grid
                sb.append(tile.isShip() ? " □ " : (tile.isHint() ? " X " : " ~ "));
            }

            sb.append("| ").append(rowShipCount).append("\n"); // Add row ship count
        }

        // Add bottom border
        sb.append("    ").append("- -".repeat(boardSize)).append(" -\n");

        // Add column ship count
        sb.append("    ");
        for (int j = 0; j < boardSize; j++) {
            int colShipCount = 0;
            for (int i = 0; i < boardSize; i++) {
                if (playerTiles[i][j].isShip()) colShipCount++;
            }
            sb.append(String.format("%2d ", colShipCount));
        }
        sb.append("\n");

        return sb.toString();
    }

    @Override
    public String toString() {
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
                sb.append(" ").append(answerTiles[x - 1][y - 1]).append(" ");
            }
            sb.append("│ ").append("0/1").append("\n");
        }
        sb.append("   └").append("───".repeat(boardSize)).append("┘\n");
        sb.append("     ").append(String.format("%-3s", 0).repeat(boardSize)).append("\n");

        // Ships information
        sb.append("Ships: ");
        for (int i = 0; i < shipAmount; i++) {
            sb.append(ships.get(i)).append(" ");
        }
        sb.append("\n");

//
//        // Add column numbers
//        sb.append("    ");
//        for (int col = 1; col <= boardSize; col++) {
//            sb.append(String.format("%2d ", col));
//        }
//        sb.append("\n");
//
//        // Add top border
//        sb.append("    ").append("- -".repeat(boardSize)).append(" -\n");
//
//        // Add grid with tiles and row ship counts
//        for (int i = 0; i < boardSize; i++) {
//            int rowShipCount = 0;  // Counter for ships in the row
//            sb.append(String.format("%2d |", i + 1));  // Row number
//
//            for (int j = 0; j < boardSize; j++) {
//                Tile tile = answerTiles[i][j];
//
//                if (playerTiles[i][j].isShip()) rowShipCount++;  // Increment ship count
//
//                // Append tile to the grid
//                sb.append(tile.isShip() ? " □ " : (tile.isHint() ? " X " : (tile.isWater() ? " ~ " : " # ")));
//            }
//            sb.append("| ").append(rowShipCount).append("\n");  // Append row ship count
//        }
//
//        // Add bottom border
//        sb.append("    ").append("- -".repeat(boardSize)).append(" -\n");
//
//        // Add column ship counts
//        sb.append("    ");
//        for (int j = 0; j < boardSize; j++) {
//            int colShipCount = 0;
//            for (int i = 0; i < boardSize; i++) {
//                if (playerTiles[i][j].isShip()) colShipCount++;
//            }
//            sb.append(String.format("%2d ", colShipCount));
//        }
//        sb.append("\n");

        return sb.toString();
    }

}