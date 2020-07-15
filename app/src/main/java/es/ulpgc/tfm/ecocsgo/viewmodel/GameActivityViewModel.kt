package es.ulpgc.tfm.ecocsgo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import es.ulpgc.tfm.ecocsgo.model.Game
import es.ulpgc.tfm.ecocsgo.model.Player

class GameActivityViewModel(application: Application) : AndroidViewModel(application) {

    private var gameLiveData: MutableLiveData<Game>? = null
    private var playersLiveData: MutableLiveData<ArrayList<Player>>? = MutableLiveData()
    private var selectedPlayerIndexLiveData: MutableLiveData<Int>? = MutableLiveData()
    private var enemyEconomyLiveData: MutableLiveData<Int>? = MutableLiveData()

    fun getGame() : MutableLiveData<Game>{
        if (gameLiveData == null) {
            gameLiveData = MutableLiveData()
            gameLiveData?.value = Game(getApplication())
        }
        return gameLiveData as MutableLiveData<Game>
    }

    fun getPlayers() : MutableLiveData<ArrayList<Player>>{
        return playersLiveData as MutableLiveData<ArrayList<Player>>
    }

    fun updatePlayers(players: ArrayList<Player>){
        playersLiveData?.value = players
        gameLiveData?.value?.players = players
    }

    fun getSelectedPlayer() : MutableLiveData<Int>{
        return selectedPlayerIndexLiveData as MutableLiveData<Int>
    }

    fun setSelectedPlayer(index: Int){
        selectedPlayerIndexLiveData?.value = index
    }

    fun setEnemyEconomy(enemyEconomy: Int){
        enemyEconomyLiveData?.value = enemyEconomy
        gameLiveData?.value?.enemyEconomy = enemyEconomy
    }

    fun getEnemyEconomy() : MutableLiveData<Int>{
        return enemyEconomyLiveData as MutableLiveData<Int>
    }

}