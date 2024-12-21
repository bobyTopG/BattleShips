package be.kdg.integration1.battleships_solitaire.logic;

import be.kdg.integration1.battleships_solitaire.entities.Board;
import be.kdg.integration1.battleships_solitaire.entities.Leaderboard;
import be.kdg.integration1.battleships_solitaire.entities.Player;
import be.kdg.integration1.battleships_solitaire.view.TerminalUIHandler;
import be.kdg.integration1.battleships_solitaire.view.UIHandler;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * {@code GameController} maps out the responsibilities to the different entities and everything else in the system.
 * It is to be used without any actual implemented logic.
 * This class should only call other methods.
 */
public class GameSession {

    private final Player player;
    private final Board board;
    private Leaderboard leaderboard;
    private final UIHandler uiHandler;
    private final PersistenceController persistenceController;

    private String shorthandCommand;
    private boolean isShorthandUsed;
    private boolean isNewGame;
    private boolean isJustSaved;

    public GameSession(Player player) {
        this.player = player;
        uiHandler = new TerminalUIHandler();
        persistenceController = new PersistenceController();
        isNewGame = uiHandler.startNewGame();
        if (isNewGame) {
            board = new Board(uiHandler.chooseDifficulty().getBoardSize());
        } else {
            Board fetchedGame = persistenceController.fetchGame(player);
            if (fetchedGame == null) {
                System.out.println("No game to load! Starting a new game.");
                board = new Board(uiHandler.chooseDifficulty().getBoardSize());
                isNewGame = true;
            } else {
                board = fetchedGame;
            }
        }
    }

    public void startGame() {
        board.setCurrentTime(LocalDateTime.now());
        if (isNewGame) {
            board.setStartTime(board.getCurrentTime());
            board.setDuration(Duration.ZERO);
        }
        loop:
        while (!board.isGameOver()) {
            System.out.println(board);
            uiHandler.showPlayingOptions();
            int x = -1;
            char y = '\0';

            String response = uiHandler.getResponse();
            if (response.length() == 3) {
                shorthandCommand = response.substring(1);
                response = String.valueOf(uiHandler.getResponse().charAt(0));
                isShorthandUsed = true;
            }

            switch (response) {
                case "W", "WATER", "X", "M", "SHIP", "U", "UNMARK" -> {
                    if (isShorthandUsed) {
                        try {
                            x = Integer.parseInt(String.valueOf(shorthandCommand.charAt(0)));
                            y = shorthandCommand.charAt(1);
                        } catch (Exception e) {
                            try {
                                x = Integer.parseInt(String.valueOf(shorthandCommand.charAt(1)));
                                y = shorthandCommand.charAt(0);
                            } catch (Exception ignored) {}
                        }
                        if (Utility.isXNotInBounds(x, board.getBoardSize())
                                || Utility.isYNotInBounds(y, board.getBoardSize())) {
                            System.out.println("X or Y out of bounds");
                            isShorthandUsed = false;
                            continue;
                        }
                    } else {
                        x = uiHandler.chooseX(board.getBoardSize());
                        y = uiHandler.chooseY(board.getBoardSize());
                    }
                }
                case "S", "SAVE" -> {
                    board.updateDuration();
                    persistenceController.saveGame(player, board);
                    isJustSaved = true;
                    continue;
                }
                case "E", "EXIT", "END" -> {
                    if (isJustSaved) {
                        break loop;
                    } else if (uiHandler.isUserSureToExit()) {
                        break loop;
                    }
                }
            }

            switch (response) {
                case "W", "WATER"     -> board.markTileAsWater(x - 1, Utility.convertCoordinate(y) - 1);
                case "M", "SHIP", "X" -> board.markTileAsShip(x - 1, Utility.convertCoordinate(y) - 1);
                case "U", "UNMARK"    -> board.unmarkTile(x - 1, Utility.convertCoordinate(y) - 1);
                case "R", "REVEAL"    -> board.revealRandomTile();
            }

            isShorthandUsed = false;
            isJustSaved = false;
            board.updateDuration();
        }
        if (board.isGameOver()) {
            uiHandler.endOfGame();
            System.out.println(board.answersToString());
            board.setEndTime(LocalDateTime.now());
            persistenceController.saveGame(player, board);
        }
    }


//    public void loadLeaderBoard() {
//         leaderboard = new Leaderboard();
//
////        // Create players
////        Player player1 = new Player("Alice", 1);
////        Player player2 = new Player("Bob", 2);
////        Player player3 = new Player("Charlie", 3);
////
////        // Simulate players scoring
////        player1.increaseScore(100);
////        player2.increaseScore(200);
////        player3.increaseScore(150);
////
////        // Add players to the leaderboard
////        leaderboard.addPlayer(player1);
////        leaderboard.addPlayer(player2);
////        leaderboard.addPlayer(player3);
//
//        // Display the leaderboard
//        leaderboard.displayLeaderboard();
//
//        // Get and display the top player
//        Player topPlayer = leaderboard.getTopPlayer();
//        if (topPlayer != null) {
//            System.out.println("\nTop player: " + topPlayer.getName() + " with score: " /*+ topPlayer.getScore()*/);
//        }
//
//    }
}