package es.ulpgc.tfm.ecocsgo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Helmet(
    override var name: String, override var team: EquipmentTeamEnum,
    override var numeration: EquipmentNumeration, override var cost: Int,
    var origin : OriginEquipmentEnum = OriginEquipmentEnum.PURCHASED
) :
    Equipment(name, team, numeration, cost), Parcelable