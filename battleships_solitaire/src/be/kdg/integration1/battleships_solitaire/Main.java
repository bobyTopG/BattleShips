package be.kdg.integration1.battleships_solitaire;

import java.sql.Date;

public class Main {
    public static void main(String[] args) {
        Leaderboard leaderboard = new Leaderboard();
        new PersistenceController();
        Game game = new Game();
        game.start(leaderboard);
    }
}
