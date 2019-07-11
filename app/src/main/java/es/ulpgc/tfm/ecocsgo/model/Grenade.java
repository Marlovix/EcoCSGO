package es.ulpgc.tfm.ecocsgo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Grenade extends EquipmentJava implements Parcelable {
    public Grenade(String key){
        super("grenades", key);
        this.acceptedCategories = new EquipmentCategory[]{
                EquipmentCategory.GRENADE
        };
        getData();
    }

    protected Grenade(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Grenade> CREATOR = new Creator<Grenade>() {
        @Override
        public Grenade createFromParcel(Parcel in) {
            return new Grenade(in);
        }

        @Override
        public Grenade[] newArray(int size) {
            return new Grenade[size];
        }
    };
}
