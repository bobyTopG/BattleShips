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
        int size = rand.nextInt(2, 5); // Ship size between 2 and 4 tiles
        boolean isCreated = false;

        while (!isCreated) {
            boolean isVertical = rand.nextBoolean();
            int startX = rand.nextInt(boardSize - size + 1);
            int startY = rand.nextInt(boardSize - size + 1);

            boolean canPlace = true;

            // Check if the ship can be placed without overlapping or touching other ships
            for (int i = 0; i < size; i++) {
                int cx = isVertical ? startX + i : startX;
                int cy = isVertical ? startY : startY + i;

                if (tiles[cx][cy] == '□') {
                    canPlace = false;
                    break;
                }

                // Check surrounding tiles to ensure no touching
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int adjX = cx + dx;
                        int adjY = cy + dy;
                        if (adjX >= 0 && adjX < boardSize && adjY >= 0 && adjY < boardSize) {
                            if (tiles[adjX][adjY] == '□') {
                                canPlace = false;
                                break;
                            }
                        }
                    }
                    if (!canPlace) break;
                }
                if (!canPlace) break;
            }

            // Place the ship if there's no overlap and no touching
            if (canPlace) {
                for (int i = 0; i < size; i++) {
                    int cx = isVertical ? startX + i : startX;
                    int cy = isVertical ? startY : startY + i;
                    tiles[cx][cy] = '□';
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

        // Add column numbers
        sb.append("    ");
        for (int col = 1; col <= boardSize; col++) {
            sb.append(String.format("%2d ", col)); // Column numbers
        }
        sb.append("\n");

        // Add top border
        sb.append("   ");
        for (int i = 0; i < boardSize; i++) {
            sb.append("- -");
        }
        sb.append(" -\n");

        // Randomly place 6 hint tiles (~)
        Random rand = new Random();
        int placed = 0;
        while (placed < 6) {
            int x = rand.nextInt(boardSize);
            int y = rand.nextInt(boardSize);

            // Only place a hint if it's not already a hint or ship
            if (tiles[x][y] != '□' && tiles[x][y] != '~') {
                tiles[x][y] = '~';  // Place the hint tile (~)
                placed++;
            }
        }

        // Add grid with tiles and ship counts per row
        for (int i = 0; i < boardSize; i++) {
            int rowShipCount = 0; // To store the number of ships in the current row
            sb.append(String.format("%2d |", i + 1)); // Row number

            for (int j = 0; j < boardSize; j++) {
                // Count the ships in this row
                if (tiles[i][j] == '□') {
                    rowShipCount++;
                }
                sb.append(tiles[i][j] == '□' ? " □ " : tiles[i][j] == '~' ? " ~ " : " # ");
            }

            sb.append("| " + rowShipCount); // Add the ship count for the current row
            sb.append("\n");
        }

        // Add bottom border
        sb.append("   ");
        for (int i = 0; i < boardSize; i++) {
            sb.append("- -");
        }
        sb.append(" -\n");

        // Add column ship count
        sb.append("   ");
        for (int j = 0; j < boardSize; j++) {
            int colShipCount = 0;
            for (int i = 0; i < boardSize; i++) {
                if (tiles[i][j] == '□') {
                    colShipCount++;
                }
            }
            sb.append(String.format("%2d ", colShipCount)); // Ship count for each column
        }
        sb.append("\n");

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
        for (int i = 0; i < 5; i++) {
            generateShips();
        };

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