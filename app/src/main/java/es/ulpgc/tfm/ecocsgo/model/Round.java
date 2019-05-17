package es.ulpgc.tfm.ecocsgo.model;

public class Round {
    private Player[] players;
    private ResultRound result;

    // Constructor for first rounds and the change team round //
    public Round(EquipmentTeam equipmentTeam){

    }

    // Constructor to take players' data from last round //
    public Round(EquipmentTeam equipmentTeam, Player[] players){

    }

    public Player[] getPlayers() {
        return players;
    }

    public ResultRound getResult() {
        return result;
    }

    public void setResult(ResultRound result) {
        this.result = result;
    }
}
