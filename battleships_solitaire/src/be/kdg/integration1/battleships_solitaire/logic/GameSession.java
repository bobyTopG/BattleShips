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

    private Player player;
    private Board board;
    private UIHandler uiHandler;
    private PersistenceController persistenceController;

    private String shorthandCommand;
    private boolean isShorthandUsed;
    private boolean isNewGame;

    private Leaderboard leaderboard;

    public GameSession(Player player) {
        this.player = player;
        uiHandler = new TerminalUIHandler();
        persistenceController = new PersistenceController();
        isNewGame = uiHandler.startNewGame();
        board = isNewGame ?
                new Board(uiHandler.chooseDifficulty().getBoardSize()) :
                persistenceController.fetchGame(player);
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
                    // TODO: save the game in the database
                    continue;
                }
                case "E", "EXIT", "END" -> {
                    break loop;
                }
            }

            switch (response) {
                case "W", "WATER"       -> board.markTileAsWater(x - 1, Utility.convertCoordinate(y) - 1);
                case "M", "SHIP", "X"   -> board.markTileAsShip(x - 1, Utility.convertCoordinate(y) - 1);
                case "U", "UNMARK"      -> board.unmarkTile(x - 1, Utility.convertCoordinate(y) - 1);
                case "R", "REVEAL"      -> board.revealRandomTile();
            }
            isShorthandUsed = false;
            board.updateDuration();
        }
        if (board.isGameOver()) {
            uiHandler.endOfGame();
            board.setDuration(Duration.ZERO);
            System.out.println(board.answersToString());
            // TODO: save the game in the database
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
//
//    public String getElapsedTime() {
//        if (startTime == 0) {
//            return "Timer has not started.";
//        }
//        long elapsedTime = (endTime > 0 ? endTime : System.currentTimeMillis()) - startTime;
//        return String.format("Final elapsed time: %.2f seconds", elapsedTime / 1000.0);
//    }
//
//}
}