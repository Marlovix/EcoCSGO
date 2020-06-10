package es.ulpgc.tfm.ecocsgo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import es.ulpgc.tfm.ecocsgo.model.Player

class GameActivityViewModel(application: Application) : AndroidViewModel(application) {

    var players: MutableLiveData<List<Player>> = MutableLiveData()

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