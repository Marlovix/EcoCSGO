package es.ulpgc.tfm.ecocsgo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Helmet extends EquipmentJava implements Parcelable {
    private OriginEquipment origin;

    public Helmet(String key){
        super("utilities", key);
        this.acceptedCategories = new EquipmentCategory[]{
                EquipmentCategory.GEAR
        };
        getData();
    }

    public Helmet (){}

    private Helmet(Parcel in) {
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

    public static final Creator<Helmet> CREATOR = new Creator<Helmet>() {
        @Override
        public Helmet createFromParcel(Parcel in) {
            return new Helmet(in);
        }

        @Override
        public Helmet[] newArray(int size) {
            return new Helmet[size];
        }
    };

    public OriginEquipment getOrigin() {
        return origin;
    }

    public void setOrigin(OriginEquipment origin) {
        this.origin = origin;
    }
}
