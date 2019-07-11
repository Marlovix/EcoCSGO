package es.ulpgc.tfm.ecocsgo.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;

public abstract class EquipmentJava implements Parcelable {
    protected DatabaseReference reference;
    protected String referenceKey;
    protected String name;
    protected EquipmentNumeration numeration;
    protected Integer cost;
    protected EquipmentTeam team;
    protected Integer reward;
    protected EquipmentCategory[] acceptedCategories;

    EquipmentJava(){ }

    EquipmentJava(String referenceKey, String key){
        this.referenceKey = referenceKey;
        this.reference = FirebaseDatabase.getInstance().getReference(referenceKey).child(key);
    }

    EquipmentJava(Parcel in) {
        referenceKey = in.readString();
        name = in.readString();
        numeration = in.readParcelable(EquipmentNumeration.class.getClassLoader());
        if (in.readByte() == 0) {
            cost = null;
        } else {
            cost = in.readInt();
        }
        team = EquipmentTeam.valueOf(in.readString());
        reward = in.readInt();
        Object[] list = in.readArray(EquipmentCategory[].class.getClassLoader());
        if (list != null) {
            EquipmentCategory[] acceptedCategories = new EquipmentCategory[list.length];
            for(int i=0; i<list.length; i++){
                acceptedCategories[i] = (EquipmentCategory)list[i];
            }
            this.acceptedCategories = acceptedCategories;
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(referenceKey);
        dest.writeString(name);
        dest.writeParcelable(numeration, flags);
        if (cost == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(cost);
        }
        dest.writeString(team.name());
        dest.writeInt(reward);
        dest.writeArray(acceptedCategories);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getName(){
        return name;
    }

    public EquipmentNumeration getNumeration() {
        return numeration;
    }

    public Integer getCost() {
        return cost;
    }

    public EquipmentTeam getTeam() {
        return team;
    }

    public Integer getReward() {
        return reward;
    }

    void getData(final AlertDialog... dialog){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer item = dataSnapshot.child("numeration").child("item").getValue(Integer.class);
                Integer idCategory = dataSnapshot.child("numeration").child("category").child("id").getValue(Integer.class);
                String description = dataSnapshot.child("numeration").child("category").child("description").getValue(String.class);

                if(item == null){
                    item = 0;
                }

                EquipmentCategory category = EquipmentCategory.NONE;
                if(description != null && idCategory != null){
                    for (EquipmentCategory categoryAux : acceptedCategories){
                        if(categoryAux.getDescription().equals(description) && categoryAux.getId() == idCategory){
                            category = categoryAux;
                            break;
                        }
                    }
                }

                String equipmentTeam = dataSnapshot.child("team").getValue(String.class);

                for(EquipmentTeam teamAux : EquipmentTeam.values()){
                    if(teamAux.getTeam().equals(equipmentTeam)){
                        team = teamAux;
                        break;
                    }
                }

                name = dataSnapshot.child("name").getValue(String.class);
                numeration = new EquipmentNumeration(item, category);
                cost = dataSnapshot.child("cost").getValue(Integer.class);
                reward = dataSnapshot.child("reward").getValue(Integer.class);

                if(reward == null) reward = 0;

                if (dialog.length != 0)
                    dialog[0].dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("", "Failed to read value.", databaseError.toException());
            }
        });
    }

    @NotNull
    @Override
    public String toString() {
        return (name == null) ? "" : name;
    }


}
