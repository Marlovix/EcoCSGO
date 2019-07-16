package es.ulpgc.tfm.ecocsgo.model

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import es.ulpgc.tfm.ecocsgo.R

class DataFirebaseProvider(var game: Game, val context: Context) {

    private var dialog : AlertDialog? = null

    fun loadData(){
        setProgressDialog()

        loadEconomy()
        loadGrenades()
        loadUtilities()
        loadWeapons()

        dialog?.dismiss()
    }

    private fun setProgressDialog() {
        val llPadding = 30
        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.HORIZONTAL
        ll.setPadding(llPadding, llPadding, llPadding, llPadding)
        ll.gravity = Gravity.CENTER
        var llParam = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        ll.layoutParams = llParam

        val progressBar = ProgressBar(context)
        progressBar.isIndeterminate = true
        progressBar.setPadding(0, 0, llPadding, 0)
        progressBar.layoutParams = llParam

        llParam = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        llParam.gravity = Gravity.CENTER
        val tvText = TextView(context)
        tvText.text = context.resources.getString(R.string.label_loading)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tvText.setTextColor(context.getColor(android.R.color.black))
        }
        tvText.textSize = 20f
        tvText.layoutParams = llParam

        ll.addView(progressBar)
        ll.addView(tvText)

        val builder = AlertDialog.Builder(context)
        builder.setCancelable(true)
        builder.setView(ll)

        dialog = builder.create()
        dialog!!.show()
        val window = dialog!!.window
        if (window != null) {
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.copyFrom(dialog!!.window!!.attributes)
            layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
            dialog!!.window!!.attributes = layoutParams
        }
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

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val list : ArrayList<Equipment> = ArrayList()
                val otraprueba = dataSnapshot.children
                for(grenadeCode : String in context.resources.getStringArray(R.array.grenade_data)){

                    //grenade.reward = dataSnapshot.child("reward").getValue(Int::class.java)

                    list.add(loadEquipment(dataSnapshot, grenadeCode, Grenade()))
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

    private fun loadEquipment(dataSnapshot: DataSnapshot, code: String, equipment: Equipment) : Equipment{
        val snapshot = dataSnapshot.child(code)

        var item = snapshot.child("numeration").child("item").getValue(Int::class.java)
        val idCategory =
            snapshot.child("numeration").child("category").child("id").getValue(Int::class.java)
        val description = snapshot.child("numeration").child("category").child("description").getValue(
            String::class.java
        )

        if (item == null) {
            item = 0
        }

        var category = EquipmentCategory.NONE
        if (description != null && idCategory != null) {
            for (categoryAux in equipment.acceptedCategories) {
                if (categoryAux.description == description && categoryAux.id == idCategory) {
                    category = categoryAux
                    break
                }
            }
        }

        val equipmentTeam = snapshot.child("team").getValue(String::class.java)

        for (teamAux in EquipmentTeam.values()) {
            if (teamAux.team == equipmentTeam) {
                equipment.team = teamAux
                break
            }
        }

        equipment.name = snapshot.child("name").getValue(String::class.java)!!
        equipment.numeration = EquipmentNumeration(item, category)
        equipment.cost = snapshot.child("cost").getValue(Int::class.java)!!

        return equipment
    }

    private fun loadGun(dataSnapshot: DataSnapshot, code: String, gun: Gun) : Gun? {
        val snapshot = dataSnapshot.child(code)
        if (gun is SecondaryGun){
            val secondaryGun = loadEquipment(dataSnapshot, code, gun) as SecondaryGun
            secondaryGun.reward = snapshot.child("reward").getValue(Int::class.java)
            return secondaryGun
        }else if(gun is MainGun){
            val mainGun = loadEquipment(dataSnapshot, code, gun) as MainGun
            mainGun.reward = snapshot.child("reward").getValue(Int::class.java)
            return mainGun
        }
        return null
    }

}