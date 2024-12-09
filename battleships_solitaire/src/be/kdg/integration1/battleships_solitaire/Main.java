package be.kdg.integration1.battleships_solitaire;

import java.sql.Date;

public class Main {
    public static void main(String[] args) {
        new PersistenceController().savePlayer(new Player("Teodor", Date.valueOf("2005-12-28")));
//        Leaderboard leaderboard = new Leaderboard();
//        new PersistenceController();
//        Game game = new Game();
//        game.start(leaderboard);
    }
}
