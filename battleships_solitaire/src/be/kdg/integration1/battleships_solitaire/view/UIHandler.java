package be.kdg.integration1.battleships_solitaire.view;

import be.kdg.integration1.battleships_solitaire.entities.Difficulty;
import be.kdg.integration1.battleships_solitaire.entities.Player;

import java.time.LocalDate;

/**
 * This interface defines all methods used to show the UI to the user.
 * Current implementations include:
 * @see TerminalUIHandler
 */
public interface UIHandler {
    void showStartScreen();
    /** @author Vivian K. */
    String askForPlayerName();
    LocalDate askForBirthdate();
    boolean startNewGame();
    Difficulty chooseDifficulty();
    String getResponse();
    void setResponse(String response);
    void showMainMenu();
    void showPlayingOptions();
    void showHelpScreen();
    void showLeaderboard();
    void showLeaderboard(int amount);
    void showLeaderboardType(String name);
    void showLeaderboardType(int boardSize);
    void leaderboardChoice();
    void welcomePlayer(Player player);
    void awaitEnter();
    int chooseX(int limit);
    char chooseY(int limit);
    void endOfGame();
    boolean isUserSureToExit();
}