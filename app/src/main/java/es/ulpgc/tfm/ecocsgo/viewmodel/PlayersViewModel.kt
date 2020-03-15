package es.ulpgc.tfm.ecocsgo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.ulpgc.tfm.ecocsgo.model.Player
import java.util.*

class PlayersViewModel : ViewModel() {

    var players: MutableLiveData<ArrayList<Player>>? = null

    fun init(players: MutableLiveData<ArrayList<Player>>) {
        this.players = players
    }

    fun getPlayers(): LiveData<ArrayList<Player>>? {
        return players
    }

    fun s(players: ArrayList<Player>){
        this.players?.value = players
    }
}
