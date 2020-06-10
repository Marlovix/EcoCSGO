package es.ulpgc.tfm.ecocsgo.adapter

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import es.ulpgc.tfm.ecocsgo.*
import es.ulpgc.tfm.ecocsgo.callback.PlayerCallback
import es.ulpgc.tfm.ecocsgo.fragment.DetailPlayerFragment
import es.ulpgc.tfm.ecocsgo.model.Player
import kotlinx.android.synthetic.main.item_player.view.*
import java.util.*

class PlayerRecyclerViewAdapter(
    private val parentActivity: GameActivity,
    private val players: ArrayList<Player>,
    private val twoPane: Boolean
) :
    RecyclerView.Adapter<PlayerRecyclerViewAdapter.ViewHolder>(),
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

    private val onClickListener: View.OnClickListener

    init {

        onClickListener = View.OnClickListener { v ->
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
                    .replace(R.id.item_detail_container, fragment)
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_player, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player = players[position]

        holder.mainGun.text = if (player.mainGunInGame != null)
            player.mainGunInGame!!.name else "-"
        holder.mainDeaths.text = if (player.mainGunInGame != null)
            "x" + player.mainGunInGame!!.name else "x0"
        holder.secondaryGun.text = if (player.secondaryGunInGame != null)
            player.secondaryGunInGame!!.name else "-"
        holder.secondaryDeaths.text = if (player.secondaryGunInGame != null)
            "x" + player.secondaryGunInGame!!.casualty.toString() else "x0"

        with(holder.itemView) {
            tag = player
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = players.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.cardView_player
        val mainGun: TextView = view.textView_main_gun
        val secondaryGun: TextView = view.textView_secondary_gun
        val mainDeaths: TextView = view.textView_secondary_deaths
        val secondaryDeaths: TextView = view.textView_main_deaths
    }
}