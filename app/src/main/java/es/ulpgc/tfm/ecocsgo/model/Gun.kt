package es.ulpgc.tfm.ecocsgo.model

abstract class Gun(name: String, team: EquipmentTeamEnum, category: EquipmentCategory,
                   numeration: EquipmentNumeration, cost: Int,
                   open var reward: Int, var casualty: Int = 0) :
    Equipment(name, team, category, numeration, cost)