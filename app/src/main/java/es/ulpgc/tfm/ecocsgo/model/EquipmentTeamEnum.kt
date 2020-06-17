package es.ulpgc.tfm.ecocsgo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
enum class EquipmentTeamEnum(var team: String) : Parcelable {
    CT("CT"),
    T("T"),
    BOTH("BOTH");
}