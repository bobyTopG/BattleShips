package be.kdg.integration1.battleships_solitaire.entities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Leaderboard {

    public class LeaderboardRow {
        private int rank;
        private String playerName;
        private Date gameDate;
        private String duration;
        private int score;

        public LeaderboardRow(String playerName, Date gameDate, String duration, int score) {
            this.playerName = playerName;
            this.gameDate = gameDate;
            this.duration = duration;
            this.score = score;
        }

        public String getPlayerName() {
            return playerName;
        }

        public Date getGameDate() {
            return gameDate;
        }

        public String getDuration() {
            return duration;
        }

        public int getScore() {
            return score;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }
    }

    private List<LeaderboardRow> leaderboardRows;
    private int currentRank = 1;

    public void addLeaderboardRow(LeaderboardRow leaderBoardRow) {
        leaderboardRows.add(leaderBoardRow);
    }

    public Leaderboard() {
        leaderboardRows = new ArrayList<>();
    }
//
//
//    // Add player to the leaderboard
//    public void addPlayer(Player player) {
//        players.add(player);
//    }
//
//    // Sort players based on their scores in descending order
//    public void sortPlayersByScore() {
//        Collections.sort(players, new Comparator<Player>() {
//            @Override
//            public int compare(Player p1, Player p2) {
//                return 0; //Integer.compare(p2.getScore(), p1.getScore());  // Higher score first
//            }
//        });
//    }
//
//    // Display the leaderboard
//    public void displayLeaderboard() {
//        sortPlayersByScore();  // Sort players before displaying
//        System.out.println("Leaderboard:");
//        for (int i = 0; i < players.size(); i++) {
//            Player player = players.get(i);
////            System.out.println((i + 1) + ". " + player.getName() + " - Score: " + player.getScore());
//        }
//    }
//
//    // Get the top player (highest score)
//    public Player getTopPlayer() {
//        if (players.isEmpty()) {
//            return null;
//        }
//        sortPlayersByScore();
//        return players.get(0);  // Return the player with the highest score
//    }
//
//    public ArrayList<Player> getPlayers() {
//        return players;
//    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("┌───────────────────────────────────────────────────────────┐\n");
        sb.append("│                        LEADERBOARD                        │\n");
        sb.append("├──────┬──────────────┬────────────┬────────────┬───────────┤\n");
        sb.append("│ Rank │    Player    │    Date    │  Duration  │   Score   │\n");
        sb.append("├──────┼──────────────┼────────────┼────────────┼───────────┤\n");

        // add each row of the leaderboard to output
        for (LeaderboardRow leaderBoardrow : leaderboardRows) {
            DateFormat formatter = new SimpleDateFormat("dd-MM-YYYY");
            String date = formatter.format(leaderBoardrow.getGameDate());
            leaderBoardrow.setRank(currentRank);

            sb.append(String.format("│  %-3d │ %-12s │ %-10s │ %-10s │ %-9d │\n",
                    leaderBoardrow.getRank(),
                    leaderBoardrow.getPlayerName(),
                    date,
                    leaderBoardrow.getDuration(),
                    leaderBoardrow.getScore()));

            currentRank++;
        }
        sb.append("└──────┴──────────────┴────────────┴────────────┴───────────┘\n");

        return sb.toString();
    }

}
