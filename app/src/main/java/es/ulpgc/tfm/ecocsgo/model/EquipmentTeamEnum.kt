package es.ulpgc.tfm.ecocsgo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
enum class EquipmentTeamEnum(var team: String) : Parcelable {
    CT("CT"),
    T("T"),
    BOTH("BOTH");

    companion object {
        fun filterTeam(team: String): EquipmentTeamEnum{
            for (value in values())
                if (value.name.toLowerCase(Locale.getDefault()) ==
                    team.toLowerCase(Locale.getDefault()))
                    return value
            return BOTH
        }
    }
}