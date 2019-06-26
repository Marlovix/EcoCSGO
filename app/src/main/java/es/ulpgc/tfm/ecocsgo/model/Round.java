package es.ulpgc.tfm.ecocsgo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Round implements Parcelable {

    private static final int NUMBER_OF_PLAYERS = 5;

    private List<Player> players;
    private ResultRound result;

    // Constructor for first rounds and the change team round //
    public Round(EquipmentTeam equipmentTeam){
        players = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_PLAYERS; i++) {
            players.add(new Player(equipmentTeam));
        }
    }

    // Constructor to take players' data from last round //
    public Round(EquipmentTeam equipmentTeam, List<Player> players){

    }

    protected Round(Parcel in) {
        players = in.createTypedArrayList(Player.CREATOR);
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

    public List<Player> getPlayers() {
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
            players.get(positionPlayer).registerMainGun((MainGun)gun);
        else if(gun instanceof SecondaryGun)
            players.get(positionPlayer).registerSecondaryGun((SecondaryGun)gun);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(players);
    }
}
