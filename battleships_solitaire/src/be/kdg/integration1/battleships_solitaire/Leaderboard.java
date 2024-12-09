package be.kdg.integration1.battleships_solitaire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Leaderboard {

    private ArrayList<Player> players;

    public Leaderboard() {
        players = new ArrayList<>();
    }

    // Add player to the leaderboard
    public void addPlayer(Player player) {
        players.add(player);
    }

    // Sort players based on their scores in descending order
    public void sortPlayersByScore() {
        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return 0; //Integer.compare(p2.getScore(), p1.getScore());  // Higher score first
            }
        });
    }

    // Display the leaderboard
    public void displayLeaderboard() {
        sortPlayersByScore();  // Sort players before displaying
        System.out.println("Leaderboard:");
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
//            System.out.println((i + 1) + ". " + player.getName() + " - Score: " + player.getScore());
        }
    }

    // Get the top player (highest score)
    public Player getTopPlayer() {
        if (players.isEmpty()) {
            return null;
        }
        sortPlayersByScore();
        return players.get(0);  // Return the player with the highest score
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Header for the leaderboard
        sb.append("        Leaderboard:\n");
        sb.append("-----------------------------\n");

        // Add each player with formatted output
        for (Player player : players) {
            sb.append(String.format("%-20s | Score: %-5d\n", player.getName(), 0/*player.getScore()*/));
        }

        // Add a footer line for cleanliness
        sb.append("-----------------------------\n");

        return sb.toString();
    }

}
