package be.kdg.integration1.battleships_solitaire;

public class SimpleMenu {

    public SimpleMenu() {

    }

    public String menuOptions() {
        return "    h - help / a / s / r / e / l : ";
    }

    public String help(){
        return "a - Add Ship / s - Add Water / r - Remove\n" +
                "e -  End the game / l - Revile a tile \n\n";
    }

    public void tileToWater(Board board, int x, int y) {
        board.markTileAsWater(x, y);
    }

    public void addShip(Board board, int x, int y) {
        board.markTileAsShip(x, y);
    }

    public void removeTile(Board board, int x, int y) {
        board.unmarkTile(x, y);
    }

    public String stopAndRevtileTiles(Board board) {
        return board.printAnswer();
    }

}
