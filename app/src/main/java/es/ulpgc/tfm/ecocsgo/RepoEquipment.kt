package es.ulpgc.tfm.ecocsgo

import android.content.Context
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import es.ulpgc.tfm.ecocsgo.model.*

class RepoEquipment(var game: Game, val context: Context) {

    fun loadData(){
        loadWeapons()
        loadGrenades()
        loadUtilities()
        loadEconomy()
    }

    private fun loadEquipment(dataSnapshot: DataSnapshot, code: String) : Equipment?{
        val snapshot = dataSnapshot.child(code)

        var item = snapshot.child("numeration").child("item").getValue(Int::class.java)
        val idCategory = snapshot.child("numeration").child("category")
            .child("id").getValue(Int::class.java)
        val description = snapshot.child("numeration").child("category")
            .child("description").getValue(String::class.java)

        if (item == null) {
            item = 0
        }

        var category = EquipmentCategory.NONE
        if (description != null && idCategory != null) {
            for (categoryAux in EquipmentCategory.values()) {
                if (categoryAux.description == description && categoryAux.id == idCategory) {
                    category = categoryAux
                    break
                }
            }
        }

        var equipmentTeam = EquipmentTeamEnum.BOTH

        for (teamAux in EquipmentTeamEnum.values()) {
            if (teamAux.team == snapshot.child("team").getValue(String::class.java)) {
                equipmentTeam = teamAux
                break
            }
        }

        val name = snapshot.child("name").getValue(String::class.java)!!
        val numeration =
            EquipmentNumeration(item, category)
        val cost = snapshot.child("cost").getValue(Int::class.java)!!

        return when(category){
            EquipmentCategory.PISTOL ->
                loadGun(snapshot, name, equipmentTeam, category, numeration, cost)
            EquipmentCategory.HEAVY ->
                loadGun(snapshot, name, equipmentTeam, category, numeration, cost)
            EquipmentCategory.SMG ->
                loadGun(snapshot, name, equipmentTeam, category, numeration, cost)
            EquipmentCategory.RIFLE ->
                loadGun(snapshot, name, equipmentTeam, category, numeration, cost)
            EquipmentCategory.GEAR ->
                loadUtility(name, equipmentTeam, category, numeration, cost)
            EquipmentCategory.GRENADE ->
                loadGrenade(name, equipmentTeam, category, numeration, cost)
            else -> null
        }

    }

    private fun loadWeapons(){
        FirebaseDatabase.getInstance().getReference("weapons").
            addValueEventListener(object: ValueEventListener{

            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(pistolCode : String in context.resources.getStringArray(R.array.pistol_data))
                    game.pistolWeapons.add(loadEquipment(
                        snapshot.child("secondary"), pistolCode) as SecondaryGun
                    )
                for(heavyCode : String in context.resources.getStringArray(R.array.heavy_data))
                    game.heavyWeapons.add(
                        loadEquipment(snapshot.child("heavy"), heavyCode) as MainGun
                    )
                for(smgCode : String in context.resources.getStringArray(R.array.smg_data))
                    game.smgWeapons.add(
                        loadEquipment(snapshot.child("smg"), smgCode) as MainGun
                    )
                for(rifleCode : String in context.resources.getStringArray(R.array.rifle_data))
                    game.rifleWeapons.add(
                        loadEquipment(snapshot.child("rifle"), rifleCode) as MainGun
                    )

                val gameActivity = context as GameActivity
                gameActivity.finishRepoLoading()
            }
        })
    }

    private fun loadGrenades(){
        FirebaseDatabase.getInstance().getReference("grenades").
            addValueEventListener(object: ValueEventListener{

            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(grenadeCode : String in context.resources.getStringArray(R.array.grenade_data))
                    game.grenades.add(loadEquipment(snapshot, grenadeCode) as Grenade)
            }

        })
    }

    private fun loadUtilities(){
        FirebaseDatabase.getInstance().getReference("utilities").
            addValueEventListener(object: ValueEventListener{

            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val defuseKitCode = context.resources.getString(R.string.defuse_kit_data)
                val helmetCode = context.resources.getString(R.string.helmet_data)
                val zeusCode = context.resources.getString(R.string.zeus_data)
                val vestCode = context.resources.getString(R.string.vest_data)

                game.defuseKit = loadEquipment(snapshot, defuseKitCode) as DefuseKit
                game.helmet = loadEquipment(snapshot, helmetCode) as Helmet
                game.zeus = loadEquipment(snapshot, zeusCode) as Zeus
                game.vest = loadEquipment(snapshot, vestCode) as Vest
            }

        })
    }

    private fun loadEconomy(){
        FirebaseDatabase.getInstance().getReference("economy").addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val beginning = snapshot.child("beginning").getValue(Int::class.java)!!

                val defeatBonus = ArrayList<Int>()
                for (bonus in snapshot.child("defeat_bonus").children) defeatBonus.
                    add(bonus.getValue(Int::class.java)!!)

                val defuseBonus = snapshot.child("defuse_bonus").getValue(Int::class.java)!!
                val explosionBonus =
                    snapshot.child("explosion_bonus").getValue(Int::class.java)!!
                val grenadeKill = snapshot.child("grenade_kill").getValue(Int::class.java)!!
                val killPartnerPenalty =
                    snapshot.child("kill_partner_penalty").getValue(Int::class.java)!!
                val knifeKill = snapshot.child("knife_kill").getValue(Int::class.java)!!
                val leavingGame = snapshot.child("leaving_game").getValue(Int::class.java)!!
                val max = snapshot.child("max").getValue(Int::class.java)!!
                val plantBonus = snapshot.child("plant_bonus").getValue(Int::class.java)!!

                val victory = HashMap<TypeVictoryGameEnum, Int>()
                for (quantity in snapshot.child("victory").children)
                    victory[TypeVictoryGameEnum.valueOf(quantity.key!!)] =
                        quantity.getValue(Int::class.java)!!

                game.economy = EconomyGame(
                    beginning, defeatBonus, defuseBonus, explosionBonus,
                    grenadeKill, killPartnerPenalty, knifeKill,
                    leavingGame, max, plantBonus, victory
                )
            }

        })
    }

    private fun loadGun(dataSnapshot: DataSnapshot, name: String, team: EquipmentTeamEnum,
                        category: EquipmentCategory, numeration: EquipmentNumeration,
                        cost: Int) : Gun? {
        val reward = dataSnapshot.child("reward").getValue(Int::class.java)!!
        return if(category == EquipmentCategory.PISTOL)
            SecondaryGun(
                name,
                team,
                category,
                numeration,
                cost,
                reward
            )
        else MainGun(
            name,
            team,
            category,
            numeration,
            cost,
            reward
        )
    }

    private fun loadGrenade(name: String, team: EquipmentTeamEnum,
                            category: EquipmentCategory, numeration: EquipmentNumeration,
                            cost: Int) : Grenade? {
        return Grenade(
            name,
            team,
            category,
            numeration,
            cost
        )
    }

    private fun loadUtility(name: String, team: EquipmentTeamEnum,
                            category: EquipmentCategory, numeration: EquipmentNumeration,
                            cost: Int) : Equipment?{
        return when (numeration.item){
            1 -> Vest(
                name,
                team,
                category,
                numeration,
                cost
            )
            2 -> Helmet(
                name,
                team,
                category,
                numeration,
                cost
            )
            3 -> Zeus(
                name,
                team,
                category,
                numeration,
                cost
            )
            4 -> DefuseKit(
                name,
                team,
                category,
                numeration,
                cost
            )
            else -> null
        }
    }

}