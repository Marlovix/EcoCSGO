package es.ulpgc.tfm.ecocsgo.model

abstract class Equipment(
    open var name: String, open var team: EquipmentTeamEnum, open var category: EquipmentCategory,
    open var numeration: EquipmentNumeration, open var cost: Int)