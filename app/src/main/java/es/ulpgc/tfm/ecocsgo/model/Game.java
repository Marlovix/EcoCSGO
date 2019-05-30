package es.ulpgc.tfm.ecocsgo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Game implements Parcelable {
    private static Game game;

    private EquipmentTeam enemyTeam;
    private int roundInGame;
    private Round[] rounds;
    private int enemyEconomy;

    private Game(EquipmentTeam team) {
        this.enemyTeam = team;
        this.roundInGame = 1;
        this.rounds = new Round[30];
        this.rounds[1] = new Round(team);
    }

    protected Game(Parcel in) {
        roundInGame = in.readInt();
        enemyEconomy = in.readInt();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    public static Game getSingletonInstance(EquipmentTeam team) {
        if (game == null)
            game = new Game(team);
        else
            System.out.println("Singleton pattern. Can not be created another instance of this class.");
        return game;
    }

    public void nextRound(){
        calculateEnemyEconomy();

        roundInGame++;

        if(isChangeTeamRound()){
            rounds[roundInGame] = new Round(enemyTeam);
        }else{
            Round lastRound = rounds[roundInGame-1];
            Player[] players = lastRound.getPlayers();
            rounds[roundInGame] = new Round(enemyTeam, players);
        }
    }

    private boolean isChangeTeamRound(){
        return roundInGame == rounds.length/2 + 1;
    }

    private void calculateEnemyEconomy() {
        ResultRound result = rounds[roundInGame].getResult();
        int rewardResultRound = result.getReward();
        enemyEconomy = 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(roundInGame);
        dest.writeInt(enemyEconomy);
    }
}
