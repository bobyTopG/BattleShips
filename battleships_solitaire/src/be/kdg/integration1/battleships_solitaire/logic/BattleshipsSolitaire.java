package be.kdg.integration1.battleships_solitaire.logic;

import be.kdg.integration1.battleships_solitaire.entities.Board;
import be.kdg.integration1.battleships_solitaire.entities.Difficulty;
import be.kdg.integration1.battleships_solitaire.entities.Leaderboard;
import be.kdg.integration1.battleships_solitaire.entities.Player;
import be.kdg.integration1.battleships_solitaire.view.SimpleMenu;
import be.kdg.integration1.battleships_solitaire.view.TerminalUIHandler;
import be.kdg.integration1.battleships_solitaire.view.UIHandler;

import java.util.InputMismatchException;
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
        System.out.println("Welcome to Battleships Solitaire Game!");

        String name;
        // validate the name by checking if it's either empty or contains characters that aren't letters
        // the regex is matching all lowercase and uppercase letters with at least 1 occurrence
        do {
            System.out.print("Please enter your name: ");
            name = scanner.nextLine();
            if (name.isEmpty()) {
                System.out.println("Name can't be empty. Please try again!");
            } else if (!name.matches("[a-zA-Z]+")) {
                System.out.println("Your name may only contain letters. Please try again!");
            }
        } while (name.isEmpty() || !name.matches("[a-zA-Z]+"));


        player = new Player(name);
        menu = new SimpleMenu();

        // TODO we should DEFINITELY change this to something easier
        if (leaderboard.getPlayers().stream().anyMatch(p -> p.getName().equalsIgnoreCase(player.getName()))) {
            System.out.printf("%nWelcome back, %s! Happy to see you =) %n", player.getName());
        } else {
            leaderboard.addPlayer(player);
            System.out.printf("%nHello %s! You were added to the game =0 %n", player.getName());
        }

        GameController gameController = new GameController();
        System.out.println("Choose your difficulty:");
        System.out.printf("1 - %-7s (Board Size: %2d)%n", "Easy", Difficulty.EASY.getBoardSize());
        System.out.printf("2 - %-7s (Board Size: %2d)%n", "Medium", Difficulty.MEDIUM.getBoardSize());
        System.out.printf("3 - %-7s (Board Size: %2d)%n", "Hard", Difficulty.HARD.getBoardSize());

        // difficulty selection, checks if option is valid
        // if player enters anything other than an int, we catch the exception
        int option = 0;
        do {
            try {
                System.out.print("Option number: ");
                option = scanner.nextInt();
                if (option < 1 || option > 3) {
                    System.out.println("Invalid choice. Please select a difficulty between 1 and 3.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter the difficulty using a number between 1 and 3.");
                // remove the invalid input from the scanner, otherwise it keeps going on forever
                scanner.nextLine();
            }
        } while (option < 1 || option > 3);

        // uses selected option to create new board
        // board size is determined by the board size defined in the Difficulty enum
        switch (option) {
            case 1:
                board = new Board(Difficulty.EASY.getBoardSize()); // Easy
                break;
            case 2:
                board = new Board(Difficulty.MEDIUM.getBoardSize()); // Medium
                break;
            case 3:
                board = new Board(Difficulty.HARD.getBoardSize()); // Hard
                break;
        }

        gameController.startGame(board, player, leaderboard, menu);

    }
}
