package be.kdg.integration1.battleships_solitaire;

import java.util.Random;

public class Board {

    private final int boardSize; //Difficulty!
    private char[][] tiles;
    private char[][] tilesSolution;
    private SimpleMenu menu;

    public Board(int boardSize) {
        this.boardSize = boardSize;
        tiles = new char[boardSize][boardSize];
        tilesSolution = new char[boardSize][boardSize];
        SimpleMenu simpleMenu = new SimpleMenu();

    }

    public boolean isGameOver() {
        if (tiles.length != tilesSolution.length || tiles[0].length != tilesSolution[0].length) {
            return false; // Arrays with different dimensions can't be identical
        }

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {

                // Check if ships are in the same positions
                if (tiles[i][j] == '□' && tilesSolution[i][j] != '□') {
                    return false; // Ship is in array1 but not in array2
                }
                if (tiles[i][j] != '□' && tilesSolution[i][j] == '□') {
                    return false; // Ship is in array2 but not in array1
                }
            }
        }

        return true;
    }


    public void generateShips() {

        Random rand = new Random();

        System.out.println("Generating ships...");
        int size = rand.nextInt(1, 5); // Ship size between 1 and 4 tiles

        while (true) {
            boolean isVertical = rand.nextBoolean();
            int startX = rand.nextInt(boardSize - (isVertical ? size : 0)); // Ensure within bounds for vertical
            int startY = rand.nextInt(boardSize - (isVertical ? 0 : size)); // Ensure within bounds for horizontal

            boolean canPlace = true;

            // Check if the ship can be placed
            for (int i = 0; i < size && canPlace; i++) {
                int cx = isVertical ? startX + i : startX;
                int cy = isVertical ? startY : startY + i;

                if (tiles[cx][cy] == '□') {
                    System.out.printf("Overlap detected at (%d, %d)\n", cx, cy); // Debug print
                    canPlace = false;
                    break;
                }

                // Check surrounding tiles
                for (int dx = -1; dx <= 1 && canPlace; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int adjX = cx + dx, adjY = cy + dy;
                        if (adjX >= 0 && adjX < boardSize && adjY >= 0 && adjY < boardSize && tiles[adjX][adjY] == '□') {
                            System.out.printf("Touching ship detected at (%d, %d)\n", adjX, adjY); // Debug print
                            canPlace = false;
                            break;
                        }
                    }
                }
            }

            // Place the ship if no conflicts
            if (canPlace) {
                for (int i = 0; i < size; i++) {
                    int cx = isVertical ? startX + i : startX;
                    int cy = isVertical ? startY : startY + i;
                    tiles[cx][cy] = '□';
                    tilesSolution[cx][cy] = '#';
                }
                System.out.println("Ship successfully placed!");
                break; // Exit loop after successful placement
            } else {
                System.out.println("Failed to place ship, retrying...");
            }
        }
    }

    public void generateTiles() {
        System.out.println("Generating Tiles..."); // Debug print

        // Randomly place hint tiles (X)
        Random rand = new Random();
        int placed = 0;
        while (placed < boardSize*1.5) {
            int x = rand.nextInt(boardSize);
            int y = rand.nextInt(boardSize);

            // Only place a hint if it's not already a hint or ship
            if (tiles[x][y] == '\0') { // '\0' indicates an uninitialized tile
                tiles[x][y] = 'X';
                tilesSolution[x][y] = 'X';
                placed++;
            }
        }

        // Populate tiles and tilesSolution with default values
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                char tile = tiles[i][j];

                if (tile == '\0') { // Default empty tile
                    tiles[i][j] = '~';
                    tilesSolution[i][j] = '~';
                }
            }
        }
    }


    public void makeTileAShip(int x, int y) {
        if (tilesSolution[x][y] == '□') {
            System.out.println("There is already a ship");
        } else if (tilesSolution[x][y] == '#') {
            System.out.println("New ship added.");
            tilesSolution[x][y] = '□';
        } else if (tilesSolution[x][y] == '~') {
            tilesSolution[x][y] = '□';
            System.out.println("New ship added.");
        } else if (tiles[x][y] == 'X') {
            System.out.println("Hey! This is a hint :)");
        }
    }

    public void makeTileWater(int x, int y) {
        if (tilesSolution[x][y] == '~') {
            System.out.println("This is already a water");
        } else if (tilesSolution[x][y] == '#') {
            System.out.println("New water tile added.");
            tilesSolution[x][y] = '~';
        } else if (tilesSolution[x][y] == '□') {
            System.out.println("New water tile added.");
            tilesSolution[x][y] = '~';
        } else if (tiles[x][y] == 'X') {
            System.out.println("Hey! This is a hint :)");
        }
    }

    public void removeTile(int x, int y) {
        if (tilesSolution[x][y] == '□') {
            System.out.println("Removing ship part...");
            tilesSolution[x][y] = '#';
        } else if (tilesSolution[x][y] == '~') {
            System.out.println("Removing water tile...");
            tilesSolution[x][y] = '#';
        }else if (tiles[x][y] == 'X') {
            System.out.println("Hey! This is a hint :)");
        }
        else {
            System.out.println("No correction needed.");
        }
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
                char tile = tiles[i][j];

                // Count ships in the row
                if (tile == '□') rowShipCount++;

                // Append tile to grid
                sb.append(tile == '□' || tile == 'X' ? " " + tile + " " : " ~ ");
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
                if (tiles[i][j] == '□') colShipCount++;
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
                char tile = tilesSolution[i][j];

                if (tiles[i][j] == '□') rowShipCount++;  // Increment ship count

                // Append tile to the grid
                sb.append(tile == '#' || tile == 'X' ? " " + tile + " " : " # ");
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
                if (tiles[i][j] == '□') colShipCount++;
            }
            sb.append(String.format("%2d ", colShipCount));
        }
        sb.append("\n");

        return sb.toString();
    }
}

