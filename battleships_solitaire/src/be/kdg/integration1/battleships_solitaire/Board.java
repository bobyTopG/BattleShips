package be.kdg.integration1.battleships_solitaire;

import java.util.*;

public class Board {
    private final int boardSize;
    private Tile[][] tiles;
    private Tile[][] tilesSolution;
    private List<Ship> ships;

    public Board(int boardSize) {
        this.boardSize = boardSize;
        this.tiles = new Tile[boardSize][boardSize];
        this.tilesSolution = new Tile[boardSize][boardSize];
        this.ships = new ArrayList<>();

        // Initialize tiles with default values
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                tiles[i][j] = new Tile(i, j);
                tilesSolution[i][j] = new Tile(i, j);
            }
        }
    }

    public boolean isGameOver() {
        // Only when all the ships are placed
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (tiles[i][j].isShip() && !tilesSolution[i][j].isShip()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void generateShips() {
        Random rand = new Random();
       // System.out.println("Generating ships...");

        while (ships.size() < boardSize/2) { // Add ships
            ShipType shipType = ShipType.values()[rand.nextInt(ShipType.values().length)]; //Ensuring that a random ship type is chosen
            boolean isVertical = rand.nextBoolean();
            int x = rand.nextInt(boardSize - (isVertical ? shipType.getSize() : 0));
            int y = rand.nextInt(boardSize - (isVertical ? 0 : shipType.getSize()));

            Ship newShip = new Ship(x, y, shipType, isVertical);

            if (canPlaceShip(newShip)) {
                placeShip(newShip);
                ships.add(newShip);
               // System.out.println("Placed: " + shipType + " at (" + x + ", " + y + ")");
            } else {
               // System.out.println("Failed to place: " + shipType + ", retrying...");
            }
        }
    }

    private void placeShip(Ship ship) {
        int size = ship.getSize();
        int x = ship.getX();
        int y = ship.getY();
        boolean isVertical = ship.isVertical();

        for (int i = 0; i < size; i++) {
            int cx = isVertical ? x + i : x;
            int cy = isVertical ? y : y + i;
            tiles[cx][cy].setShip(true);
        }
    }

    private boolean canPlaceShip(Ship ship) {
        int size = ship.getSize();
        int x = ship.getX();
        int y = ship.getY();
        boolean isVertical = ship.isVertical();

        for (int i = 0; i < size; i++) {
            int cx = isVertical ? x + i : x;
            int cy = isVertical ? y : y + i;

            if (cx >= boardSize || cy >= boardSize || tiles[cx][cy].isShip()) {
                return false; // Out of bounds or overlapping
            }

            // Check surrounding tiles
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int adjX = cx + dx;
                    int adjY = cy + dy;
                    if (adjX >= 0 && adjX < boardSize && adjY >= 0 && adjY < boardSize &&
                            tiles[adjX][adjY].isShip()) {
                        return false; // Adjacent to another ship
                    }
                }
            }
        }

        return true;
    }

    public void generateTiles(){
       // System.out.println("Generating Tiles...");

        Random rand = new Random();
        int placed = 0;
        while (placed < boardSize * 2) {
            int x = rand.nextInt(boardSize);
            int y = rand.nextInt(boardSize);

            if (!tiles[x][y].isShip()) { // Only place a hint if it's not already a hint or ship
                tiles[x][y].setHint(true);
                tilesSolution[x][y].setHint(true);
                placed++;
            }
        }

        // Set other default tiles to '~'
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (!tiles[i][j].isShip()) {
                    tiles[i][j].setShip(false);
                    tiles[i][j].setWater(true);

                }
            }
        }
    }

    public void makeTileAShip(int x, int y) {
        if (tilesSolution[x][y].isShip()) {
            System.out.println("There is already a ship");
        } else if (tiles[x][y].isHint()) {
            System.out.println("Hey! There is hint :)");
        } else {
            System.out.println("New ship added.");
            tilesSolution[x][y].setShip(true);
        }
    }

    public void makeTileWater(int x, int y) {
        if (tilesSolution[x][y].isWater()) {
            System.out.println("This is already water");
        } else if (tiles[x][y].isHint()) {
            System.out.println("Hey! There is hint :)");
        } else {
            System.out.println("New water tile added.");
            tilesSolution[x][y].setShip(false);
            tilesSolution[x][y].setWater(true);
        }
    }

    public void removeTile(int x, int y) {
        if (tilesSolution[x][y].isShip() || tilesSolution[x][y].isWater() ) {
            System.out.println("Removing ship part...");
            tilesSolution[x][y].setShip(false);
            tilesSolution[x][y].setWater(false);

        } else if (tiles[x][y].isHint()) {
            System.out.println("Hey! There is hint :)");
        }else {
            System.out.println("No correction needed.");
        }

    }

    public void revealTile() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(boardSize);
            y = rand.nextInt(boardSize);
        } while (!tiles[x][y].isWater());

        tilesSolution[x][y] = tiles[x][y];

        System.out.printf("Revealed tile at X: %d, Y: %d\n", x+1, y+1);
    }

    public int getBoardSize() {
        return boardSize;
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
                Tile tile = tiles[i][j];

                // Count ships in the row
                if (tile.isShip()) rowShipCount++;

                // Append tile to grid
                sb.append(tile.isShip() ? " □ " :(tile.isHint() ? " X " : " ~ "));
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
                if (tiles[i][j].isShip()) colShipCount++;
            }
            sb.append(String.format("%2d ", colShipCount));
        }
        sb.append("\n");

        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Add column numbers
        sb.append("    ");
        for (int col = 1; col <= boardSize; col++) {
            sb.append(String.format("%2d ", col));
        }
        sb.append("\n");

        // Add top border
        sb.append("    ").append("- -".repeat(boardSize)).append(" -\n");

        // Add grid with tiles and row ship counts
        for (int i = 0; i < boardSize; i++) {
            int rowShipCount = 0;  // Counter for ships in the row
            sb.append(String.format("%2d |", i + 1));  // Row number

            for (int j = 0; j < boardSize; j++) {
                Tile tile = tilesSolution[i][j];

                if (tiles[i][j].isShip()) rowShipCount++;  // Increment ship count

                // Append tile to the grid
                sb.append(tile.isShip() ? " □ " : (tile.isHint() ? " X " :(tile.isWater() ? " ~ " :  " # ")));
            }
            sb.append("| ").append(rowShipCount).append("\n");  // Append row ship count
        }

        // Add bottom border
        sb.append("    ").append("- -".repeat(boardSize)).append(" -\n");

        // Add column ship counts
        sb.append("    ");
        for (int j = 0; j < boardSize; j++) {
            int colShipCount = 0;
            for (int i = 0; i < boardSize; i++) {
                if (tiles[i][j].isShip()) colShipCount++;
            }
            sb.append(String.format("%2d ", colShipCount));
        }
        sb.append("\n");

        return sb.toString();
    }
}

