package be.kdg.integration1.battleships_solitaire;

public enum ShipType {

    BATTLESHIP(4),

    CRUISER(3),

    DESTROYER(2),

    SUBMARINE(1);

    private int shipSize;

     ShipType(int shipSize){
        this.shipSize = shipSize;
    }

    public int getShipSize() {
         return shipSize;
    }

}

//BATTLESHIP(4)
//CRUISER(3)
//DESTROYER(2)
//SUBMARINE(1)