package es.ulpgc.tfm.ecocsgo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class EquipmentCategoryEnum(var description: String, var id: Int) : Parcelable {
    NONE("", 0),
    PISTOL("PISTOL", 1),
    HEAVY("HEAVY", 2),
    SMG("SMG", 3),
    RIFLE("RIFLE", 4),
    GEAR("GEAR", 5),
    GRENADE("GRENADE", 6);
}