package be.kdg.integration1.battleships_solitaire.logic;

public final class Utility {

    public static char convertCoordinate(int n) {
        return (char) (n + 'A' - 1);
    }

    public static int convertCoordinate(char c) {
        return c - 'A' + 1;
    }

    public static boolean isXNotInBounds(int x, int limit) {
        return x < 1 || x > limit;
    }

    public static boolean isYNotInBounds(char y, int limit) {
        return y < 'A' || y > 'A' + limit - 1;
    }

    public static void repeat(int n, Runnable action) {
        for (int i = 0; i < n; i++) {
            action.run();
        }
    }

}