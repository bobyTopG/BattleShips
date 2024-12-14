package be.kdg.integration1.battleships_solitaire.entities;

import be.kdg.integration1.battleships_solitaire.logic.Utility;

/**
 * {@code Tile} is used to represent the tiles in the board of the game
 * and when fetching them from the database.
 * Types:
 * {@code WATER} means that the tile is NOT part of a ship &
 * {@code SHIP_PART} means that the tile is part of a ship.
 */
public class Tile {

    /** Enumeration for the types of ships **/
    public enum Type {
        WATER(true),
        SHIP_PART(false);

        private final boolean isShip;

        Type(boolean isShip) {
            this.isShip = isShip;
        }

        public boolean isShip() {
            return isShip;
        }

        public Type getOpposite() {
            return switch (this) {
                case SHIP_PART -> WATER;
                case WATER -> SHIP_PART;
            };
        }
    }

    private int x;
    private char y;
    /** If it is {@code null}, then the tile is unmarked.**/
    private Type type;
    private boolean isRevealed;

    @Deprecated
    private boolean isHint;

    public Tile(int x, char y) {
        this.x = x;
        this.y = y;
    }

    public Tile(int x, int y) {
        this.x = x;
        this.y = Utility.convertCoordinate(y);
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

    public int getX() {
        return x;
    }

    public char getY() {
        return y;
    }

    public Type getType() {
        return type;
    }

    public boolean isShip() {
        if (type == null) return false;
        return type.isShip;
    }

    public boolean isWater() {
        if (type == null) return false;
        return !type.isShip;
    }

    public boolean isHint() {
        return isHint;
    }

    public void setHint(boolean isHint) {
        this.isHint = isHint;
    }

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
            case null -> " ";
        };
    }
}