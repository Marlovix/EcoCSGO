package es.ulpgc.tfm.ecocsgo.model;

public class Game {
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
}
