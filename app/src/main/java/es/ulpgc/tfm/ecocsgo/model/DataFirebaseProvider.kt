package es.ulpgc.tfm.ecocsgo.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataFirebaseProvider(game: Game) {
    fun loadData(){
        loadEconomy()
        loadGrenades()
        loadUtilities()
        loadWeapons()
    }

    private fun loadEconomy(){
        FirebaseDatabase.getInstance().getReference("economy").addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
            }

        })
    }

    private fun loadGrenades(){
        FirebaseDatabase.getInstance().getReference("grenades").addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val list : ArrayList<Equipment> = ArrayList()
                for(children : DataSnapshot in snapshot.children){
                    val weapon : Equipment = children.getValue(Equipment::class.java)!!
                    list.add(weapon)
                }
            }

        })
    }

    private fun loadUtilities(){
        FirebaseDatabase.getInstance().getReference("utilities").addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
            }

        })
    }

    private fun loadWeapons(){
        FirebaseDatabase.getInstance().getReference("weapons").addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
            }

        })
    }

}