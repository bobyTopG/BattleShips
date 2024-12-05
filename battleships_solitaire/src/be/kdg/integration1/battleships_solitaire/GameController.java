package be.kdg.integration1.battleships_solitaire;

import java.util.Scanner;

public class GameController {

    //Constructor
    public GameController() {}


    public void selectPlayer(String player){}

    public void startGame(int difficulty)
    {
        Scanner scanner = new Scanner(System.in);
        Board board = new Board(difficulty);

        // Placing ships on the board
        for (int i = 0; i < 1; i++) {
            board.generateShips();
        };
        board.generateTiles();
        System.out.println(board.toString());

        while (!board.isGameOver()){
            System.out.print(" X: ");
            int x = scanner.nextInt() - 1;
            System.out.print(" Y: ");
            int y = scanner.nextInt() - 1;
            if(y == 100){
                System.out.println(board.printAnswer());
                break;
            }
            board.revealTile(x, y);
            System.out.println(board.toString());
        }

        System.out.println("you win");
    }

    public void endGame(){}

    public void saveGame(){}

    public void loadGame(){}

    public void loadPlayer(Player player){}

    public void loadLeaderBoard(){}

    public void markTile(){}

    public void showCredits(){}

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