package be.kdg.integration1.battleships_solitaire.entities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Leaderboard {

    public static class LeaderboardRow {
        //private int rank;
        private final String playerName;
        private final Date gameDate;
        private final String duration;
        private final Difficulty difficulty;
        private final int score;

        public LeaderboardRow(String playerName, Date gameDate, String duration, Difficulty difficulty, int score) {
            this.playerName = playerName;
            this.gameDate = gameDate;
            this.duration = duration;
            this.difficulty = difficulty;
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

        public Difficulty getDifficulty() {
            return difficulty;
        }

//        public int getRank() {
//            return rank;
//        }
//
//        public void setRank(int rank) {
//            this.rank = rank;
//        }
    }

    private final List<LeaderboardRow> leaderboardRows;
    private int currentRank = 1;

    public void addLeaderboardRow(LeaderboardRow leaderBoardRow) {
        leaderboardRows.add(leaderBoardRow);
    }

    public List<LeaderboardRow> getLeaderboardRows() {
        return leaderboardRows;
    }

    public Leaderboard() {
        leaderboardRows = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("┌───────────────────────────────────────────────────────────┐\n");
        sb.append("│                        LEADERBOARD                        │\n");
        sb.append("├──────┬──────────────┬────────────┬────────────┬───────────┤\n");
        sb.append("│ Rank │    Player    │    Date    │  Duration  │   Score   │\n");
        sb.append("├──────┼──────────────┼────────────┼────────────┼───────────┤\n");

        // add each row of the leaderboard to output
        for (LeaderboardRow leaderboardRow : leaderboardRows) {
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String date = formatter.format(leaderboardRow.getGameDate());
            //leaderboardRow.setRank(currentRank);

            sb.append(String.format("│  %-3d │ %-12s │ %-10s │ %-10s │ %s %-7d │\n",
                    currentRank++,
                    leaderboardRow.getPlayerName(),
                    date,
                    leaderboardRow.getDuration(),
                    leaderboardRow.getDifficulty().getName().charAt(0),
                    leaderboardRow.getScore()));

            //currentRank++;
        }
        sb.append("└──────┴──────────────┴────────────┴────────────┴───────────┘");

        return sb.toString();
    }

}
