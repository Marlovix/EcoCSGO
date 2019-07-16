package es.ulpgc.tfm.ecocsgo.model

class MainGun : Gun() {
    init{
        this.acceptedCategories = arrayOf(EquipmentCategory.HEAVY, EquipmentCategory.SMG, EquipmentCategory.RIFLE)
    }
}