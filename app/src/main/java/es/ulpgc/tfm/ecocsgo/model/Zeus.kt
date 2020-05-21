package es.ulpgc.tfm.ecocsgo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Zeus(override var name: String, override var team: EquipmentTeamEnum,
                override var category: EquipmentCategoryEnum,
                override var numeration: EquipmentNumeration, override var cost: Int) :
    Equipment(name, team, category, numeration, cost), Parcelable