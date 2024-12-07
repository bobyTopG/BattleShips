package be.kdg.integration1.battleships_solitaire;

import java.util.Scanner;

public class Game {

    private Leaderboard level;
    private Board board;
    private Player player;
    private SimpleMenu menu;
    private PersistenceController persistenceController;


    public void start(Leaderboard leaderboard) {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("Welcome to Battleships Solitaire Game! \n Player name: ");
        String name = scanner.nextLine();

        player = new Player(name);
        board = new Board(10);
        menu = new SimpleMenu();




        if (leaderboard.getPlayers().stream().anyMatch(p -> p.getName().equalsIgnoreCase(player.getName()))) {
            System.out.println("Welcome again! Happy to see you");
        } else {
            leaderboard.addPlayer(player);
            System.out.printf("Hello %s! New player\n", player.getName());
            System.out.println();
        }

        GameController gameController = new GameController();
        int difficulty = 10;
        if (difficulty >= 5) {
            gameController.startGame(board, player, leaderboard, menu);
        }

    }
}
