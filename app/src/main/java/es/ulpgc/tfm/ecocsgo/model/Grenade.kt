package es.ulpgc.tfm.ecocsgo.model

class Grenade() : Equipment() {
    init{
        this.acceptedCategories = arrayOf(EquipmentCategory.GRENADE)
    }
}