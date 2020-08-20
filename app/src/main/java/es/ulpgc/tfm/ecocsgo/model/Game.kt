package es.ulpgc.tfm.ecocsgo.model

import android.content.Context
import es.ulpgc.tfm.ecocsgo.db.AppDatabase
import es.ulpgc.tfm.ecocsgo.db.AppHelperDB
import java.util.ArrayList

class Game(context : Context) {

    companion object {
        const val ROUNDS = 30
        const val ENEMIES = 5
    }

    var roundInGame: Int = 1
    var players: ArrayList<Player>? = ArrayList()
    var enemyEconomy: Int = 0
    var enemyTeam: EquipmentTeamEnum? = null
    var economy: EconomyGame? = null
    var infoGame: InfoGame? = null

    private var appDatabase: AppDatabase? = null
    private var appHelperDB: AppHelperDB? = null

    init {
        appDatabase = AppDatabase(context)
        appHelperDB = AppHelperDB(appDatabase)

        appHelperDB!!.open()
        economy = appHelperDB!!.fetchEconomyGame()

        infoGame = InfoGame()
    }

    fun createPlayers(enemyTeamEnum: EquipmentTeamEnum){
        enemyTeam = enemyTeamEnum
        for (i in 0 until ENEMIES) players?.add(Player(enemyTeam!!))
    }

    fun initRound(){
        if(roundInGame == 1 || roundInGame == (ROUNDS / 2) + 1){
            val numeration = EquipmentNumeration(1, EquipmentCategoryEnum.PISTOL)
            val secondaryWeapon = findWeaponByNumeration(numeration, enemyTeam!!) as SecondaryWeapon
            for (player in players!!){
                player.registerSecondaryWeapon(secondaryWeapon.copy())
                enemyEconomy += economy?.beginning!!
            }
        }
    }

    fun findWeaponByNumeration(numeration: EquipmentNumeration,
                            team: EquipmentTeamEnum = EquipmentTeamEnum.BOTH): Weapon? {
        appHelperDB!!.open()
        return appHelperDB!!.fetchWeaponByNumeration(numeration, team)
    }

}