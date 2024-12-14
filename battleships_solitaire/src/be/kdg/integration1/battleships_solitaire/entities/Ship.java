package be.kdg.integration1.battleships_solitaire.entities;

import be.kdg.integration1.battleships_solitaire.logic.Utility;

/**
 * {@code Ship} is used to represent the ships in the game
 * and when fetching them from the database.
 */
public class Ship {

    /** Enumeration for the types of ships **/
    public enum Type {
        SUBMARINE(1),
        DESTROYER(2),
        CRUISER(3),
        BATTLESHIP(4);
        // CARRIER(5);

        private final int size;

        Type(int size) {
            this.size = size;
        }

        public int getSize() {
            return size;
        }

        public static Type getFromSize(int size) {
            for (Type type : Type.values())
                if (type.getSize() == size) return type;
            throw new IllegalArgumentException("Ship type not found for size: " + size);
        }
    }

    private final int x;
    private final char y;
    private final Type type;
    private final Boolean isVertical;

    private int remainingParts;

    public Ship(int x, char y, Type type, boolean isVertical) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.isVertical = isVertical;
        this.remainingParts = type.size;
    }

    public Ship(int x, char y, int size, boolean isVertical) {
        this(x, y, Type.getFromSize(size), isVertical);
    }

    public Ship(int x, int y, Type type, boolean isVertical) {
        this(x, Utility.convertCoordinate(y), type, isVertical);
    }

    public Ship(int x, int y, int size, boolean isVertical) {
        this(x, y, Type.getFromSize(size), isVertical);
    }

    public int getX() {
        return x;
    }

    public char getCharY() {
        return y;
    }

    public int getIntY() {
        return Utility.convertCoordinate(y);
    }

    public int getSize() {
        return type.size;
    }

    public boolean isVertical() {
        return isVertical;
    }

    public int getRemainingParts() {
        return remainingParts;
    }

    public void incrementRemainingParts() {
        if (remainingParts == type.size)
            throw new IllegalStateException("Ship is already fully hidden");
        remainingParts++;
    }

    public void decrementRemainingParts() {
        if (remainingParts == 0)
            throw new IllegalStateException("Ship already has no remaining parts");
        remainingParts--;
    }

    @Override
    public String toString() {
        int foundParts = type.size - remainingParts;
        return String.format("%s%s",
                "#".repeat(foundParts),
                "_".repeat(remainingParts)
        );
    }

    public static int getShipAmountByDifficulty(Difficulty difficulty) {
        return switch (difficulty) {
            case EASY -> 5;
            case MEDIUM -> 8;
            case HARD -> 10;
        };
    }
}
