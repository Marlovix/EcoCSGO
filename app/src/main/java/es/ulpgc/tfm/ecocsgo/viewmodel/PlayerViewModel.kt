package es.ulpgc.tfm.ecocsgo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.ulpgc.tfm.ecocsgo.model.Weapon
import es.ulpgc.tfm.ecocsgo.model.Player

class PlayerViewModel : ViewModel()  {
    var playerLiveData: MutableLiveData<Player>? = null

    fun getPlayer(): LiveData<Player>? {
        return playerLiveData
    }

    fun setPlayer(player: Player){
        if(playerLiveData == null){
            playerLiveData = MutableLiveData()
        }

        playerLiveData?.value = player
    }
}