package be.kdg.integration1.battleships_solitaire.scripts;

public class LeaderBoard {

    private Player[] players;

    public LeaderBoard(Player[] players) {
        this.players = players;
    }

    public Player[] getPlayers() {
        return players;
    }

    public Player[] getFirst(int amount) {
        Player[] result = new Player[amount];
        for (int i = 0; i < amount; i++) {
            result[i] = players[i];
        }
        return result;
    }
}


//-players: Player[]

//+«create»(Player[] players)
//+getAll(): Player[]
//+getFirst(int amount): Player[]