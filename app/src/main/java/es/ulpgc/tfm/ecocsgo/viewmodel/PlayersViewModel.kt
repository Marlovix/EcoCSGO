package es.ulpgc.tfm.ecocsgo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import es.ulpgc.tfm.ecocsgo.model.Player

class PlayersViewModel(application: Application) : AndroidViewModel(application) {

    private var playersLiveData: MutableLiveData<List<Player>> = MutableLiveData()

    fun getPlayers() : MutableLiveData<List<Player>>{
        return playersLiveData
    }

    fun setPlayers(players: List<Player>) {
        playersLiveData.value = players
    }

    fun updatePlayer(player: Player) {
        //playersLiveData.value = players
    }

}