package es.ulpgc.tfm.ecocsgo.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import com.google.android.material.snackbar.Snackbar
import es.ulpgc.tfm.ecocsgo.R
import es.ulpgc.tfm.ecocsgo.adapter.PlayersRecyclerViewAdapter
import es.ulpgc.tfm.ecocsgo.model.EquipmentTeamEnum
import es.ulpgc.tfm.ecocsgo.viewmodel.GameActivityViewModel
import kotlinx.android.synthetic.main.fragment_list_players.*

class GameListPlayersFragment : Fragment(){

    private var playersAdapter: PlayersRecyclerViewAdapter? = null
    private var onClickPlayerListener: View.OnClickListener? = null
    private var interaction: OnListPlayersFragmentInteraction? = null
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

        onClickPlayerListener = View.OnClickListener { view ->
            val playerSelectedIndex = view.tag as Int
            interaction?.selectPlayer(playerSelectedIndex)
        }

        playersAdapter = gameViewModel.getGame().value?.players?.let {
            PlayersRecyclerViewAdapter(it, onClickPlayerListener!!)
        }
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

        fab_finish_round.setOnClickListener { v ->
            Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        gameViewModel.getPlayers().observe(viewLifecycleOwner) {
            list_players.adapter?.notifyDataSetChanged()
        }

        gameViewModel.getEnemyEconomy().observe(viewLifecycleOwner) {
            val team = if(gameViewModel.getGame().value?.enemyTeam == EquipmentTeamEnum.T)
                getString(R.string.label_terrorist) else getString(R.string.label_counter_terrorists)
            val text = team + "s: " + it + "$"
            textView_enemy_economy.text = text
        }
    }

    override fun onDetach() {
        interaction = null
        super.onDetach()
    }

    interface OnListPlayersFragmentInteraction{
        fun selectPlayer(selectedPlayerIndex: Int)
    }

}