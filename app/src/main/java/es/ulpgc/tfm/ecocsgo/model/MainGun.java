package es.ulpgc.tfm.ecocsgo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MainGun extends Equipment implements Gun, Parcelable {
    private Integer casualty;
    private OriginEquipment origin;
    public MainGun(String key){
        super("weapons", key);
        acceptedCategories = new EquipmentCategory[]{
                EquipmentCategory.HEAVY, EquipmentCategory.SMG, EquipmentCategory.RIFLE
        };
        casualty = 0;
        origin = OriginEquipment.PURCHASED;
        getData();
    }
    protected MainGun(Parcel in) {
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

    public Integer getCasualty() {
        return casualty;
    }

    public OriginEquipment getOrigin() {
        return origin;
    }
}
