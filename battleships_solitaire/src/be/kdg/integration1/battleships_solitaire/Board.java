package be.kdg.integration1.battleships_solitaire;

import jdk.jshell.spi.ExecutionControl;

import java.sql.Array;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Random;

public class Board {

    private int boardSize;
    private char[][] tiles;
    //TODO: maybe rename tilesCopy to tilesSolution for clarity?
    private char[][] tilesCopy;
    private SimpleMenu menu;

    private long startTime;
    private long endTime;

    public Board(int boardSize) {
        this.boardSize = boardSize;
        tiles = new char[boardSize][boardSize];
        tilesCopy = new char[boardSize][boardSize];
        SimpleMenu simpleMenu = new SimpleMenu();
        startTime = System.currentTimeMillis();
    }


    public boolean isGameOver() {
        if (tiles.length != tilesCopy.length || tiles[0].length != tilesCopy[0].length) {
            return false; // Arrays with different dimensions can't be identical
        }

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {

                // Check if ships are in the same positions
                if (tiles[i][j] == '□' && tilesCopy[i][j] != '□') {
                    return false; // Ship is in array1 but not in array2
                }
                if (tiles[i][j] != '□' && tilesCopy[i][j] == '□') {
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
        boolean isCreated = false;

        while (!isCreated) {
            boolean isVertical = rand.nextBoolean();
            int startX = rand.nextInt(boardSize - (isVertical ? size : 0)); // Ensure within bounds for vertical
            int startY = rand.nextInt(boardSize - (isVertical ? 0 : size)); // Ensure within bounds for horizontal

            boolean canPlace = true;

            // Check if the ship can be placed
            for (int i = 0; i < size; i++) {
                int cx = isVertical ? startX + i : startX;
                int cy = isVertical ? startY : startY + i;

                if (tiles[cx][cy] == '□') {
                    System.out.printf("Overlap detected at (%d, %d)\n", cx, cy); // Debug print
                    canPlace = false;
                    break;
                }

                // Check surrounding tiles
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {

                        int adjX = cx + dx;
                        int adjY = cy + dy;
                        if (adjX >= 0 && adjX < boardSize && adjY >= 0 && adjY < boardSize) {
                            if (tiles[adjX][adjY] == '□') {
                                System.out.printf("Touching ship detected at (%d, %d)\n", adjX, adjY); // Debug print
                                canPlace = false;
                                break;
                            }
                        }
                    }
                    if (!canPlace) break;
                }
                if (!canPlace) break;
            }

            // Place the ship if no conflicts
            if (canPlace) {
                for (int i = 0; i < size; i++) {
                    int cx = isVertical ? startX + i : startX;
                    int cy = isVertical ? startY : startY + i;
                    tiles[cx][cy] = '□';
                }
                isCreated = true;
                System.out.println("Ship successfully placed!");// Debug print
            } else {
                System.out.println("Failed to place ship, retrying...");// Debug print
            }
        }
    }


    public void generateTiles() {
        System.out.println("Generating Tiles...");// Debug print

        // Randomly place 6 hint tiles (~)
        Random rand = new Random();
        int placed = 0;
        while (placed < 6) {
            int x = rand.nextInt(boardSize);
            int y = rand.nextInt(boardSize);

            // Only place a hint if it's not already a hint or ship
            if (tiles[x][y] != '□' && tiles[x][y] != '~') {
                tiles[x][y] = '~';
                tilesCopy[x][y] = '~';
                placed++;
            }
        }


        for (int i = 0; i < boardSize; i++) {
            // Add row number
            for (int j = 0; j < boardSize; j++) {
                char tile = tiles[i][j];

                if (tile == '□') {
                    tilesCopy[i][j] = '#'; // Replace ship tiles with hidden state
                    tiles[i][j] = '□';

                } else if (tile == '~') {
                    tiles[i][j] = '~';
                    tilesCopy[i][j] = '~';
                } else {
                    tiles[i][j] = '#';
                    tilesCopy[j][i] = '#';
                }// Default
            }
        }
    }


    public void makeTileAShip(int x, int y) {
        if (tilesCopy[x][y] == '□') {
            System.out.println("There is already a ship");
        } else if (tilesCopy[x][y] == '#') {
            System.out.println("New ship added.");
            tilesCopy[x][y] = '□';
        } else if (tilesCopy[x][y] == '~') {
            System.out.println("Hey! This is a hint :)");
        }
    }

    public void makeTileWater(int x, int y) {
        if (tilesCopy[x][y] == '~') {
            System.out.println("This is already a water");
        } else if (tilesCopy[x][y] == '#') {
            System.out.println("New water tile added.");
            tilesCopy[x][y] = '~';
        } else if (tilesCopy[x][y] == '□') {
            System.out.println("New water tile added.");
            tilesCopy[x][y] = '~';
        }
    }

    public void removeTile(int x, int y) {
        if (tilesCopy[x][y] == '□') {
            System.out.println("Removing ship part...");
            tilesCopy[x][y] = '#';
        } else if (tilesCopy[x][y] == '~') {
            System.out.println("Removing water tile...");
            tilesCopy[x][y] = '#';
        } else {
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
        sb.append("    ");
        for (int i = 0; i < boardSize; i++) {
            sb.append("- -");
        }
        sb.append(" -\n");

        // Add grid with tiles
        for (int i = 0; i < boardSize; i++) {
            int rowShipCount = 0;  // Initialize the counter for ships in the row
            sb.append(String.format("%2d |", i + 1));  // Add row number to the start of the row

            for (int j = 0; j < boardSize; j++) {
                char tile = tiles[i][j];

                // Check the tile and append it to the string builder
                if (tiles[i][j] == '□') {
                    rowShipCount++;    // Increment ship count for the row
                }

                if (tiles[i][j] == '□') {
                    sb.append(" □ ");
                } else if (tile == '~' || tile == '□') {
                    sb.append(" " + tile + " ");  // Display revealed tiles
                } else {
                    sb.append(" # ");  // Display hidden state for unknown tiles
                }
            }

            sb.append("| ").append(rowShipCount).append("\n");  // Append the ship count at the end of the row
        }


        // Add bottom border
        sb.append("   ");
        for (int i = 0; i < boardSize; i++) {
            sb.append("- -");
        }
        sb.append(" -\n");

        // Add column ship count
        sb.append("    ");
        for (int j = 0; j < boardSize; j++) {
            int colShipCount = 0;
            for (int i = 0; i < boardSize; i++) {
                if (tiles[i][j] == '□') {
                    colShipCount++;
                }
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
        sb.append("    ");
        for (int i = 0; i < boardSize; i++) {
            sb.append("- -");
        }
        sb.append(" -\n");

        // Add grid with tiles
        for (int i = 0; i < boardSize; i++) {
            int rowShipCount = 0;  // Initialize the counter for ships in the row
            sb.append(String.format("%2d |", i + 1));  // Add row number to the start of the row

            for (int j = 0; j < boardSize; j++) {
                char tile = tilesCopy[i][j];

                // Check the tile and append it to the string builder
                if (tiles[i][j] == '□') {
                    rowShipCount++;    // Increment ship count for the row
                }

                if (tilesCopy[i][j] == '□') {
                    sb.append(" □ ");
                } else if (tile == '~' || tile == '□') {
                    sb.append(" " + tile + " ");  // Display revealed tiles
                } else {
                    sb.append(" # ");  // Display hidden state for unknown tiles
                }
            }

            sb.append("| ").append(rowShipCount).append("\n");  // Append the ship count at the end of the row
        }


        // Add bottom border
        sb.append("   ");
        for (int i = 0; i < boardSize; i++) {
            sb.append("- -");
        }
        sb.append(" -\n");

        // Add column ship count
        sb.append("    ");
        for (int j = 0; j < boardSize; j++) {
            int colShipCount = 0;
            for (int i = 0; i < boardSize; i++) {
                if (tiles[i][j] == '□') {
                    colShipCount++;
                }
            }
            sb.append(String.format("%2d ", colShipCount));
        }
        sb.append("\n");

        return sb.toString();
    }


}

//-boardSize: int
//-score: int
//-start: Timestamp
//-end: Timestamp
//-duration:

//+«create»(int)
//+generateShips()
//+getRandomShip()
//+generateTiles()
//+revealTile()
//+correctTile()
//+updateTile(Tile tile)
//+isFullyMarked(): boolean
//+isCorrect(): boolean
//+toString(): String