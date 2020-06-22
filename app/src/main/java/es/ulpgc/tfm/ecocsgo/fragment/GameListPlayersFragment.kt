package es.ulpgc.tfm.ecocsgo.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import es.ulpgc.tfm.ecocsgo.R
import es.ulpgc.tfm.ecocsgo.adapter.PlayersRecyclerViewAdapter
import es.ulpgc.tfm.ecocsgo.model.EquipmentCategoryEnum
import es.ulpgc.tfm.ecocsgo.model.EquipmentTeamEnum
import es.ulpgc.tfm.ecocsgo.model.Game
import es.ulpgc.tfm.ecocsgo.viewmodel.GameActivityViewModel
import es.ulpgc.tfm.ecocsgo.viewmodel.PlayerViewModel
import kotlinx.android.synthetic.main.fragment_list_players.*

class GameListPlayersFragment() : Fragment(){

    private var playersAdapter: PlayersRecyclerViewAdapter? = null
    private var onClickPlayerListener: View.OnClickListener? = null
    private var interaction: OnListPlayersFragmentInteraction? = null
    private var game: Game? = null

    private val gameViewModel: GameActivityViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListPlayersFragmentInteraction) {
            interaction = context
        } else {
            throw RuntimeException("$context must implement OnListPlayersFragmentInteraction")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onClickPlayerListener = View.OnClickListener { v ->
            interaction?.selectPlayer(v)
        }

        game = gameViewModel.getGame().value
        playersAdapter = game!!.players?.let {
            PlayersRecyclerViewAdapter(it, onClickPlayerListener!!)
        }
    }

    override fun onDetach() {
        interaction = null
        super.onDetach()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_players, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list_players.adapter = playersAdapter

        // observe de viewmodel???
    }

    interface OnListPlayersFragmentInteraction{
        fun selectPlayer(view: View)
    }

}