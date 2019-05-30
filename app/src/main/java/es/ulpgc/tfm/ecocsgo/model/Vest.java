package es.ulpgc.tfm.ecocsgo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Vest extends Equipment implements Parcelable {
    private OriginEquipment origin;

    public Vest(String key){
        super("utilities", key);
        this.acceptedCategories = new EquipmentCategory[]{
                EquipmentCategory.GEAR
        };
        getData();
    }

    public Vest (){}

    private Vest(Parcel in) {
        super(in);
        String nameOrigin = in.readString();
        if(nameOrigin != null){
            origin = OriginEquipment.valueOf(nameOrigin);
        }else{
            origin = null;
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        if(origin != null){
            dest.writeString(origin.name());
        }else{
            dest.writeString(null);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Vest> CREATOR = new Creator<Vest>() {
        @Override
        public Vest createFromParcel(Parcel in) {
            return new Vest(in);
        }

        @Override
        public Vest[] newArray(int size) {
            return new Vest[size];
        }
    };

    public OriginEquipment getOrigin() {
        return origin;
    }

    public void setOrigin(OriginEquipment origin) {
        this.origin = origin;
    }
}
