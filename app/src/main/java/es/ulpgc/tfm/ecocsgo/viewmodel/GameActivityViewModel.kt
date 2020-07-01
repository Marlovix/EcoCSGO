package es.ulpgc.tfm.ecocsgo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import es.ulpgc.tfm.ecocsgo.model.Game

class GameActivityViewModel(application: Application) : AndroidViewModel(application) {

    private var gameLiveData: MutableLiveData<Game>? = null

    fun getGame() : MutableLiveData<Game>{
        if (gameLiveData == null) {
            gameLiveData = MutableLiveData()
            gameLiveData?.value = Game(getApplication())
        }
        return gameLiveData as MutableLiveData<Game>
    }

}