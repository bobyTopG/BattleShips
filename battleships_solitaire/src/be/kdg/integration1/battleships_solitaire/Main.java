package be.kdg.integration1.battleships_solitaire;

import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Leaderboard leaderboard = new Leaderboard();
        Game game = new Game();
        game.start(leaderboard);
    }
}
