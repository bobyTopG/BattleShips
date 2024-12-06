package be.kdg.integration1.battleships_solitaire;

import java.util.Timer;

public class SimpleMenu {

    public SimpleMenu() {

    }

    public String menuOptions() {
        return "a - Add Ship/ s - Ship To Water/ r - Remove Tile / t -  revile tiles / press c continue:  ";
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
