package es.ulpgc.tfm.ecocsgo.model

import kotlin.collections.ArrayList

class Game(private val enemyTeam: EquipmentTeamEnum) {

    internal val ROUNDS = 30
    internal val ENEMIES = 5

    var roundInGame: Int = 1
    var rounds: Array<Round?> = arrayOfNulls(ROUNDS)
    var enemyEconomy: Int = 0

    var pistolWeapons: ArrayList<SecondaryGun> = ArrayList()
    var heavyWeapons: ArrayList<MainGun> = ArrayList()
    var smgWeapons: ArrayList<MainGun> = ArrayList()
    var rifleWeapons: ArrayList<MainGun> = ArrayList()
    var grenades: ArrayList<Equipment> = ArrayList()

    var defuseKit: DefuseKit? = null
    var helmet: Helmet? = null
    var vest: Vest? = null
    var zeus: Zeus? = null

    var economy: EconomyGame? = null

    init {
        createGameRounds()
    }

    private fun createGameRounds() {
        for (i in 0 until ROUNDS) rounds[i] = Round(enemyTeam, ENEMIES)
    }

    fun initRound(){
        if(roundInGame == 1 || roundInGame == (ROUNDS / 2) + 1){
            var numeration = EquipmentNumeration(1, EquipmentCategory.PISTOL)
            var secondaryGun = findGun(numeration) as SecondaryGun
            rounds[roundInGame-1]?.initPlayers(secondaryGun)
        }
    }

    fun findGun(numeration: EquipmentNumeration): Gun? {

        val listGuns: ArrayList<Gun>? = ArrayList()
        when (numeration.category) {
            EquipmentCategory.PISTOL -> {
                for (pistolWeapon in pistolWeapons) listGuns?.add(pistolWeapon)
            }
            EquipmentCategory.HEAVY -> {
                for (heavyWeapon in heavyWeapons) listGuns?.add(heavyWeapon)
            }
            EquipmentCategory.SMG -> {
                for (smgWeapon in smgWeapons) listGuns?.add(smgWeapon)
            }
            EquipmentCategory.RIFLE -> {
                for (rifleWeapon in rifleWeapons) listGuns?.add(rifleWeapon)
            }
            else -> return null
        }
        return selectGun(listGuns, numeration.item)
    }

    fun selectGun(listGuns: ArrayList<Gun>?, item: Int): Gun? {
        for (i in listGuns?.indices!!) {
            val gun = listGuns[i]
            if (gun.numeration.item == item && gun.team == enemyTeam) return gun
        }
        return null
    }

}