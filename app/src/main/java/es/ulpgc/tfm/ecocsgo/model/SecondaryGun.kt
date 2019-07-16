package es.ulpgc.tfm.ecocsgo.model

class SecondaryGun : Gun() {
    init{
        this.acceptedCategories = arrayOf(EquipmentCategory.PISTOL)
    }
}