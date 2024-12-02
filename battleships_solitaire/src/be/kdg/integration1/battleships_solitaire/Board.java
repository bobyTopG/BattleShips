package be.kdg.integration1.battleships_solitaire;

import java.sql.Timestamp;
import java.util.Random;

public class Board {

    private int boardSize;
    private int score;
    //    private Timestamp start;
//    private Timestamp end;
    private int duration;
    private char[][] tiles;


    public Board(int boardSize, int score, int duration) {
        this.boardSize = boardSize;
        this.score = score;
//        this.start = start;
//        this.end = end;
        this.duration = duration;
        tiles = new char[boardSize][boardSize];
    }


    public void generateShips() {

        Random rand = new Random();
        boolean isCreated = false;
        int size = rand.nextInt(2, 5); // Ship size between 2 and 4 tiles

        while (!isCreated) {
            boolean isVertical = rand.nextBoolean(); // Randomly choose orientation
            int startX, startY;

            if (isVertical) {
                // Ensure vertical placement avoids top and bottom boundaries
                startX = rand.nextInt(boardSize - 2 - size) + 1; // Between 1 and boardSize - size - 1
                startY = rand.nextInt(boardSize - 2) + 1; // Between 1 and boardSize - 2
            } else {
                // Ensure horizontal placement avoids left and right boundaries
                startX = rand.nextInt(boardSize - 2) + 1; // Between 1 and boardSize - 2
                startY = rand.nextInt(boardSize - 2 - size) + 1; // Between 1 and boardSize - size - 1
            }

            // Check if the ship can be placed without overlap
            boolean canPlace = true;
            for (int i = 0; i < size; i++) {
                int x = isVertical ? startX + i : startX;
                int y = isVertical ? startY : startY + i;

                if (tiles[x][y] == '□') {
                    canPlace = false;
                    break;
                }
            }

            // Place the ship if there's no overlap
            if (canPlace) {
                for (int i = 0; i < size; i++) {
                    int x = isVertical ? startX + i : startX;
                    int y = isVertical ? startY : startY + i;

                    tiles[x][y] = '□';
                }
                isCreated = true;
            }
        }
    }


    public void getRandomShip() {

    }

    ;

    public String generateTiles() {
        StringBuilder sb = new StringBuilder();

        // Calculate the total width of the grid including the column numbers and boundaries
        int totalWidth = (boardSize * 3) + 4; // Each tile has 3 spaces and there's 4 spaces for borders (left, right, and between)

        // Add column numbers (start from 1) for the top row, excluding boundaries
        sb.append("    "); // Adjust for spacing to align the column numbers
        for (int col = 1; col <= boardSize; col++) {
            sb.append(String.format("%2d ", col)); // Format the column numbers
        }
        sb.append("\n");

        // Add the top boundary row (fully extended with boundaries)
        sb.append("   ");
        for (int i = 0; i < boardSize; i++) {
            sb.append("- -"); // Add hyphens for the boundary
        }
        sb.append(" -\n"); // Add extra space for the right boundary

        // Generate the board with row numbers (start from 0)
        for (int i = 0; i < boardSize; i++) {  // Start from 0 for rows
            sb.append(String.format("%2d |", i + 1)); // Row number for grid, with left boundary

            // Add the grid tiles
            for (int j = 0; j < boardSize; j++) { // Start from 0 for columns
                if (tiles[i][j] != '□') {
                    sb.append(" # ");  // Add water tile for non-ship positions
                } else {
                    sb.append(" □ ");  // Add ship part
                }
            }

            sb.append("|");  // Right boundary
            sb.append("\n");
        }

        // Add the bottom boundary row (fully extended with boundaries)
        sb.append("   ");
        for (int i = 0; i < boardSize; i++) {
            sb.append("- -"); // Add hyphens for the boundary
        }
        sb.append(" -\n"); // Add extra space for the right boundary

        return sb.toString();
    }




    public void revealTile(int x, int y) {

            if (tiles[x][y] == '□') {
                System.out.println("Hit!");
                tiles[x][y] = 'X';
            } else if (tiles[x][y] == '~') {
                System.out.println("Miss.");
                tiles[x][y] = 'O';
            } else {
                System.out.println("Already revealed.");
            }
    }



    public void correctTile(int x, int y) {
        if (tiles[x][y] == '□') {
            System.out.println("Removing ship part...");
            tiles[x][y] = '~';
        } else {
            System.out.println("No correction needed.");
        }
    }



    public void updateTile(Tile tile) {

    }



    public boolean isFullyMarked() {
        return false;
    }


    public int getBoardSize() {
        return boardSize;
    }

    public boolean isCorrect() {
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();



        // Placing 3 ships on the board
        for (int i = 0; i < 3; i++) {
            generateShips();
        }

        sb.append(generateTiles());

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