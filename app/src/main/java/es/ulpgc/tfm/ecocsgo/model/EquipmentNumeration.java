package es.ulpgc.tfm.ecocsgo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class EquipmentNumeration implements Parcelable {
    private int item;
    private EquipmentCategory category;

    public EquipmentNumeration(){

    }

    public EquipmentNumeration(int item, EquipmentCategory category) {
        this.item = item;
        this.category = category;
    }

    protected EquipmentNumeration(Parcel in) {
        item = in.readInt();
    }

    public static final Creator<EquipmentNumeration> CREATOR = new Creator<EquipmentNumeration>() {
        @Override
        public EquipmentNumeration createFromParcel(Parcel in) {
            return new EquipmentNumeration(in);
        }

        @Override
        public EquipmentNumeration[] newArray(int size) {
            return new EquipmentNumeration[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(item);
    }
}
