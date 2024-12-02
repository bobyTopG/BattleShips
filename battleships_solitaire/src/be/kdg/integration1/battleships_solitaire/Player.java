package be.kdg.integration1.battleships_solitaire;

public class Player {

    private String name;
    private int id;


    public Player(String name, int id) {
        this.name = name;
        this.id = id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public Tile play(){
        return null;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}


//+play(): Tile
//+toString(): String