package es.ulpgc.tfm.ecocsgo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.ulpgc.tfm.ecocsgo.model.Gun
import es.ulpgc.tfm.ecocsgo.model.Player
import java.util.ArrayList

class PlayerViewModel : ViewModel()  {
    var player: MutableLiveData<Player>? = null

    fun init(player: MutableLiveData<Player>) {
        this.player = player
    }

    fun getPlayer(): LiveData<Player>? {
        return player
    }

    fun addGun(gun: Gun){

    }
}