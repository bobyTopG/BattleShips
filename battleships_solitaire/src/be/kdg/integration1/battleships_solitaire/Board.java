package be.kdg.integration1.battleships_solitaire;

import java.sql.Timestamp;

public class Board {

    private int boardSize;
    private int score;
    private Timestamp start;
    private Timestamp end;
    private int duration;


    public Board(int boardSize, int score, Timestamp start, Timestamp end, int duration) {
        this.boardSize = boardSize;
        this.score = score;
        this.start = start;
        this.end = end;
        this.duration = duration;
    }

    public void generateShips(){};

    public void getRandomShip(){};

    public void generateTiles(){};

    public void revealTile(){};

    public void correctTile(){};

    public void updateTile(Tile tile){};

    public boolean isFullyMarked(){
        return false;
    };

    public int getBoardSize() {
        return boardSize;
    }

    public boolean isCorrect(){
        return false;
    }

    @Override
    public String toString() {
        return "Board{" +
                "boardSize=" + boardSize +
                '}';
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