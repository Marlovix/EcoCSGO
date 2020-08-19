package es.ulpgc.tfm.ecocsgo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EquipmentNumeration(var item: Int, var category: EquipmentCategoryEnum) : Parcelable