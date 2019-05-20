package es.ulpgc.tfm.ecocsgo.model;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.database.*;

public abstract class Equipment {
    protected String reference;
    protected String name;
    protected EquipmentNumeration numeration;
    protected Integer cost;
    protected EquipmentTeam team;
    protected EquipmentCategory[] acceptedCategories;

    public Equipment(){

    }

    Equipment(String reference, String key){
        this.reference = reference;
        //this.key = key;
        getData(FirebaseDatabase.getInstance().getReference(reference).child(key));
    }

    public String getName(){
        return name;
    }

    private void getData(DatabaseReference reference){

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
                numeration = new EquipmentNumeration(category, item);
                cost = dataSnapshot.child("cost").getValue(Integer.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("", "Failed to read value.", databaseError.toException());
            }
        });
    }
}
