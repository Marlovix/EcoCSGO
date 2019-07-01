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
    private ArrayList<MainGun> heavyWeapons;
    private ArrayList<MainGun> smgWeapons;
    private ArrayList<MainGun> rifleWeapons;
    private ArrayList<Grenade> grenades;

    private DefuseKit kit;
    private Helmet helmet;
    private Vest vest;
    private Taser taser;

    private EconomyGame economy;

    private Game(EquipmentTeam team) {
        this.enemyTeam = team;
        this.rounds = new Round[30];
        pistolWeapons = new ArrayList<>();
        heavyWeapons = new ArrayList<>();
        smgWeapons = new ArrayList<>();
        rifleWeapons = new ArrayList<>();
        grenades = new ArrayList<>();
    }

    protected Game(Parcel in) {
        enemyTeam = EquipmentTeam.valueOf(in.readString());
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
        economy = in.readParcelable(EconomyGame.class.getClassLoader());
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

    public void firstRound(){

    }

    public void startRound(int nRound) {
        roundInGame = nRound + 1;
        rounds[nRound] = new Round(enemyTeam);

        for (int i = 0; i < rounds[nRound].getPlayers().size(); i++) {
            Player player = rounds[nRound].getPlayers().get(i);

            // If the round is not the first one or the change team round //
            if (roundInGame != 1 && roundInGame != rounds.length / 2 + 1) {

                if (rounds[nRound - 1].getPlayers().get(i).isAlive()) {
                    rounds[nRound].getPlayers().set(i, player);
                }
                // Else -> Copiar dinero de la ronda anterior
            }

            if (player.getSecondaryGuns().isEmpty()) {
                EquipmentNumeration numeration = new EquipmentNumeration(1, EquipmentCategory.PISTOL);
                player.registerSecondaryGun((SecondaryGun) findGun(numeration, enemyTeam));
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
            ArrayList<Player> players = lastRound.getPlayers();
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

    public Equipment findGun(EquipmentNumeration numeration, EquipmentTeam team) {

        ArrayList listGuns = new ArrayList<Gun>();
        switch(numeration.getCategory()){
            case PISTOL:
                listGuns = game.getPistolWeapons();
                break;
            case HEAVY:
                listGuns = game.getHeavyWeapons();
                break;
            case SMG:
                listGuns = game.getSmgWeapons();
                break;
            case RIFLE:
                listGuns = game.getRifleWeapons();
                break;
        }

        if(listGuns != null){
            for (int i=0; i<listGuns.size(); i++){
                Equipment gun = (Equipment) listGuns.get(i);
                if(gun.getNumeration().getItem() == numeration.getItem() && gun.getTeam().equals(team)){
                    return gun;
                }
            }
        }

        return null;
    }

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

    public void setKit(DefuseKit kit) {
        this.kit = kit;
    }

    public Taser getTaser() {
        return taser;
    }

    public void setTaser(Taser taser) {
        this.taser = taser;
    }

    public void setHelmet(Helmet helmet) {
        this.helmet = helmet;
    }

    public void setVest(Vest vest) {
        this.vest = vest;
    }

    public void setEconomy(EconomyGame economy) {
        this.economy = economy;
    }

    public EquipmentTeam getEnemyTeam() {
        return enemyTeam;
    }

    public ArrayList<SecondaryGun> getPistolWeapons() {
        return pistolWeapons;
    }

    public ArrayList<MainGun> getSmgWeapons() {
        return smgWeapons;
    }

    public ArrayList<MainGun> getRifleWeapons() {
        return rifleWeapons;
    }

    public ArrayList<MainGun> getHeavyWeapons() {
        return heavyWeapons;
    }

    public ArrayList<Grenade> getGrenades() {
        return grenades;
    }

    public DefuseKit getKit() {
        return kit;
    }

    public Helmet getHelmet() {
        return helmet;
    }

    public Vest getVest() {
        return vest;
    }

    public EconomyGame getEconomy() {
        return economy;
    }

    public int getRoundInGame() {
        return roundInGame;
    }

    public Round[] getRounds() {
        return rounds;
    }

    public int getEnemyEconomy() {
        return enemyEconomy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(enemyTeam.name());
        parcel.writeInt(roundInGame);
        parcel.writeTypedArray(rounds, i);
        parcel.writeInt(enemyEconomy);
        parcel.writeTypedList(pistolWeapons);
        parcel.writeTypedList(smgWeapons);
        parcel.writeTypedList(rifleWeapons);
        parcel.writeTypedList(heavyWeapons);
        parcel.writeTypedList(grenades);
        parcel.writeParcelable(kit, i);
        parcel.writeParcelable(helmet, i);
        parcel.writeParcelable(vest, i);
        parcel.writeParcelable(economy, i);
    }
}
