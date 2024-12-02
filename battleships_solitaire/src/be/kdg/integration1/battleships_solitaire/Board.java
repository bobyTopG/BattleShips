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
        boolean isVertical = rand.nextBoolean();
        boolean isOneTile = rand.nextBoolean();
        int size = rand.nextInt(2, 5);
        boolean isCreated = false;

        while (!isCreated) {
            if (isOneTile) {
                int x = rand.nextInt(
                        boardSize);
                int y = rand.nextInt(
                        boardSize);
                tiles[x][y] = '□';
                isCreated = true;
            } else if (isVertical) {
                int x = rand.nextInt(
                        boardSize);
                if (x + 5 <
                        boardSize || x - 5 > 0) {
                    int y = 0;
                    for (int i = 0; i < size; i++) {
                        y += rand.nextBoolean() ? 1 : -1;
                        tiles[x][y] = '□';
                    }
                    isCreated = true;
                }

            } else if (!isVertical) {
                int y = rand.nextInt(
                        boardSize);
                if (y + 5 <
                        boardSize || y - 5 > 0) {
                    int x = 0;
                    for (int i = 0; i < size; i++) {
                        x += rand.nextBoolean() ? 1 : -1;
                        tiles[x][y] = '□';
                    }
                    isCreated = true;
                }
            }
        }


    }

    ;

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

        generateShips();

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