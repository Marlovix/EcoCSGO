package es.ulpgc.tfm.ecocsgo.model

import es.ulpgc.tfm.ecocsgo.db.AppDatabase
import es.ulpgc.tfm.ecocsgo.db.AppHelperDB
import kotlin.collections.ArrayList

class Game(private val enemyTeam: EquipmentTeamEnum) {

    internal val ROUNDS = 30
    internal val ENEMIES = 5

    var roundInGame: Int = 1
    var rounds: Array<Round?> = arrayOfNulls(ROUNDS)
    var enemyEconomy: Int = 0

    var economy: EconomyGame? = null

    private var customerHelper: AppHelperDB? = null
    private var helper: AppDatabase? = null

    init {
        createGameRounds()
    }

    private fun createGameRounds() {
        for (i in 0 until ROUNDS) rounds[i] = Round(enemyTeam, ENEMIES)
    }

    fun initRound(){
        if(roundInGame == 1 || roundInGame == (ROUNDS / 2) + 1){
            var numeration = EquipmentNumeration(1, EquipmentCategoryEnum.PISTOL)
            var secondaryGun = findGun(numeration) as SecondaryWeapon
            rounds[roundInGame-1]?.initPlayers(secondaryGun)
        }
    }

    fun findGun(numeration: EquipmentNumeration): Weapon? {

        val listWeapons: ArrayList<Weapon>? = ArrayList()
        when (numeration.category) {
            EquipmentCategoryEnum.PISTOL -> {
                //for (pistolWeapon in pistolWeapons) listWeapons?.add(pistolWeapon)
            }
            EquipmentCategoryEnum.HEAVY -> {
                //for (heavyWeapon in heavyWeapons) listWeapons?.add(heavyWeapon)
            }
            EquipmentCategoryEnum.SMG -> {
                //for (smgWeapon in smgWeapons) listWeapons?.add(smgWeapon)
            }
            EquipmentCategoryEnum.RIFLE -> {
                //for (rifleWeapon in rifleWeapons) listWeapons?.add(rifleWeapon)
            }
            else -> return null
        }
        return selectGun(listWeapons, numeration.item)
    }

    fun selectGun(listWeapons: ArrayList<Weapon>?, item: Int): Weapon? {
        for (i in listWeapons?.indices!!) {
            val gun = listWeapons[i]
            if (gun.numeration.item == item && gun.team == enemyTeam) return gun
        }
        return null
    }

}