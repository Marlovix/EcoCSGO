package es.ulpgc.tfm.ecocsgo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DefuseKit extends EquipmentJava implements Parcelable {
    private OriginEquipment origin;

    public DefuseKit(String key){
        super("utilities", key);
        this.acceptedCategories = new EquipmentCategory[]{
                EquipmentCategory.GEAR
        };
        getData();
    }

    public DefuseKit (){}

    private DefuseKit(Parcel in) {
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

    public static final Creator<DefuseKit> CREATOR = new Creator<DefuseKit>() {
        @Override
        public DefuseKit createFromParcel(Parcel in) {
            return new DefuseKit(in);
        }

        @Override
        public DefuseKit[] newArray(int size) {
            return new DefuseKit[size];
        }
    };

    public OriginEquipment getOrigin() {
        return origin;
    }

    public void setOrigin(OriginEquipment origin) {
        this.origin = origin;
    }

}
