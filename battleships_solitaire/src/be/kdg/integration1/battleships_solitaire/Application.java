package be.kdg.integration1.battleships_solitaire;

import be.kdg.integration1.battleships_solitaire.logic.BattleshipsSolitaire;

/**
 * This class only has the {@code main()} method used to start up the program.
 * It does not provide any logic itself.
 */
public class Application {
    public static void main(String[] args) {
        new BattleshipsSolitaire().start();
//        Leaderboard leaderboard = new Leaderboard();
//        new PersistenceController();
//        BattleshipsSolitaire game = new BattleshipsSolitaire();
//        game.start(leaderboard);
    }
}
