package be.kdg.integration1.battleships_solitaire.entities;

public enum Difficulty {
    EASY(7),
    MEDIUM(10),
    HARD(15);

    private final int BOARD_SIZE;

    Difficulty(int boardSize) {
        BOARD_SIZE = boardSize;
    }

    public int getBoardSize() {
        return BOARD_SIZE;
    }

}
