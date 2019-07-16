package es.ulpgc.tfm.ecocsgo.model;

import android.app.AlertDialog;
import android.os.Parcel;
import android.os.Parcelable;

public class MainGunJava extends EquipmentJava implements GunJava, Parcelable {
    private Integer casualty;
    private OriginEquipment origin;
    public MainGunJava(String key){
        super("weapons", key);
        acceptedCategories = new EquipmentCategory[]{
                EquipmentCategory.HEAVY, EquipmentCategory.SMG, EquipmentCategory.RIFLE
        };
        casualty = 0;
        origin = OriginEquipment.PURCHASED;
        getData();
    }

    public MainGunJava(String key, AlertDialog dialog){

    }

    protected MainGunJava(Parcel in) {
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

    public static final Creator<MainGunJava> CREATOR = new Creator<MainGunJava>() {
        @Override
        public MainGunJava createFromParcel(Parcel in) {
            return new MainGunJava(in);
        }

        @Override
        public MainGunJava[] newArray(int size) {
            return new MainGunJava[size];
        }
    };

    public Integer getCasualty() {
        return casualty;
    }

    public OriginEquipment getOrigin() {
        return origin;
    }
}
