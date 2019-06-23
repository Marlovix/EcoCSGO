package es.ulpgc.tfm.ecocsgo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Game implements Parcelable {
    private static Game game;

    private EquipmentTeam enemyTeam;
    private int roundInGame;
    private Round[] rounds;
    private int enemyEconomy;

    private ArrayList<SecondaryGun> pistolWeapons;
    private ArrayList<MainGun> smgWeapons;
    private ArrayList<MainGun> rifleWeapons;
    private ArrayList<MainGun> heavyWeapons;
    private ArrayList<Grenade> grenades;

    private DefuseKit kit;
    private Helmet helmet;
    private Vest vest;

    private Game(EquipmentTeam team) {
        this.enemyTeam = team;
        this.rounds = new Round[30];
    }

    public static Game getSingletonInstance(EquipmentTeam team) {
        if (game == null)
            game = new Game(team);
        else
            System.out.println("Singleton pattern. Can not be created another instance of this class.");
        return game;
    }

    protected Game(Parcel in) {
        roundInGame = in.readInt();
        rounds = in.createTypedArray(Round.CREATOR);
        enemyEconomy = in.readInt();
        pistolWeapons = in.createTypedArrayList(SecondaryGun.CREATOR);
        smgWeapons = in.createTypedArrayList(MainGun.CREATOR);
        rifleWeapons = in.createTypedArrayList(MainGun.CREATOR);
        heavyWeapons = in.createTypedArrayList(MainGun.CREATOR);
        grenades = in.createTypedArrayList(Grenade.CREATOR);
        kit = in.readParcelable(DefuseKit.class.getClassLoader());
        helmet = in.readParcelable(Helmet.class.getClassLoader());
        vest = in.readParcelable(Vest.class.getClassLoader());
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

    public void setPistolWeapons(ArrayList<SecondaryGun> pistolWeapons) {
        this.pistolWeapons = pistolWeapons;
    }

    public void setSmgWeapons(ArrayList<MainGun> smgWeapons) {
        this.smgWeapons = smgWeapons;
    }

    public void setRifleWeapons(ArrayList<MainGun> rifleWeapons) {
        this.rifleWeapons = rifleWeapons;
    }

    public void setHeavyWeapons(ArrayList<MainGun> heavyWeapons) {
        this.heavyWeapons = heavyWeapons;
    }

    public void setGrenades(ArrayList<Grenade> grenades) {
        this.grenades = grenades;
    }

    public void startRound(int nRound) {
        roundInGame = nRound;
        rounds[nRound] = new Round(enemyTeam);

        int indexPlayer = 0;
        for (Player player : rounds[nRound].getPlayers()) {
            if (roundInGame != 1 && roundInGame != rounds.length / 2 + 1) {
                if (rounds[nRound - 1].getPlayers()[indexPlayer].isAlive()) {
                    rounds[nRound].getPlayers()[indexPlayer] = player;
                }
                // Else -> Copiar dinero de la ronda anterior
            }

            if (player.getSecondaryGuns().isEmpty()) {
                EquipmentNumeration numeration = new EquipmentNumeration(1, EquipmentCategory.PISTOL);
                player.registerSecondaryGun(pistolWeapons.get(0));
                indexPlayer++;
            }
        }
    }

    public void nextRound() {
        calculateEnemyEconomy();

        roundInGame++;

        if (isChangeTeamRound()) {
            rounds[roundInGame] = new Round(enemyTeam);
        } else {
            Round lastRound = rounds[roundInGame - 1];
            Player[] players = lastRound.getPlayers();
            rounds[roundInGame] = new Round(enemyTeam, players);
        }
    }

    private boolean isChangeTeamRound() {
        return roundInGame == rounds.length / 2 + 1;
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
        dest.writeTypedArray(rounds, flags);
        dest.writeInt(enemyEconomy);
        dest.writeTypedList(pistolWeapons);
        dest.writeTypedList(smgWeapons);
        dest.writeTypedList(rifleWeapons);
        dest.writeTypedList(heavyWeapons);
        dest.writeTypedList(grenades);
        dest.writeParcelable(kit, flags);
        dest.writeParcelable(helmet, flags);
        dest.writeParcelable(vest, flags);
    }
}
