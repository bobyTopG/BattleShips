package be.kdg.integration1.battleships_solitaire.logic;

import be.kdg.integration1.battleships_solitaire.entities.Board;
import be.kdg.integration1.battleships_solitaire.entities.Leaderboard;
import be.kdg.integration1.battleships_solitaire.entities.Player;
import be.kdg.integration1.battleships_solitaire.view.SimpleMenu;
import be.kdg.integration1.battleships_solitaire.view.TerminalUIHandler;
import be.kdg.integration1.battleships_solitaire.view.UIHandler;

import java.util.Scanner;

/**
 * {@code BattleshipsSolitaire} is where the game loop logic is kept.
 * The game starts from here and the user is given choices and directions to play the game with.
 */
public class BattleshipsSolitaire {

    private Board board;
    private Player player;
    private SimpleMenu menu;
    private PersistenceController persistenceController;

    private GameController gameController;
    private UIHandler uiHandler;

    public BattleshipsSolitaire() {
        uiHandler = new TerminalUIHandler();
    }

    /**
     * Begins the Battleships Solitaire game.
     * Starts a loop with prompts to the user what to do next.
     * {@link GameController}
     */
    public void start() {
        uiHandler.showStartScreen();

        Leaderboard leaderboard = null; // get this from
        Scanner scanner = new Scanner(System.in);
        System.out.printf("Welcome to Battleships Solitaire Game! \nPlayer name: ");
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
