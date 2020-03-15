package es.ulpgc.tfm.ecocsgo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MainGun(override var name: String, override var team: EquipmentTeamEnum,
                   override var category: EquipmentCategory,
                   override var numeration: EquipmentNumeration,
                   override var cost: Int, override var reward: Int) :
    Gun(name, team, category, numeration, cost, reward), Parcelable