package es.ulpgc.tfm.ecocsgo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SecondaryGunJava extends EquipmentJava implements GunJava, Parcelable {
    private Integer casualty;
    private OriginEquipment origin;
    public SecondaryGunJava(String key){
        super("weapons", key);
        this.acceptedCategories = new EquipmentCategory[]{
                EquipmentCategory.PISTOL
        };
        casualty = 0;
        origin = OriginEquipment.PURCHASED;
        getData();
    }
    protected SecondaryGunJava(Parcel in) {
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

    public static final Creator<SecondaryGunJava> CREATOR = new Creator<SecondaryGunJava>() {
        @Override
        public SecondaryGunJava createFromParcel(Parcel in) {
            return new SecondaryGunJava(in);
        }

        @Override
        public SecondaryGunJava[] newArray(int size) {
            return new SecondaryGunJava[size];
        }
    };

    public Integer getCasualty() {
        return casualty;
    }

    public OriginEquipment getOrigin() {
        return origin;
    }
}
