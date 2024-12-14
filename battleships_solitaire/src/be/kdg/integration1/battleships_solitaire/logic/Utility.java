package be.kdg.integration1.battleships_solitaire.logic;

public final class Utility {

    public static char convertCoordinate(int n) {
        return (char) (n + 'A' - 1);
    }

    public static int convertCoordinate(char c) {
        return c - 'A' + 1;
    }

}