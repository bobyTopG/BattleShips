package be.kdg.integration1.battleships_solitaire.entities;

public enum Difficulty {
    EASY(7, 1, "Easy"),
    MEDIUM(10, 2, "Medium"),
    HARD(15, 3, "Hard");

    private final int BOARD_SIZE;
    private final int NUMERIC_VALUE;
    private final String NAME;

    Difficulty(int boardSize, int numericValue, String name) {
        BOARD_SIZE = boardSize;
        NUMERIC_VALUE = numericValue;
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

    @Override
    public String toString() {
        return String.format("[%d] - %-6s | Board Size: %2d*%-2d", NUMERIC_VALUE, NAME, BOARD_SIZE, BOARD_SIZE);
    }
}
