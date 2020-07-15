package es.ulpgc.tfm.ecocsgo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlin.collections.ArrayList

@Parcelize
data class Player(val equipmentTeam: EquipmentTeamEnum,
                  var mainWeapons: ArrayList<MainWeapon>? = ArrayList(),
                  var secondaryWeapons: ArrayList<SecondaryWeapon>? = ArrayList(),
                  var vest: Vest? = null,
                  var helmet: Helmet? = null,
                  var defuseKit: DefuseKit? = null,
                  var alive: Boolean = true) : Parcelable {

    fun registerMainWeapon(mainWeapon: MainWeapon){
        updateSelectedWeapon(mainWeapon)
        mainWeapons?.add(mainWeapon)
    }

    fun registerSecondaryWeapon(secondaryWeapon: SecondaryWeapon){
        updateSelectedWeapon(secondaryWeapon)
        secondaryWeapons?.add(secondaryWeapon)
    }

    fun removeSecondaryWeapon(weaponInGame: Weapon){
        secondaryWeapons?.remove(weaponInGame)
        if (!secondaryWeapons?.isEmpty()!!){
            updateSelectedWeapon(secondaryWeapons!!.first())
        }
    }

    fun removeMainWeapon(weaponInGame: Weapon){
        mainWeapons?.remove(weaponInGame)
        if (!mainWeapons?.isEmpty()!!){
             updateSelectedWeapon(mainWeapons!!.first())
        }
    }

    fun getMainWeaponInGame() : MainWeapon? {
        for (weapon in mainWeapons!!){
            if (weapon.inGame) return weapon
        }
        return null
    }

    fun getSecondaryWeaponInGame() : SecondaryWeapon? {
        for (weapon in secondaryWeapons!!){
            if (weapon.inGame) return weapon
        }
        return null
    }

    fun updateSelectedWeapon(weapon: Weapon){
        val lastWeaponInGame : Weapon? =
            if (weapon.numeration.category == EquipmentCategoryEnum.PISTOL)
            getSecondaryWeaponInGame() else getMainWeaponInGame()

        if(lastWeaponInGame != null) lastWeaponInGame.inGame = false

        weapon.inGame = true
    }
}