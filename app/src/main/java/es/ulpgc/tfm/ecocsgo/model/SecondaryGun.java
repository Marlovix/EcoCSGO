package es.ulpgc.tfm.ecocsgo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SecondaryGun extends Equipment implements Gun, Parcelable {
    private Integer casualty;
    private OriginEquipment origin;
    public SecondaryGun(String key){
        super("weapons", key);
        this.acceptedCategories = new EquipmentCategory[]{
                EquipmentCategory.PISTOL
        };
        casualty = 0;
        origin = OriginEquipment.PURCHASED;
        getData();
    }
    protected SecondaryGun(Parcel in) {
        super(in);
        casualty = in.readInt();
        origin = OriginEquipment.valueOf(in.readString());
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

    public static final Creator<SecondaryGun> CREATOR = new Creator<SecondaryGun>() {
        @Override
        public SecondaryGun createFromParcel(Parcel in) {
            return new SecondaryGun(in);
        }

        @Override
        public SecondaryGun[] newArray(int size) {
            return new SecondaryGun[size];
        }
    };

    public Integer getCasualty() {
        return casualty;
    }

    public OriginEquipment getOrigin() {
        return origin;
    }
}
