package es.ulpgc.tfm.ecocsgo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class OriginEquipmentEnum(var origin:String) : Parcelable {
    PURCHASED("PURCHASED"), FOUND("FOUND"), SAVED("SAVED")
}