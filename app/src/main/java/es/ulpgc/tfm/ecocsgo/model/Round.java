package es.ulpgc.tfm.ecocsgo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Round implements Parcelable {

    private static final int NUMBER_OF_PLAYERS = 5;

    private Player[] players;
    private ResultRound result;

    // Constructor for first rounds and the change team round //
    public Round(EquipmentTeam equipmentTeam){
        players = new Player[NUMBER_OF_PLAYERS];
        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
            players[i] = new Player(equipmentTeam);
        }
    }

    // Constructor to take players' data from last round //
    public Round(EquipmentTeam equipmentTeam, Player[] players){

    }

    protected Round(Parcel in) {
    }

    public static final Creator<Round> CREATOR = new Creator<Round>() {
        @Override
        public Round createFromParcel(Parcel in) {
            return new Round(in);
        }

        @Override
        public Round[] newArray(int size) {
            return new Round[size];
        }
    };

    public Player[] getPlayers() {
        return players;
    }

    public ResultRound getResult() {
        return result;
    }

    public void setResult(ResultRound result) {
        this.result = result;
    }

    public void registerGun(Gun gun, int positionPlayer){
        if(gun instanceof MainGun)
            players[positionPlayer].registerMainGun((MainGun)gun);
        else if(gun instanceof SecondaryGun)
            players[positionPlayer].registerSecondaryGun((SecondaryGun)gun);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
