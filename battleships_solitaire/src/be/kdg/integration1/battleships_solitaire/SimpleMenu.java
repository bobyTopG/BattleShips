package be.kdg.integration1.battleships_solitaire;

import java.util.Timer;

public class SimpleMenu {

    public SimpleMenu() {

    }

    public String menuOptions() {
        return "h - help / a / s / r / e / l : ";
    }

    public String help(){
        return "a - Add Ship / s - Ship To Water / r - Remove Tile\n" +
                "e -  End the game / l - Revile a tile \n\n";
    }

    public void tileToWater(Board board, int x, int y) {
        board.makeTileWater(x, y);
    }

    public void addShip(Board board, int x, int y) {
        board.makeTileAShip(x, y);
    }

    public void removeTile(Board board, int x, int y) {
        board.removeTile(x, y);
    }

    public String stopAndRevtileTiles(Board board) {
        return board.printAnswer();
    }

}
