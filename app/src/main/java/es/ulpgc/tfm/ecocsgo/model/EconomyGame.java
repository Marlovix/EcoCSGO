package es.ulpgc.tfm.ecocsgo.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EconomyGame implements Parcelable {
    protected DatabaseReference reference;
    private Integer beginning;
    private ArrayList<Integer> defeatBonus;
    private Integer defuseBonus;
    private Integer explosionBonus;
    private Integer grenadeKill;
    private Integer killPartnerPenalty;
    private Integer knifeKill;
    private Integer leavingGame;
    private Integer max;
    private Integer plantBonus;
    private Map<TypeVictoryGame, Integer> typeVictories;

    public EconomyGame(final AlertDialog dialog, final Game game){
        defeatBonus = new ArrayList<>();
        typeVictories = new HashMap<>();
        reference = FirebaseDatabase.getInstance().getReference("economy");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                beginning = dataSnapshot.child("beginning").getValue(Integer.class);
                defuseBonus = dataSnapshot.child("defuse_bonus").getValue(Integer.class);
                explosionBonus = dataSnapshot.child("explosion_bonus").getValue(Integer.class);
                grenadeKill = dataSnapshot.child("grenade_kill").getValue(Integer.class);
                killPartnerPenalty = dataSnapshot.child("kill_partner_penalty").getValue(Integer.class);
                knifeKill = dataSnapshot.child("knife_kill").getValue(Integer.class);
                leavingGame = dataSnapshot.child("leaving_game").getValue(Integer.class);
                max = dataSnapshot.child("max").getValue(Integer.class);
                plantBonus = dataSnapshot.child("plant_bonus").getValue(Integer.class);

                for(DataSnapshot snapshot : dataSnapshot.child("defeat_bonus").getChildren()){
                    defeatBonus.add(snapshot.getValue(Integer.class));
                }
                for(DataSnapshot snapshot : dataSnapshot.child("victory").getChildren()){
                    typeVictories.put(TypeVictoryGame.valueOf(snapshot.getKey()), snapshot.getValue(Integer.class));
                }
                game.startRound(0);

                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("", "Failed to read value.", databaseError.toException());
                dialog.dismiss();
            }
        });
    }

    protected EconomyGame(Parcel in) {
        if (in.readByte() == 0) {
            beginning = null;
        } else {
            beginning = in.readInt();
        }
        if (in.readByte() == 0) {
            defuseBonus = null;
        } else {
            defuseBonus = in.readInt();
        }
        if (in.readByte() == 0) {
            explosionBonus = null;
        } else {
            explosionBonus = in.readInt();
        }
        if (in.readByte() == 0) {
            grenadeKill = null;
        } else {
            grenadeKill = in.readInt();
        }
        if (in.readByte() == 0) {
            killPartnerPenalty = null;
        } else {
            killPartnerPenalty = in.readInt();
        }
        if (in.readByte() == 0) {
            knifeKill = null;
        } else {
            knifeKill = in.readInt();
        }
        if (in.readByte() == 0) {
            leavingGame = null;
        } else {
            leavingGame = in.readInt();
        }
        if (in.readByte() == 0) {
            max = null;
        } else {
            max = in.readInt();
        }
        if (in.readByte() == 0) {
            plantBonus = null;
        } else {
            plantBonus = in.readInt();
        }
    }

    public static final Creator<EconomyGame> CREATOR = new Creator<EconomyGame>() {
        @Override
        public EconomyGame createFromParcel(Parcel in) {
            return new EconomyGame(in);
        }

        @Override
        public EconomyGame[] newArray(int size) {
            return new EconomyGame[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (beginning == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(beginning);
        }
        if (defuseBonus == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(defuseBonus);
        }
        if (explosionBonus == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(explosionBonus);
        }
        if (grenadeKill == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(grenadeKill);
        }
        if (killPartnerPenalty == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(killPartnerPenalty);
        }
        if (knifeKill == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(knifeKill);
        }
        if (leavingGame == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(leavingGame);
        }
        if (max == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(max);
        }
        if (plantBonus == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(plantBonus);
        }
    }
}
