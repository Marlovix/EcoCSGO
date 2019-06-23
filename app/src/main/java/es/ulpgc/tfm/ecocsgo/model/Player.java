package es.ulpgc.tfm.ecocsgo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Player implements Parcelable {
    private List<MainGun> mainGuns;
    private List<SecondaryGun> secondaryGuns;
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
        mainGuns = in.createTypedArrayList(MainGun.CREATOR);
        secondaryGuns = in.createTypedArrayList(SecondaryGun.CREATOR);
        vest = in.readParcelable(Vest.class.getClassLoader());
        helmet = in.readParcelable(Helmet.class.getClassLoader());
        defuseKit = in.readParcelable(DefuseKit.class.getClassLoader());
        alive = in.readByte() != 0;
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

    public List<MainGun> getMainGuns() {
        return mainGuns;
    }

    public List<SecondaryGun> getSecondaryGuns() {
        return secondaryGuns;
    }

    public boolean isAlive() {
        return alive;
    }

    public void registerMainGun(MainGun gun){
        mainGuns.add(gun);
    }
    public void registerSecondaryGun(SecondaryGun gun){
        secondaryGuns.add(gun);
    }

    @Override
    public int describeContents() {
        return 0;
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
}
