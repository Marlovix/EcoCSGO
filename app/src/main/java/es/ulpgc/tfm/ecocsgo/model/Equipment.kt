package es.ulpgc.tfm.ecocsgo.model

abstract class Equipment {
    //var referenceKey: String = ""
    var name: String = ""
    var team: EquipmentTeam? = null
    var numeration: EquipmentNumeration? = null
    var cost: Int = 0

    var acceptedCategories: Array<EquipmentCategory> = emptyArray()
}