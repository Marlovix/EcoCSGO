package es.ulpgc.tfm.ecocsgo.model

enum class EquipmentCategory(val description: String, val id: Int) {
    NONE("", 0),
    PISTOL("PISTOL", 1),
    HEAVY("HEAVY", 2),
    SMG("SMG", 3),
    RIFLE("RIFLE", 4),
    GEAR("GEAR", 5),
    GRENADE("GRENADE", 6);
}