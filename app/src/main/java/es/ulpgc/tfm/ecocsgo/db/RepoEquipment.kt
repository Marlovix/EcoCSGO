package es.ulpgc.tfm.ecocsgo.db

import android.content.Context
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import es.ulpgc.tfm.ecocsgo.MainActivity
import es.ulpgc.tfm.ecocsgo.R
import es.ulpgc.tfm.ecocsgo.model.*

class RepoEquipment(val context: Context) {

    private var customerHelper: AppHelperDB? = null
    private var helper: AppDatabase? = null

    private var weaponsLoaded: Boolean = false
    private var grenadesLoaded: Boolean = false
    private var utilitiesLoaded: Boolean = false
    private var economyLoaded: Boolean = false

    fun loadData() {
        helper = AppDatabase(context)
        customerHelper = AppHelperDB(helper)
        customerHelper!!.open()

        loadWeapons()
        loadGrenades()
        loadUtilities()
        loadEconomy()
    }

    private fun loadEquipment(dataSnapshot: DataSnapshot, code: String): Equipment? {
        val snapshot = dataSnapshot.child(code)

        var item = snapshot.child("numeration").child("item").getValue(Int::class.java)
        val idCategory = snapshot.child("numeration").child("category")
            .child("id").getValue(Int::class.java)
        val description = snapshot.child("numeration").child("category")
            .child("description").getValue(String::class.java)

        if (item == null) {
            item = 0
        }

        var category = EquipmentCategoryEnum.NONE
        if (description != null && idCategory != null) {
            for (categoryAux in EquipmentCategoryEnum.values()) {
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

        return when (category) {
            EquipmentCategoryEnum.PISTOL ->
                loadWeapon(snapshot, name, equipmentTeam, numeration, cost)
            EquipmentCategoryEnum.HEAVY ->
                loadWeapon(snapshot, name, equipmentTeam, numeration, cost)
            EquipmentCategoryEnum.SMG ->
                loadWeapon(snapshot, name, equipmentTeam, numeration, cost)
            EquipmentCategoryEnum.RIFLE ->
                loadWeapon(snapshot, name, equipmentTeam, numeration, cost)
            EquipmentCategoryEnum.GEAR ->
                loadUtility(name, equipmentTeam, numeration, cost)
            EquipmentCategoryEnum.GRENADE ->
                loadGrenade(name, equipmentTeam, numeration, cost)
            else -> null
        }

    }

    private fun loadWeapons() {
        FirebaseDatabase.getInstance().getReference("weapons")
            .addValueEventListener(object : ValueEventListener {

                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (pistolCode: String in context.resources.getStringArray(R.array.pistol_data))
                        customerHelper!!.createWeapon(
                            loadEquipment(
                                snapshot.child("secondary"), pistolCode
                            ) as SecondaryWeapon
                        )
                    for (heavyCode: String in context.resources.getStringArray(R.array.heavy_data))
                        customerHelper!!.createWeapon(
                            loadEquipment(snapshot.child("heavy"), heavyCode) as MainWeapon
                        )
                    for (smgCode: String in context.resources.getStringArray(R.array.smg_data))
                        customerHelper!!.createWeapon(
                            loadEquipment(snapshot.child("smg"), smgCode) as MainWeapon
                        )
                    for (rifleCode: String in context.resources.getStringArray(R.array.rifle_data))
                        customerHelper!!.createWeapon(
                            loadEquipment(snapshot.child("rifle"), rifleCode) as MainWeapon
                        )

                    weaponsLoaded = true
                    checkDataLoaded()
                }
            })
    }

    private fun loadGrenades() {
        FirebaseDatabase.getInstance().getReference("grenades")
            .addValueEventListener(object : ValueEventListener {

                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (grenadeCode: String in context.resources.getStringArray(R.array.grenade_data))
                        customerHelper!!.createGrenade(
                            loadEquipment(
                                snapshot,
                                grenadeCode
                            ) as Grenade
                        )

                    grenadesLoaded = true
                    checkDataLoaded()
                }

            })
    }

    private fun loadUtilities() {
        FirebaseDatabase.getInstance().getReference("utilities")
            .addValueEventListener(object : ValueEventListener {

                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val defuseKitCode = context.resources.getString(R.string.defuse_kit_data)
                    val helmetCode = context.resources.getString(R.string.helmet_data)
                    val zeusCode = context.resources.getString(R.string.zeus_data)
                    val vestCode = context.resources.getString(R.string.vest_data)

                    customerHelper!!.createUtility(
                        loadEquipment(
                            snapshot,
                            defuseKitCode
                        ) as DefuseKit
                    )
                    customerHelper!!.createUtility(loadEquipment(snapshot, helmetCode) as Helmet)
                    customerHelper!!.createUtility(loadEquipment(snapshot, zeusCode) as Zeus)
                    customerHelper!!.createUtility(loadEquipment(snapshot, vestCode) as Vest)

                    utilitiesLoaded = true
                    checkDataLoaded()
                }

            })
    }

    private fun loadEconomy() {
        FirebaseDatabase.getInstance().getReference("economy")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val beginning = snapshot.child("beginning").getValue(Int::class.java)!!

                    val defeatBonus = ArrayList<Int>()
                    for (bonus in snapshot.child("defeat_bonus").children) defeatBonus.add(
                        bonus.getValue(
                            Int::class.java
                        )!!
                    )

                    val defuseBonus =
                        snapshot.child("defuse_bonus").getValue(Int::class.java)!!
                    val explosionBonus =
                        snapshot.child("explosion_bonus").getValue(Int::class.java)!!
                    val grenadeKill =
                        snapshot.child("grenade_kill").getValue(Int::class.java)!!
                    val killPartnerPenalty =
                        snapshot.child("kill_partner_penalty").getValue(Int::class.java)!!
                    val knifeKill =
                        snapshot.child("knife_kill").getValue(Int::class.java)!!
                    val leavingGame =
                        snapshot.child("leaving_game").getValue(Int::class.java)!!
                    val plantBonus =
                        snapshot.child("plant_bonus").getValue(Int::class.java)!!
                    val max = snapshot.child("max").getValue(Int::class.java)!!

                    val victory = HashMap<TypeVictoryGameEnum, Int>()
                    for (quantity in snapshot.child("victory").children) {
                        val typeVictory =
                            TypeVictoryGameEnum.valueOf(quantity.key!!)

                        when(typeVictory){
                            TypeVictoryGameEnum.TEAM -> {
                                val completeQuantity =
                                    quantity.getValue(Int::class.java)!! + explosionBonus
                                victory[typeVictory] = quantity.getValue(Int::class.java)!!
                                victory[TypeVictoryGameEnum.TEAM_BOMB] = completeQuantity
                            }
                            TypeVictoryGameEnum.EXPLOSION -> {
                                val completeQuantity =
                                    quantity.getValue(Int::class.java)!! + explosionBonus
                                victory[typeVictory] = completeQuantity
                            }
                            else -> victory[typeVictory] = quantity.getValue(Int::class.java)!!
                        }
                    }

                    customerHelper!!.createEconomy(
                        EconomyGame(
                            beginning, defeatBonus, defuseBonus,
                            explosionBonus, grenadeKill, killPartnerPenalty, knifeKill, leavingGame,
                            max, plantBonus, victory
                        )
                    )

                    economyLoaded = true
                    checkDataLoaded()
                }

            })
    }

    private fun loadWeapon(
        dataSnapshot: DataSnapshot, name: String, team: EquipmentTeamEnum,
        numeration: EquipmentNumeration,
        cost: Int
    ): Weapon? {
        val reward = dataSnapshot.child("reward").getValue(Int::class.java)!!
        return if (numeration.category == EquipmentCategoryEnum.PISTOL)
            SecondaryWeapon(
                name,
                team,
                numeration,
                cost,
                reward
            )
        else MainWeapon(
            name,
            team,
            numeration,
            cost,
            reward
        )
    }

    private fun loadGrenade(
        name: String, team: EquipmentTeamEnum, numeration: EquipmentNumeration,
        cost: Int
    ): Grenade? {
        return Grenade(
            name,
            team,
            numeration,
            cost
        )
    }

    private fun loadUtility(
        name: String, team: EquipmentTeamEnum, numeration: EquipmentNumeration,
        cost: Int
    ): Equipment? {
        return when (numeration.item) {
            1 -> Vest(
                name,
                team,
                numeration,
                cost
            )
            2 -> Helmet(
                name,
                team,
                numeration,
                cost
            )
            3 -> Zeus(
                name,
                team,
                numeration,
                cost
            )
            4 -> DefuseKit(
                name,
                team,
                numeration,
                cost
            )
            else -> null
        }
    }

    private fun checkDataLoaded() {
        if (weaponsLoaded && grenadesLoaded && utilitiesLoaded && economyLoaded) {
            customerHelper!!.close()
            val mainActivity = context as MainActivity
            mainActivity.finishRepoLoading()
        }
    }
}