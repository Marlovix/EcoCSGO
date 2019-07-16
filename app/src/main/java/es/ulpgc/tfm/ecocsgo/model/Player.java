package es.ulpgc.tfm.ecocsgo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Player implements Parcelable {
    private ArrayList<MainGunJava> mainGuns;
    private ArrayList<SecondaryGunJava> secondaryGuns;
    private Vest vest;
    private Helmet helmet;
    private DefuseKit defuseKit;
    private boolean alive;

    public Player(EquipmentTeam equipmentTeam){
        mainGuns = new ArrayList<>();
        secondaryGuns = new ArrayList<>();
        alive = true;
    }

    protected Player(Parcel in) {
        mainGuns = in.createTypedArrayList(MainGunJava.CREATOR);
        secondaryGuns = in.createTypedArrayList(SecondaryGunJava.CREATOR);
        vest = in.readParcelable(Vest.class.getClassLoader());
        helmet = in.readParcelable(Helmet.class.getClassLoader());
        defuseKit = in.readParcelable(DefuseKit.class.getClassLoader());
        alive = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mainGuns);
        dest.writeTypedList(secondaryGuns);
        dest.writeParcelable(vest, flags);
        dest.writeParcelable(helmet, flags);
        dest.writeParcelable(defuseKit, flags);
        dest.writeByte((byte) (alive ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    public ArrayList<MainGunJava> getMainGuns() {
        return mainGuns;
    }

    public ArrayList<SecondaryGunJava> getSecondaryGuns() {
        return secondaryGuns;
    }

    public boolean isAlive() {
        return alive;
    }

    public MainGunJava getLastMainGun(){
        return (mainGuns != null && !mainGuns.isEmpty()) ? mainGuns.get(mainGuns.size() - 1) : null;
    }

    public SecondaryGunJava getLastSecondaryGun(){
        return (secondaryGuns != null && !secondaryGuns.isEmpty()) ? secondaryGuns.get(secondaryGuns.size() - 1) : null;
    }

    public void registerMainGun(MainGunJava gun){
        mainGuns.add(gun);
    }
    public void registerSecondaryGun(SecondaryGunJava gun){
        secondaryGuns.add(gun);
    }

}
