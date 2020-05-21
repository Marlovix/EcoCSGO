package es.ulpgc.tfm.ecocsgo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MainWeapon(override var name: String, override var team: EquipmentTeamEnum,
                      override var category: EquipmentCategoryEnum,
                      override var numeration: EquipmentNumeration,
                      override var cost: Int, override var reward: Int) :
    Weapon(name, team, category, numeration, cost, reward), Parcelable