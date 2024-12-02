package be.kdg.integration1.battleships_solitaire.scripts;

public class Board {

    private int boardSize;

    public Board(int boardSize) {
        this.boardSize = boardSize;
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