package be.kdg.integration1.battleships_solitaire.entities;

public enum Difficulty {
    EASY(5, 5, 5, "Easy"),
    MEDIUM(7, 2, 4, "Medium"),
    HARD(9, 1, 1, "Hard");

    private final int BOARD_SIZE;
    private final int PENALTY_POINTS;
    private final int DEFAULT_HINTS;
    private final int NUMERIC_VALUE;
    private final String NAME;

    Difficulty(int boardSize, int penaltyPoints, int defaultHints, String name) {
        BOARD_SIZE = boardSize;
        NUMERIC_VALUE = ordinal() + 1;
        PENALTY_POINTS = penaltyPoints;
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

    public int getPenaltyPoints() {
        return PENALTY_POINTS;
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
        return null;
    }

    @Override
    public String toString() {
        return String.format("[%d] - %-6s | Board Size: %2d*%-2d", NUMERIC_VALUE, NAME, BOARD_SIZE, BOARD_SIZE);
    }
}
