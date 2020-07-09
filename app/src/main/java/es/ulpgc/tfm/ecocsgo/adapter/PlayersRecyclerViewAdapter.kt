package es.ulpgc.tfm.ecocsgo.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import es.ulpgc.tfm.ecocsgo.*
import es.ulpgc.tfm.ecocsgo.callback.PlayerCallback
import es.ulpgc.tfm.ecocsgo.model.Player
import kotlinx.android.synthetic.main.item_player.view.*
import java.util.*

class PlayersRecyclerViewAdapter(
    private var players: ArrayList<Player>, private val listener : View.OnClickListener
) :
    RecyclerView.Adapter<PlayersRecyclerViewAdapter.ViewHolder>(),
    PlayerCallback.ItemTouchHelperContract {

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(players, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(players, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onRowSelected(myViewHolder: ViewHolder) {
        myViewHolder.cardView.setBackgroundColor(Color.LTGRAY)
    }

    override fun onRowClear(myViewHolder: ViewHolder) {
        myViewHolder.cardView.setBackgroundColor(Color.WHITE)
    }
/*
    init {

        listener = View.OnClickListener { v ->
            val player = v.tag as Player
            if (twoPane) {
                val fragment = DetailPlayerFragment()
                    .apply {
                    arguments = Bundle().apply {
                        putParcelable(DetailPlayerFragment.ARG_PLAYER, player)
                        /*putParcelableArrayList(DetailPlayerFragment.ARG_PISTOL, parentActivity.game?.pistolWeapons)
                        putParcelableArrayList(DetailPlayerFragment.ARG_HEAVY, parentActivity.game?.heavyWeapons)
                        putParcelableArrayList(DetailPlayerFragment.ARG_SMG, parentActivity.game?.smgWeapons)
                        putParcelableArrayList(DetailPlayerFragment.ARG_RIFLE, parentActivity.game?.rifleWeapons)
                        putParcelable(DetailPlayerFragment.ARG_DEFUSE_KIT, parentActivity.game?.defuseKit)
                        putParcelable(DetailPlayerFragment.ARG_HELMET, parentActivity.game?.helmet)
                        putParcelable(DetailPlayerFragment.ARG_VEST, parentActivity.game?.vest)*/
                    }
                }
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_detail_player, fragment)
                    .commit()
            } else {
                val intent = Intent(v.context, DetailPlayerActivity::class.java).apply {
                    putExtra(DetailPlayerFragment.ARG_PLAYER, player)
                    /*putParcelableArrayListExtra(DetailPlayerFragment.ARG_PISTOL, parentActivity.game?.pistolWeapons)
                    putParcelableArrayListExtra(DetailPlayerFragment.ARG_HEAVY, parentActivity.game?.heavyWeapons)
                    putParcelableArrayListExtra(DetailPlayerFragment.ARG_SMG, parentActivity.game?.smgWeapons)
                    putParcelableArrayListExtra(DetailPlayerFragment.ARG_RIFLE, parentActivity.game?.rifleWeapons)
                    putExtra(DetailPlayerFragment.ARG_DEFUSE_KIT, parentActivity.game?.defuseKit)
                    putExtra(DetailPlayerFragment.ARG_HELMET, parentActivity.game?.helmet)
                    putExtra(DetailPlayerFragment.ARG_VEST, parentActivity.game?.vest)*/
                }
                v.context.startActivity(intent)
            }
        }
    }*/

    /*
    fun setPlayers(players : List<Player>){
        this.players = players as ArrayList<Player>
        notifyDataSetChanged()
    }
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_player, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player = players[position]

        updateView(holder, player)

        with(holder.itemView) {
            tag = position
            setOnClickListener(listener)
        }

        with(holder.togglePlayerAlive) {
            setOnCheckedChangeListener { _, isChecked ->
                player.alive = isChecked
            }
        }
    }

    override fun getItemCount() = players.size

    private fun updateView(holder: ViewHolder, player: Player){
        holder.mainWeapon.text = if (player.getMainWeaponInGame() != null)
            player.getMainWeaponInGame()!!.name else "-"
        holder.mainDeaths.text = if (player.getMainWeaponInGame() != null)
            "x" + player.getMainWeaponInGame()!!.casualty else "x0"
        holder.secondaryWeapon.text = if (player.getSecondaryWeaponInGame() != null)
            player.getSecondaryWeaponInGame()!!.name else "-"
        holder.secondaryDeaths.text = if (player.getSecondaryWeaponInGame() != null)
            "x" + player.getSecondaryWeaponInGame()!!.casualty else "x0"

        holder.togglePlayerAlive.isChecked = player.alive
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.cardView_player
        val mainWeapon: TextView = view.textView_main_weapon
        val secondaryWeapon: TextView = view.textView_secondary_weapon
        val mainDeaths: TextView = view.textView_main_deaths
        val secondaryDeaths: TextView = view.textView_secondary_deaths
        val togglePlayerAlive: ToggleButton = view.toggleButton_player_alive
        /*
        togglePlayerAlive.setOnCheckedChangeListener( new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked) {
                doSomethingWith(toggleButton, isChecked) ;
            }
        }) ;*/
    }
}