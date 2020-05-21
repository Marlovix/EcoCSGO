package es.ulpgc.tfm.ecocsgo.model

abstract class Weapon(name: String, team: EquipmentTeamEnum, category: EquipmentCategoryEnum,
                      numeration: EquipmentNumeration, cost: Int,
                      open var reward: Int, var casualty: Int = 0) :
    Equipment(name, team, category, numeration, cost)