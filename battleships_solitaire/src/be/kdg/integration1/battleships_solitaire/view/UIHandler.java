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
    LocalDate askForBirthday();
    boolean startNewGame();
    Difficulty chooseDifficulty();
    String getResponse();
}