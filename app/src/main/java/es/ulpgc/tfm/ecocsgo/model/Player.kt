package es.ulpgc.tfm.ecocsgo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Player(
    val equipmentTeam: EquipmentTeamEnum,
    var mainWeapons: ArrayList<MainWeapon>? = ArrayList(),
    var secondaryWeapons: ArrayList<SecondaryWeapon>? = ArrayList(),
    var vest: Vest? = null,
    var helmet: Helmet? = null,
    var defuseKit: DefuseKit? = null,
    var alive: Boolean = true
) : Parcelable {

    fun registerMainWeapon(mainWeapon: MainWeapon) {
        updateSelectedWeapon(mainWeapon)
        mainWeapons?.add(mainWeapon)
    }

    fun registerSecondaryWeapon(secondaryWeapon: SecondaryWeapon) {
        updateSelectedWeapon(secondaryWeapon)
        secondaryWeapons?.add(secondaryWeapon)
    }

    fun removeSecondaryWeapon(weaponInGame: Weapon) {
        secondaryWeapons?.remove(weaponInGame)
        if (!secondaryWeapons?.isEmpty()!!) {
            updateSelectedWeapon(secondaryWeapons!!.first())
        }
    }

    fun removeMainWeapon(weaponInGame: Weapon) {
        mainWeapons?.remove(weaponInGame)
        if (!mainWeapons?.isEmpty()!!) {
            updateSelectedWeapon(mainWeapons!!.first())
        }
    }

    fun getMainWeaponInGame(): MainWeapon? {
        for (weapon in mainWeapons!!) {
            if (weapon.inGame) return weapon
        }
        return null
    }

    fun getSecondaryWeaponInGame(): SecondaryWeapon? {
        for (weapon in secondaryWeapons!!) {
            if (weapon.inGame) return weapon
        }
        return null
    }

    fun updateSelectedWeapon(weapon: Weapon) {
        val lastWeaponInGame: Weapon? =
            if (weapon.numeration.category == EquipmentCategoryEnum.PISTOL)
                getSecondaryWeaponInGame() else getMainWeaponInGame()

        if (lastWeaponInGame != null) lastWeaponInGame.inGame = false

        weapon.inGame = true
    }

    fun prepareMainWeapons() {
        val weaponInGame = getMainWeaponInGame()
        mainWeapons?.clear()
        weaponInGame?.casualty = 0
        weaponInGame?.origin = OriginEquipmentEnum.NO_PURCHASED
        weaponInGame?.let { registerMainWeapon(it) }
    }

    fun prepareSecondaryWeapon() {
        val weaponInGame = getSecondaryWeaponInGame()
        secondaryWeapons?.clear()
        weaponInGame?.casualty = 0
        weaponInGame?.origin = OriginEquipmentEnum.NO_PURCHASED
        weaponInGame?.let { registerSecondaryWeapon(it) }
    }

    fun prepareUtility() {
        if(vest != null){
            vest!!.origin = OriginEquipmentEnum.NO_PURCHASED
        }
        if(helmet != null){
            helmet!!.origin = OriginEquipmentEnum.NO_PURCHASED
        }
        if(defuseKit != null){
            defuseKit!!.origin = OriginEquipmentEnum.NO_PURCHASED
        }
    }

    fun resetUtility() {
        vest = null
        helmet = null
        defuseKit = null
    }
}