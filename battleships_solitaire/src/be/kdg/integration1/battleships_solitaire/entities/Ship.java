package be.kdg.integration1.battleships_solitaire.entities;

public class Ship {
    private int x;  // Starting X coordinate
    private int y;  // Starting Y coordinate
    private ShipType shipType; // Type of ship
    private boolean isVertical; // Orientation

    public Ship(int x, int y, ShipType shipType, boolean isVertical) {
        this.x = x;
        this.y = y;
        this.shipType = shipType;
        this.isVertical = isVertical;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return shipType.getSize();
    }

    public boolean isVertical() {
        return isVertical;
    }
}
