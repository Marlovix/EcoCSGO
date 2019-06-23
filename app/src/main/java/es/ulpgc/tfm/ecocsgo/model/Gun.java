package es.ulpgc.tfm.ecocsgo.model;

import android.os.Parcel;
import android.os.Parcelable;

abstract public class Gun extends Equipment implements Parcelable {
    Integer reward;
    Integer casualty;
    OriginEquipment origin;

    public Gun(String key) {
        super("weapons", key);
    }

    public Gun(Parcel in) {
        super(in);
        reward = in.readInt();
        casualty = in.readInt();
        origin = OriginEquipment.valueOf(in.readString());
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
        dest.writeInt(reward);
        dest.writeInt(casualty);
        dest.writeString(origin.name());
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
