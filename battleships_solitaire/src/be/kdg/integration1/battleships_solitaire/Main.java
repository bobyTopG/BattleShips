package be.kdg.integration1.battleships_solitaire;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
       GameController gameController = new GameController();
       gameController.startGame(5);
    }
}