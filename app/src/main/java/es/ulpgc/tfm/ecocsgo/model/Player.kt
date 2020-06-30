package es.ulpgc.tfm.ecocsgo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlin.collections.ArrayList

@Parcelize
data class Player(val equipmentTeam: EquipmentTeamEnum,
                  var secondaryWeaponInGame: SecondaryWeapon? = null,
                  var mainWeaponInGame: MainWeapon? = null,
                  var mainWeapons: ArrayList<MainWeapon>? = ArrayList(),
                  var secondaryWeapons: ArrayList<SecondaryWeapon>? = ArrayList(),
                  var vest: Vest? = null,
                  var helmet: Helmet? = null,
                  var defuseKit: DefuseKit? = null,
                  var alive: Boolean = true) : Parcelable {

    fun registerSecondaryWeapon(secondaryWeapon: SecondaryWeapon){
        secondaryWeapon.inGame = true
        secondaryWeapons?.add(secondaryWeapon)
        secondaryWeaponInGame = secondaryWeapon
    }
}