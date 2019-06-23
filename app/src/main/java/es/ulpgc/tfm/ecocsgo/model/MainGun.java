package es.ulpgc.tfm.ecocsgo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MainGun extends Gun implements Parcelable {

    public MainGun(){
        super();
        name = "ADSASDADSADASD";
    }

    public MainGun(String key){
        super(key);
        acceptedCategories = new EquipmentCategory[]{
                EquipmentCategory.HEAVY, EquipmentCategory.SMG, EquipmentCategory.RIFLE
        };
        getData();
    }

    protected MainGun(Parcel in) {
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