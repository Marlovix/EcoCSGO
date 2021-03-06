package es.ulpgc.tfm.ecocsgo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Grenade(
    override var name: String, override var team: EquipmentTeamEnum,
    override var numeration: EquipmentNumeration, override var cost: Int
) :
    Equipment(name, team, numeration, cost), Parcelable