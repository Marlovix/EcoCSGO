package es.ulpgc.tfm.ecocsgo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class TypeVictoryGameEnum : Parcelable { DEFUSE, EXPLOSION, TEAM, TEAM_BOMB, TIME }