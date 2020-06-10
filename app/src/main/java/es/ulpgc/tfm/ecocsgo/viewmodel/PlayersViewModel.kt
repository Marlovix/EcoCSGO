package es.ulpgc.tfm.ecocsgo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import es.ulpgc.tfm.ecocsgo.model.Player

class PlayersViewModel(application: Application) : AndroidViewModel(application) {

    var players: MutableLiveData<List<Player>> = MutableLiveData()

    fun getPlayers(): LiveData<List<Player>>? {
        return players
    }

    fun setPlayers(playersList: List<Player>){
        players?.value = playersList
    }

    fun loadPlayers() {
        players.value = listOf(
            /*Player(),
            Player(),
            Player(),
            Player(),
            Player()*/
        )
    }

}