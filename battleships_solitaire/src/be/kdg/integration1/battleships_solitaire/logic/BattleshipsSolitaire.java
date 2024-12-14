package be.kdg.integration1.battleships_solitaire.logic;

import be.kdg.integration1.battleships_solitaire.entities.Board;
import be.kdg.integration1.battleships_solitaire.entities.Player;
import be.kdg.integration1.battleships_solitaire.view.SimpleMenu;
import be.kdg.integration1.battleships_solitaire.view.TerminalUIHandler;
import be.kdg.integration1.battleships_solitaire.view.UIHandler;

/**
 * {@code BattleshipsSolitaire} is where the game loop logic is kept.
 * The game starts from here and the user is given choices and directions to play the game with.
 */
public class BattleshipsSolitaire {

    private Board board;
    private Player player;

    private GameSession gameSession;
    private UIHandler uiHandler;
    private PersistenceController persistenceController;

    public BattleshipsSolitaire() {
        uiHandler = new TerminalUIHandler();
        persistenceController = new PersistenceController();
    }

    /**
     * Begins the Battleships Solitaire game.
     * Starts a loop with prompts to the user what to do next.
     * {@link GameSession}
     */
    public void start() {
        uiHandler.showStartScreen();
        player = persistenceController.fetchPlayer(uiHandler.askForPlayerName());

        while (!"QUIT".equals(uiHandler.getResponse())) {
            gameSession = new GameSession(player);
            gameSession.startGame();
        }

        // TODO: we should DEFINITELY change this to something easier
//        if (leaderboard.getPlayers().stream().anyMatch(p -> p.getName().equalsIgnoreCase(player.getName()))) {
//            System.out.printf("%nWelcome back, %s! Happy to see you =) %n", player.getName());
//        } else {
//            leaderboard.addPlayer(player);
//            System.out.printf("%nHello %s! You were added to the game =0 %n", player.getName());
//        }
    }
}
