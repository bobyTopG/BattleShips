package be.kdg.integration1.battleships_solitaire;

import java.util.Timer;

public class SimpleMenu {

    private Player player;
    private Board board;
    private Timer duration;

    public SimpleMenu() {

    }

    public String help() {
        return "This is the help ifo";
    }

    public void exit(){
        System.out.println("Goodbye!");
    }

    public void save() {
        System.out.println("Saving...");
    }


}
