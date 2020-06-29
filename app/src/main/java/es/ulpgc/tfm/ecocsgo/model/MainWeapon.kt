package es.ulpgc.tfm.ecocsgo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MainWeapon(override var name: String, override var team: EquipmentTeamEnum,
                      override var numeration: EquipmentNumeration,
                      override var cost: Int, override var reward: Int) :
    Weapon(name, team, numeration, cost, reward), Parcelable{
    override fun toString(): String { return name }
}