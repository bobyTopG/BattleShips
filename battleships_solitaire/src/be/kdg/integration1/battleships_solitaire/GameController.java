package be.kdg.integration1.battleships_solitaire;

import java.util.Scanner;

public class GameController {

    private Board board;
    private PersistenceController persistenceController;
    boolean gameEnded = false;
    private Leaderboard leaderboard;
    private long startTime;
    private long endTime;

    //Constructor
    public GameController() {
    }

    public void startGame(Board board , Player player , Leaderboard leaderboard , SimpleMenu menu) {
        Scanner scanner = new Scanner(System.in);
        Game game = new Game();
        // Place ships and generate tiles
        board.generateShips();
        board.generateTiles();

            startTime = System.currentTimeMillis();
            System.out.println(board);
            boolean gameEnded = false;

            while (!board.isGameOver() && !gameEnded) {
                System.out.print(menu.menuOptions());

                char command = scanner.nextLine().charAt(0);

                if (command == 'a' || command == 's' || command == 'r') {
                    System.out.print(" X: ");
                    int x = Integer.parseInt(scanner.nextLine()) - 1;
                    System.out.print(" Y: ");
                    int y = Integer.parseInt(scanner.nextLine()) - 1;

                    if (command == 's') {
                        menu.tileToWater(board, x, y);
                    } else if (command == 'r') {
                        menu.removeTile(board, x, y);
                    } else {
                        menu.addShip(board, x, y);
                    }
                    System.out.println(board);
                } else if (command == 'e') {
                    System.out.print(menu.stopAndRevtileTiles(board));
                    gameEnded = true;
                } else if (command == 'h') {
                    System.out.printf(menu.help());
                } else if (command == 'l') {
                    board.revealTile();
                    System.out.println(board);
                } else {
                    System.out.println("Invalid command");
                }

            }

            endTime = System.currentTimeMillis();

            if (!gameEnded) {
                // player.increaseScore(board.getBoardSize());
                System.out.println(leaderboard.toString());
            }

        if (gameEnded) {
            System.out.println("\n--------- Game Over ---------");
            System.out.println("Hey! Next time don't quit =(\n");
        } else {
            System.out.println("--------- You win =) ---------");
            System.out.println(getElapsedTime() + "\n");
        }
            System.out.println(leaderboard.toString());

            System.out.println("Another game? y/n");
            if (scanner.nextLine().charAt(0) == 'y') {
                game.start(leaderboard);
            }
        }



    public void endGame() {
    }

    public void saveGame() {
    }

    public void loadGame() {
    }

    public void loadPlayer(Player player) {
    }

    public void loadLeaderBoard() {
         leaderboard = new Leaderboard();

//        // Create players
//        Player player1 = new Player("Alice", 1);
//        Player player2 = new Player("Bob", 2);
//        Player player3 = new Player("Charlie", 3);
//
//        // Simulate players scoring
//        player1.increaseScore(100);
//        player2.increaseScore(200);
//        player3.increaseScore(150);
//
//        // Add players to the leaderboard
//        leaderboard.addPlayer(player1);
//        leaderboard.addPlayer(player2);
//        leaderboard.addPlayer(player3);

        // Display the leaderboard
        leaderboard.displayLeaderboard();

        // Get and display the top player
        Player topPlayer = leaderboard.getTopPlayer();
        if (topPlayer != null) {
            System.out.println("\nTop player: " + topPlayer.getName() + " with score: " /*+ topPlayer.getScore()*/);
        }

    }

    public void markTile() {
    }

    public void showCredits() {
    }

    public void selectPlayer(String player) {


    }

    public String getElapsedTime() {
        if (startTime == 0) {
            return "Timer has not started.";
        }
        long elapsedTime = (endTime > 0 ? endTime : System.currentTimeMillis()) - startTime;
        return String.format("Final elapsed time: %.2f seconds", elapsedTime / 1000.0);
    }

}


//+selectPlayer(String)
//+startGame(int difficulty)
//+endGame()
//+saveGame()
//+loadGame()
//+loadPlayer()
//+loadLeaderBoard()
//+markTile()
//+showCredits()