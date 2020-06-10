package es.ulpgc.tfm.ecocsgo.model

import android.content.Context
import es.ulpgc.tfm.ecocsgo.db.AppDatabase
import es.ulpgc.tfm.ecocsgo.db.AppHelperDB

class Game(context : Context, private val enemyTeam: EquipmentTeamEnum) {

    internal val ROUNDS = 30
    internal val ENEMIES = 5

    var roundInGame: Int = 1
    var rounds: Array<Round?> = arrayOfNulls(ROUNDS)
    var enemyEconomy: Int = 0

    var economy: EconomyGame? = null

    private var appDatabase: AppDatabase? = null
    private var appHelperDB: AppHelperDB? = null

    init {
        appDatabase = AppDatabase(context)
        appHelperDB = AppHelperDB(appDatabase)

        val numeration = EquipmentNumeration(1, EquipmentCategoryEnum.PISTOL)

        val weapon = findGunByNumeration(numeration)
        val weapon2 = weapon
    }

    fun initRound(){
        if(roundInGame == 1 || roundInGame == (ROUNDS / 2) + 1){
            var numeration = EquipmentNumeration(1, EquipmentCategoryEnum.PISTOL)
            var secondaryGun = findGunByNumeration(numeration) as SecondaryWeapon
            rounds[roundInGame-1]?.initPlayers(secondaryGun)
        }
    }

    fun findGunByNumeration(numeration: EquipmentNumeration): Weapon? {
        appHelperDB!!.open()
        val weapon: Weapon? = appHelperDB!!.fetchWeaponByNumeration(numeration)
        appHelperDB!!.close()
        return weapon
    }

}