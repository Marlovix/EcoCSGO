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
}
