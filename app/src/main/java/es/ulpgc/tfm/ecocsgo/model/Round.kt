package es.ulpgc.tfm.ecocsgo.model

import java.util.ArrayList

class Round(equipmentTeam: EquipmentTeamEnum, nPlayers: Int){
    var players: ArrayList<Player>? = ArrayList()

    init {
        for (i in 0 until nPlayers) players?.add(Player(equipmentTeam))
    }

    fun initPlayers(secondaryGun: SecondaryWeapon){
        for (player in players!!) player.registerSecondaryGun(secondaryGun)
    }
}