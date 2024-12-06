package be.kdg.integration1.battleships_solitaire;

public enum ShipType {
    DESTROYER(2), SUBMARINE(3), BATTLESHIP(4), CARRIER(5);

    private final int size;

    ShipType(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
