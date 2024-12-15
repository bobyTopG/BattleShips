package be.kdg.integration1.battleships_solitaire.logic;

import be.kdg.integration1.battleships_solitaire.entities.Player;
import be.kdg.integration1.battleships_solitaire.view.TerminalUIHandler;
import be.kdg.integration1.battleships_solitaire.view.UIHandler;

/**
 * {@code BattleshipsSolitaire} is where the game loop logic is kept.
 * The game starts from here and the user is given choices and directions to play the game with.
 */
public class BattleshipsSolitaire {

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
        uiHandler.welcomePlayer(player);
        outerLoop:
        do {
            uiHandler.showMainMenu();
            switch (uiHandler.getResponse()) {
                case "P", "PLAY" -> {
                    gameSession = new GameSession(player);
                    gameSession.startGame();
                }
                case "H", "HELP" -> {
                    uiHandler.showHelpScreen();
                    uiHandler.awaitEnter();
                }
                case "L", "LEADERBOARD" -> {
                    // uiHandler.showLeaderboard(persistenceController.fetchLeaderboard());
                    uiHandler.awaitEnter();
                }
                case "E", "EXIT", "Q", "QUIT" -> {
                    break outerLoop;
                }
            }

        } while (!"S".equals(uiHandler.getResponse()));
    }
}
