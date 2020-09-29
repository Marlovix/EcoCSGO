package es.ulpgc.tfm.ecocsgo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SecondaryWeapon(
    override var name: String, override var team: EquipmentTeamEnum,
    override var numeration: EquipmentNumeration,
    override var cost: Int, override var reward: Int,
    override var inGame: Boolean = false, override var casualty: Int = 0,
    override var origin: OriginEquipmentEnum = OriginEquipmentEnum.NO_PURCHASED
) :
    Weapon(name, team, numeration, cost, reward, inGame, casualty, origin), Parcelable {
    override fun toString(): String {
        return name
    }
}