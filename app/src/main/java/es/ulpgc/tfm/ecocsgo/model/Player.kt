package es.ulpgc.tfm.ecocsgo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlin.collections.ArrayList

@Parcelize
data class Player(val equipmentTeam: EquipmentTeamEnum,
                  var secondaryGunInGame: SecondaryGun? = null,
                  var mainGunInGame: MainGun? = null,
                  var mainGuns: ArrayList<MainGun>? = ArrayList(),
                  var secondaryGuns: ArrayList<SecondaryGun>? = ArrayList(),
                  var vest: Vest? = null,
                  var helmet: Helmet? = null,
                  var defuseKit: DefuseKit? = null,
                  var alive: Boolean = true) : Parcelable {

    fun registerSecondaryGun(secondaryGun: SecondaryGun){
        secondaryGuns?.add(secondaryGun)
        secondaryGunInGame = secondaryGun
    }
}