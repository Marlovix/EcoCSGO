package es.ulpgc.tfm.ecocsgo.model

abstract class Weapon(name: String, team: EquipmentTeamEnum, numeration: EquipmentNumeration,
                      cost: Int, open var reward: Int, var casualty: Int = 0) :
    Equipment(name, team, numeration, cost)