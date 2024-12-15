package be.kdg.integration1.battleships_solitaire.entities;

public class PlayerTile extends Tile {
    private boolean isRevealed;

    public PlayerTile(int x, char y) {
        super(x, y);
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
        isRevealed = true;
        this.type = type;
    }

    @Override
    public void markAs(Type type) {
        if (isRevealed)
            throw new IllegalStateException("Tile is already revealed");
        this.type = type;
    }

    @Override
    public String toString() {
        return switch (type) {
            case WATER -> isRevealed ? "≈" : "~";
            case SHIP_PART -> isRevealed ? "#" : "X";
            case null -> "·";
        };
    }
}