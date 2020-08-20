package es.ulpgc.tfm.ecocsgo.model

import android.os.Parcelable
import es.ulpgc.tfm.ecocsgo.R
import kotlinx.android.parcel.Parcelize

@Parcelize
enum class OriginEquipmentEnum(var colorResource: Int) : Parcelable {
    PURCHASED(R.color.bluePurchased),
    NO_PURCHASED(R.color.greenNoPurchased)
}