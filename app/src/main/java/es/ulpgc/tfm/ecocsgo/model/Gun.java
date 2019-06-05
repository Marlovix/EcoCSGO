package es.ulpgc.tfm.ecocsgo.model;

import android.os.Parcel;
import android.os.Parcelable;

abstract class Gun extends Equipment implements Parcelable {
    Integer reward;
    Integer casualty;
    OriginEquipment origin;

    public Gun(String key) {
        super("weapons", key);
    }

    public Gun(Parcel parcel) {
        super(parcel);
    }

    public Gun(){

    }

    public Integer getReward() {
        return reward;
    }

    public Integer getCasualty() {
        return casualty;
    }

    public OriginEquipment getOrigin() {
        return origin;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MainGun> CREATOR = new Creator<MainGun>() {
        @Override
        public MainGun createFromParcel(Parcel in) {
            return new MainGun(in);
        }

        @Override
        public MainGun[] newArray(int size) {
            return new MainGun[size];
        }
    };

}
