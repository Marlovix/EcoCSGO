package es.ulpgc.tfm.ecocsgo.model

abstract class Weapon(name: String, team: EquipmentTeamEnum, numeration: EquipmentNumeration,
                      cost: Int, open var reward: Int,
                      open var inGame: Boolean, open var casualty: Int) :
    Equipment(name, team, numeration, cost)