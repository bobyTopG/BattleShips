package be.kdg.integration1.battleships_solitaire;

public class Tile {

    private int x;
    private int y;
    private boolean isShip;

    public Tile(int x, int y, boolean isShip) {
        this.x = x;
        this.y = y;
        this.isShip = isShip;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

//+«create»(int ,int)
//-x: int
//-y: int
//-isShip: boolean


//+getX(): int
//+getY(): int