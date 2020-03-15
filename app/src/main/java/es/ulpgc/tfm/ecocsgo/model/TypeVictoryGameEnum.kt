package es.ulpgc.tfm.ecocsgo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class TypeVictoryGameEnum(var quantity: Int) : Parcelable {
    DEFUSE(3500), EXPLOSION(3500), TEAM(3250), TIME(3250)
}