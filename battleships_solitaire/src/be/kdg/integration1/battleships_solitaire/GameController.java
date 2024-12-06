package be.kdg.integration1.battleships_solitaire;

import java.util.Scanner;

public class GameController {

    private LeaderBoard leaderBoard;
    private Board board;
    private Player player;
    private PersistenceController persistenceController;
    boolean gameEnded = false;

    private long startTime;
    private long endTime;

    //Constructor
    public GameController() {
    }


    public void selectPlayer(String player) {
    }

    public void startGame(int difficulty) {
        Scanner scanner = new Scanner(System.in);
        Board board = new Board(difficulty);
        SimpleMenu menu = new SimpleMenu();

        // Placing ships on the board
        for (int i = 0; i < 1; i++) {
            board.generateShips();
        }
        ;
        board.generateTiles();
        startTime = System.currentTimeMillis();
        System.out.println(board.toString());

        while (!board.isGameOver()) {
            System.out.print(menu.menuOptions());
            char line = ' ';

            while (true) {
                line = scanner.nextLine().charAt(0);

                if (line == 't') {
                    System.out.print(" X: ");
                    int x = scanner.nextInt() - 1;
                    System.out.print(" Y: ");
                    int y = scanner.nextInt() - 1;
                    menu.shipToWater(board , x , y);

                } else if (line == 'r') {
                    System.out.print(" X: ");
                    int x = scanner.nextInt() - 1;
                    System.out.print(" Y: ");
                    int y = scanner.nextInt() - 1;
                    menu.removeTile(board , x , y);

                }
                else if(line == 'a') {
                    System.out.print(" X: ");
                    int x = scanner.nextInt() - 1;
                    System.out.print(" Y: ");
                    int y = scanner.nextInt() - 1;
                    menu.addShip(board , x , y);
                }
                else if (line == 's') {
                    System.out.print(menu.stopAndRevtileTiles(board));
                    gameEnded = true;
                    break;
                }
                else{
                    break;
                }
            }
            if (gameEnded) break;

            System.out.print(" X: ");
            int x = scanner.nextInt() - 1;
            System.out.print(" Y: ");
            int y = scanner.nextInt() - 1;


            System.out.println(board.toString());
            System.out.println(getElapsedTime());
        }
        endTime = System.currentTimeMillis();
        if(gameEnded) {
            System.out.println("You lost :(");
        }else {
            System.out.println("You win :)");
        }
        System.out.println(getElapsedTime());
    }

    public String getElapsedTime() {
        if (startTime == 0) {
            return "Timer has not started.";
        }
        long elapsedTime = (endTime > 0 ? endTime : System.currentTimeMillis()) - startTime;
        return String.format("Elapsed time: %.2f seconds", elapsedTime / 1000.0);
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