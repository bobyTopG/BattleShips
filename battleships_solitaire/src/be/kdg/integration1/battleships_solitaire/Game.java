package be.kdg.integration1.battleships_solitaire;

public class Game {

    private GameController gameController;

    public void start() {
        GameController gameController = new GameController()
        gameController.startGame();
    }

}
