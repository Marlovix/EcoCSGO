package es.ulpgc.tfm.ecocsgo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Taser extends Equipment implements Parcelable {
    public Taser(String key){
        super("utilities", key);
        this.acceptedCategories = new EquipmentCategory[]{
                EquipmentCategory.GEAR
        };
        getData();
    }

    protected Taser(Parcel in) {
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

    public static final Creator<Taser> CREATOR = new Creator<Taser>() {
        @Override
        public Taser createFromParcel(Parcel in) {
            return new Taser(in);
        }

        @Override
        public Taser[] newArray(int size) {
            return new Taser[size];
        }
    };
}
