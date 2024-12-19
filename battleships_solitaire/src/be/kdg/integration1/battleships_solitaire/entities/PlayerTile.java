package be.kdg.integration1.battleships_solitaire.entities;

public class PlayerTile extends Tile {
    private boolean isRevealed;

    public PlayerTile(int x, char y) {
        super(x, y);
    }

    public PlayerTile(int x, char y, boolean isRevealed, Tile.Type type) {
        super(x, y);
        this.isRevealed = isRevealed;
        this.type = type;
    }

    public PlayerTile(int x, int y) {
        super(x, y);
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealedAs(Type type) {
        if (type == null)
            throw new IllegalArgumentException("Revealed tile type cannot be null");
        markAs(type);
        isRevealed = true;
    }

    public boolean isWater() {
        if (type == null) return false;
        return !type.isShip();
    }

    /** @throws IllegalStateException */
    @Override
    public void markAs(Type type) {
        if (isRevealed)
            throw new IllegalStateException("Tile is already revealed");
        this.type = type;
        if (getCorrespondingShip() != null) {
            if (type == Type.SHIP_PART) {
                getCorrespondingShip().decrementRemainingParts();
            } else {
                getCorrespondingShip().incrementRemainingParts();
            }
        }
    }

    @Override
    public String toString() {
        return switch (type) {
            case WATER -> isRevealed ? "\033[1;36m≈\033[0m" : "\033[1;34m~\033[0m";
            case SHIP_PART -> isRevealed ? "\033[1;31m▨\033[0m" : "\033[1;31mX\033[0m";
            case null -> "\033[1;37m·\033[0m";
        };
    }
}