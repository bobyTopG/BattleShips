package be.kdg.integration1.battleships_solitaire.entities;

public class Tile {
    private int x;
    private int y;
    private boolean isShip;
    private boolean isWater;
    private boolean isHint;
    private boolean isRevealed;




    public Tile(int x, int y ) {
        this.x = x;
        this.y = y;

    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isShip() {
        return isShip;
    }
    public boolean isWater() {
        return isWater;
    }
    public boolean isHint() {
        return isHint;
    }

    public void setHint(boolean isHint) {
        this.isHint = isHint;
    }

    public void setShip(boolean isShip) {
        this.isShip = isShip;
    }
    public void setWater(boolean isWater) {
        this.isWater = isWater;
    }
}
