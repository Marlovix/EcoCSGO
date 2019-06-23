package es.ulpgc.tfm.ecocsgo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SecondaryGun extends Gun implements Parcelable {
    public SecondaryGun(String key){
        super(key);
        this.acceptedCategories = new EquipmentCategory[]{
                EquipmentCategory.PISTOL
        };
        getData();
    }
    protected SecondaryGun(Parcel in) {
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
}
