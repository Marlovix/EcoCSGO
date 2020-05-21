package es.ulpgc.tfm.ecocsgo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlin.collections.ArrayList

@Parcelize
data class Player(val equipmentTeam: EquipmentTeamEnum,
                  var secondaryGunInGame: SecondaryWeapon? = null,
                  var mainGunInGame: MainWeapon? = null,
                  var mainGuns: ArrayList<MainWeapon>? = ArrayList(),
                  var secondaryGuns: ArrayList<SecondaryWeapon>? = ArrayList(),
                  var vest: Vest? = null,
                  var helmet: Helmet? = null,
                  var defuseKit: DefuseKit? = null,
                  var alive: Boolean = true) : Parcelable {

    fun registerSecondaryGun(secondaryGun: SecondaryWeapon){
        secondaryGuns?.add(secondaryGun)
        secondaryGunInGame = secondaryGun
    }
}