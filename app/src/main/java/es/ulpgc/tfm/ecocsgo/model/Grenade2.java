package es.ulpgc.tfm.ecocsgo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Grenade2 extends EquipmentJava implements Parcelable {
    public Grenade2(String key){
        super("grenades", key);
        this.acceptedCategories = new EquipmentCategory[]{
                EquipmentCategory.GRENADE
        };
        getData();
    }

    protected Grenade2(Parcel in) {
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

    public static final Creator<Grenade2> CREATOR = new Creator<Grenade2>() {
        @Override
        public Grenade2 createFromParcel(Parcel in) {
            return new Grenade2(in);
        }

        @Override
        public Grenade2[] newArray(int size) {
            return new Grenade2[size];
        }
    };
}
