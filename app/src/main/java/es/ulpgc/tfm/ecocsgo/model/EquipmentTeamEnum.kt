package es.ulpgc.tfm.ecocsgo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class EquipmentTeamEnum(var team: String) : Parcelable {
    CT("CT"),
    T("T"),
    BOTH("BOTH");
}