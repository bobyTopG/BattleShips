package be.kdg.integration1.battleships_solitaire;

import java.util.Timer;

public class SimpleMenu {

    public SimpleMenu() {

    }

    public String menuOptions() {
        return "a - Add Ship/t - Ship To Water/ r - Remove Tile / s - Stop and revile tiles / press c continue:  ";
    }

    public void shipToWater(Board board, int x, int y) {
        board.makeTileAShip(x, y);
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
