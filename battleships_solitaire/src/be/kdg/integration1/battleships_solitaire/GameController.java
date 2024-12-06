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

        outer:
        while (!board.isGameOver()) {
            while (true) {
                System.out.print(menu.menuOptions());
                char line = ' ';
                line = scanner.nextLine().charAt(0);

                if (line == 't') {
                    System.out.print(" X: ");
                    int x = Integer.parseInt(scanner.nextLine())-1;
                    System.out.print(" Y: ");
                    int y = Integer.parseInt(scanner.nextLine())-1;
                    menu.shipToWater(board , x , y);
                    System.out.println(board.toString());
                    System.out.println(getElapsedTime());

                } else if (line == 'r') {
                    System.out.print(" X: ");
                    int x = Integer.parseInt(scanner.nextLine())-1;
                    System.out.print(" Y: ");
                    int y = Integer.parseInt(scanner.nextLine())-1;
                    menu.removeTile(board , x , y);
                    System.out.println(board.toString());
                    System.out.println(getElapsedTime());

                }
                else if(line == 'a') {
                    System.out.print(" X: ");
                    int x = Integer.parseInt(scanner.nextLine())-1;
                    System.out.print(" Y: ");
                    int y = Integer.parseInt(scanner.nextLine())-1;
                    menu.addShip(board, x, y);
                    System.out.println(board);
                    System.out.println(getElapsedTime());
                }
                else if (line == 's') {
                    System.out.print(menu.stopAndRevtileTiles(board));
                    gameEnded = true;
                    break;
                }
                else{
                    break;
                }
                if(board.isGameOver()) {break outer;}
            }
            if (gameEnded) break;



        }
        endTime = System.currentTimeMillis();
        if(gameEnded) {
            System.out.println("You lost :(");
        }else {
            System.out.print("You win :) \nFinal ");
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