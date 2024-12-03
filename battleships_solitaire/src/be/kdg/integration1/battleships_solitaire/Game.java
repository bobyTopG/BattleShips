package be.kdg.integration1.battleships_solitaire;

public class Game {

    private GameController gameController;

    public void start() {
        Board board = new Board(10 , 5 , 1);
        System.out.println(board.toString());
    }

}
