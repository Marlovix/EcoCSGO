package es.ulpgc.tfm.ecocsgo.model;

import android.os.Parcel;
import android.os.Parcelable;

abstract public class Gun extends Equipment implements Parcelable {
    Integer casualty;
    OriginEquipment origin;

    public Gun(String key) {
        super("weapons", key);
        casualty = 0;
        origin = OriginEquipment.PURCHASED;
    }

    public Gun(Parcel in) {
        super(in);
        casualty = in.readInt();
        origin = OriginEquipment.valueOf(in.readString());
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
        dest.writeInt(casualty);
        dest.writeString(origin.name());
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
