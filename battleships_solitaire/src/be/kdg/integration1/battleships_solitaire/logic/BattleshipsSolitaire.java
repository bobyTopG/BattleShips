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
        player.authorize();
        uiHandler.welcomePlayer(player);
        do {
            selectChoice();
        } while (!"S".equals(uiHandler.getResponse()));
    }

    public void selectChoice() {
        uiHandler.showMainMenu();
        switch (uiHandler.getResponse()) {
            case "P", "PLAY", "1" -> {
                gameSession = new GameSession(player);
                gameSession.startGame();
                uiHandler.awaitEnter();
            }
            case "H", "HELP", "2" -> {
                uiHandler.showHelpScreen();
                uiHandler.awaitEnter();
            }
            case "L", "LEADERBOARD", "3" -> {
                uiHandler.showLeaderboard();
            }
            case "E", "EXIT", "Q", "QUIT", "4" -> {
                uiHandler.setResponse("S");
            }
        }
    }
}
