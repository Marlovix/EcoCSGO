package es.ulpgc.tfm.ecocsgo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MainWeapon(override var name: String, override var team: EquipmentTeamEnum,
                      override var numeration: EquipmentNumeration,
                      override var cost: Int, override var reward: Int,
                      override var inGame: Boolean = false, override var casualty: Int = 0) :
    Weapon(name, team, numeration, cost, reward, inGame, casualty), Parcelable{
    override fun toString(): String { return name }
}