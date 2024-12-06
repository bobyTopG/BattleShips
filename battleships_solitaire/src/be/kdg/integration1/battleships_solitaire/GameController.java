package be.kdg.integration1.battleships_solitaire;

import java.util.Scanner;

public class GameController {

    private Board board;
    private PersistenceController persistenceController;
    boolean gameEnded = false;

    private long startTime;
    private long endTime;

    //Constructor
    public GameController() {
    }

    public void startGame(int difficulty) {
        Scanner scanner = new Scanner(System.in);
        Board board = new Board(difficulty);
        SimpleMenu menu = new SimpleMenu();

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
                System.out.println(getElapsedTime());
            } else if (command == 't') {
                System.out.print(menu.stopAndRevtileTiles(board));
                gameEnded = true;
            }
            else if (command == 'l') {
                board.revealTile();
                System.out.println(board);
                System.out.println(getElapsedTime());
            }
        }

        endTime = System.currentTimeMillis();

        System.out.println(gameEnded ? "You lost :(" : "You win :) \nFinal " + getElapsedTime());
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
        return String.format("Elapsed time: %.2f seconds", elapsedTime / 1000.0);
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