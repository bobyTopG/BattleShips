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

    public void generateTiles() {
    }

    ;

    public void revealTile() {
    }

    ;

    public void correctTile() {
    }

    ;

    public void updateTile(Tile tile) {
    }

    ;

    public boolean isFullyMarked() {
        return false;
    }

    ;

    public int getBoardSize() {
        return boardSize;
    }

    public boolean isCorrect() {
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (i == 0 || i == boardSize - 1) {
                    tiles[i][j] = '-';
                } else if (j == 0 || j == boardSize - 1) {
                    tiles[i][j] = '|';
                } else {
                    tiles[i][j] = '~';

                }

            }
        }

        // Placing 3 ships on the board
        for (int i = 0; i < 3; i++) {
            generateShips();
        }


        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                sb.append(tiles[i][j]).append(" ");
            }
            sb.append("\n");
        }
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