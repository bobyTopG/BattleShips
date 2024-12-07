package be.kdg.integration1.battleships_solitaire;

import java.util.Scanner;

public class Game {

    private Board board;
    private Player player;
    private SimpleMenu menu;
    private PersistenceController persistenceController;


    public void start(Leaderboard leaderboard) {
        Scanner scanner = new Scanner(System.in);
        System.out.printf("\n\nWelcome to Battleships Solitaire Game! \nPlayer name: ");
        String name = scanner.nextLine();

        player = new Player(name);
        menu = new SimpleMenu();


        if (leaderboard.getPlayers().stream().anyMatch(p -> p.getName().equalsIgnoreCase(player.getName()))) {
            System.out.println("Welcome again! Happy to see you =)\n");
        } else {
            leaderboard.addPlayer(player);
            System.out.printf("Hello %s! You were added to the board =0.\n", player.getName());
            System.out.println();
        }

        GameController gameController = new GameController();
        System.out.print("Choose between the 3 options =) \n1- Easy (7 points)\n2- Medium (10 points)\n3- Hard (15 points)\nOption number: ");
        int option = scanner.nextInt();

        while (option < 1 || option > 3) {
            System.out.println("Invalid option. Please try again.");
            option = scanner.nextInt();
        }

        if (option == 1) {
            board = new Board(7);  // Easy
        } else if (option == 2) {
            board = new Board(10);  // Medium
        } else {
            board = new Board(15); // Hard
        }
        gameController.startGame(board, player, leaderboard, menu);

    }
}
