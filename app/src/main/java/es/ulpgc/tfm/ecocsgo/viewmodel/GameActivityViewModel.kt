package es.ulpgc.tfm.ecocsgo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import es.ulpgc.tfm.ecocsgo.model.EquipmentTeamEnum
import es.ulpgc.tfm.ecocsgo.model.Game

class GameActivityViewModel(application: Application) : AndroidViewModel(application) {

    //var gameLiveData: MutableLiveData<Game> = MutableLiveData()
    private var gameLiveData: MutableLiveData<Game>? = null

    fun getGame(team: EquipmentTeamEnum) : MutableLiveData<Game>{
        if (gameLiveData == null) {
            gameLiveData = MutableLiveData<Game>()
            gameLiveData?.value = Game(getApplication(), team)
            gameLiveData?.value?.initRound()
        }
        return gameLiveData as MutableLiveData<Game>
    }

}