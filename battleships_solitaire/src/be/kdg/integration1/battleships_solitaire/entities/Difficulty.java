package be.kdg.integration1.battleships_solitaire.entities;

public enum Difficulty {
    EASY(5, 14, 5, "Easy"),
    MEDIUM(7, 26, 4, "Medium"),
    HARD(9, 40, 1, "Hard");

    private final int BOARD_SIZE;
    private final int STARTING_POINTS;
    private final int DEFAULT_HINTS;
    private final int NUMERIC_VALUE;
    private final String NAME;

    Difficulty(int boardSize, int startingPoints, int defaultHints, String name) {
        BOARD_SIZE = boardSize;
        NUMERIC_VALUE = ordinal() + 1;
        STARTING_POINTS = startingPoints;
        DEFAULT_HINTS = defaultHints;
        NAME = name;
    }

    public int getNumericValue() {
        return NUMERIC_VALUE;
    }

    public String getName() {
        return NAME;
    }

    public int getBoardSize() {
        return BOARD_SIZE;
    }

    public int getStartingPoints() {
        return STARTING_POINTS;
    }

    public int getDefaultHints() {
        return DEFAULT_HINTS;
    }

    public static Difficulty valueOf(int boardSize) {
        for (Difficulty d : Difficulty.values()) {
            if (d.getBoardSize() == boardSize) {
                return d;
            }
        }
        return EASY;
    }

    @Override
    public String toString() {
        return String.format("[%d] - %-6s | Board Size: %2d*%-2d", NUMERIC_VALUE, NAME, BOARD_SIZE, BOARD_SIZE);
    }
}
