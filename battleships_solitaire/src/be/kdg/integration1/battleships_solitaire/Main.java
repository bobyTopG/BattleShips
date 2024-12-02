package be.kdg.integration1.battleships_solitaire;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        Board board = new Board(10 , 5 , 1);

        System.out.println(board.toString());

    }
}