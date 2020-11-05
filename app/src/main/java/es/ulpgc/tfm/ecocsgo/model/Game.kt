package es.ulpgc.tfm.ecocsgo.model

import android.content.Context
import es.ulpgc.tfm.ecocsgo.db.AppDatabase
import es.ulpgc.tfm.ecocsgo.db.AppHelperDB
import java.util.*

class Game(context: Context) {

    companion object {
        const val ROUNDS = 30
        const val ENEMIES = 5
        const val MAX_CONSECUTIVE_LOST = 4
    }

    var roundInGame: Int = 1
    var players: ArrayList<Player>? = ArrayList()
    var enemyEconomy: Int = 0
    var enemyTeam: EquipmentTeamEnum? = null
    var economy: EconomyGame? = null
    var infoGame: InfoGame? = null
    var consecutiveLostRounds : Int = 0

    private var appDatabase: AppDatabase? = null
    private var appHelperDB: AppHelperDB? = null

    init {
        appDatabase = AppDatabase(context)
        appHelperDB = AppHelperDB(appDatabase)

        appHelperDB!!.open()
        economy = appHelperDB!!.fetchEconomyGame()

        infoGame = InfoGame()
    }

    fun createPlayers(enemyTeamEnum: EquipmentTeamEnum) {
        enemyTeam = enemyTeamEnum
        for (i in 0 until ENEMIES) players?.add(Player(enemyTeam!!))
    }

    fun initRound() {
        if (roundInGame == 1 || roundInGame == (ROUNDS / 2) + 1) {
            for (player in players!!) {
                setDefaultSecondaryWeapon(player)
                enemyEconomy += economy?.beginning!!
            }
        }
    }

    fun findWeaponByNumeration(
        numeration: EquipmentNumeration,
        team: EquipmentTeamEnum = EquipmentTeamEnum.BOTH
    ): Weapon? {
        appHelperDB!!.open()
        return appHelperDB!!.fetchWeaponByNumeration(numeration, team)
    }

    fun calculateVictoryToEnemyEconomy(type: TypeFinalRoundEnum){
        val reward : Int = economy?.type!![type] ?: error("")
        val teamReward : Int =  reward * ENEMIES

        enemyEconomy += teamReward

        if(consecutiveLostRounds > 0){
            consecutiveLostRounds--
        }
    }

    fun calculateDefeatToEnemyEconomy(type: TypeFinalRoundEnum){
        val defeatBonus = economy?.defeatBonus?.get(consecutiveLostRounds)
        var teamReward : Int =  defeatBonus!! * ENEMIES

        // Losing as CT need not options //
        if (type == TypeFinalRoundEnum.TEAM_BOMB){
            teamReward += economy?.explosionBonus!! * ENEMIES
        }

        enemyEconomy += teamReward

        if (consecutiveLostRounds < MAX_CONSECUTIVE_LOST) {
            consecutiveLostRounds++
        }
    }

    fun calculateInfoGameToEnemyEconomy() {
        appHelperDB!!.open()
        val grenades = appHelperDB!!.fetchGrenades(enemyTeam!!)
        appHelperDB!!.close()

        for (grenade in grenades) {
            when (grenade.numeration.item) {
                1 -> enemyEconomy += infoGame?.molotov!! * grenade.cost
                2 -> enemyEconomy += infoGame?.decoy!! * grenade.cost
                3 -> enemyEconomy += infoGame?.flash!! * grenade.cost
                4 -> enemyEconomy += infoGame?.he!! * grenade.cost
                5 -> enemyEconomy += infoGame?.smoke!! * grenade.cost
            }
        }

        enemyEconomy += infoGame?.grenadeDeaths!! * economy?.grenadeKill!!
        enemyEconomy += infoGame?.knifeDeaths!! * economy?.knifeKill!!
        enemyEconomy -= infoGame?.partnerDeaths!! * economy?.killPartnerPenalty!!
        enemyEconomy += infoGame?.zeus!! * 200
        //TODO: Change this last value (200) for the cost of Zeus from BD //

        resetInfoGame()
    }

    fun calculatePlayersDataToEnemyEconomy() {
        for (player in players!!) {
            player.mainWeapons?.let { calculateEconomyFromWeapons(it) }
            player.secondaryWeapons?.let { calculateEconomyFromWeapons(it) }

            if (player.alive) {
                player.prepareMainWeapons()
                player.prepareSecondaryWeapon()
                player.prepareUtility()
            } else {
                player.mainWeapons?.clear()
                player.secondaryWeapons?.clear()
                player.resetUtility()
                setDefaultSecondaryWeapon(player)
            }
        }
    }

    private fun calculateEconomyFromUtility(player: Player){
        if (player.vest != null){
            enemyEconomy -= player.vest!!.cost
        }

        if (player.helmet != null){
            enemyEconomy -= player.helmet!!.cost
        }

        if (player.defuseKit != null){
            enemyEconomy -= player.defuseKit!!.cost
        }
    }

    private fun calculateEconomyFromWeapons(weapons: List<Weapon>) {
        if (weapons.isNotEmpty()) {
            for (weapon in weapons) {
                if (weapon.origin == OriginEquipmentEnum.PURCHASED) {
                    enemyEconomy -= weapon.cost
                }
                enemyEconomy += weapon.casualty * weapon.reward
            }
        }
    }

    private fun setDefaultSecondaryWeapon(player: Player){
        val numeration = EquipmentNumeration(1, EquipmentCategoryEnum.PISTOL)
        val secondaryWeapon =
            findWeaponByNumeration(numeration, enemyTeam!!) as SecondaryWeapon
        secondaryWeapon.origin = OriginEquipmentEnum.NO_PURCHASED
        player.registerSecondaryWeapon(secondaryWeapon.copy())
    }

    private fun resetInfoGame() {
        infoGame?.decoy = 0
        infoGame?.flash = 0
        infoGame?.grenadeDeaths = 0
        infoGame?.he = 0
        infoGame?.knifeDeaths = 0
        infoGame?.molotov = 0
        infoGame?.partnerDeaths = 0
        infoGame?.smoke = 0
        infoGame?.zeus = 0
    }

}