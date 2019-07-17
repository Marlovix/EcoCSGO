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
import es.ulpgc.tfm.ecocsgo.model.Player
import kotlinx.android.synthetic.main.item_player_list.view.*
import java.util.*

class PlayerRecyclerViewAdapter(
    private val parentActivity: GameActivity,
    private val values: List<Player>,
    private val twoPane: Boolean
) :
    RecyclerView.Adapter<PlayerRecyclerViewAdapter.ViewHolder>(),
    PlayerCallback.ItemTouchHelperContract {

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(values, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(values, i, i - 1)
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
                val fragment = ItemDetailFragment().apply {
                    arguments = Bundle().apply {
                        // putString(ItemDetailFragment.ARG_ITEM_KIT, item)
                    }
                }
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()
            } else {
                val intent = Intent(v.context, DetailPlayerActivity::class.java).apply {
                    //putExtra(ItemDetailFragment.ARG_GAME, parentActivity.game)
                    putExtra(ItemDetailFragment.ARG_PLAYER, player)
                }
                v.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_player_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player = values[position]

        if (player.mainGuns == null || player.mainGuns.isEmpty()) {
            holder.mainGun.text = "-"
            holder.mainDeaths.text = "0"
        } else {
            holder.mainGun.text = player.mainGuns[0].name
            holder.mainDeaths.text = player.mainGuns[0].casualty.toString()
        }

        if (player.secondaryGuns == null || player.secondaryGuns.isEmpty()) {
            holder.secondaryGun.text = "-"
            holder.secondaryDeaths.text = "0"
        } else {
            holder.secondaryGun.text = player.secondaryGuns[0].name
            holder.secondaryDeaths.text = player.secondaryGuns[0].casualty.toString()
        }

        with(holder.itemView) {
            tag = player
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.cardView_player
        val mainGun: TextView = view.textView_main_gun
        val secondaryGun: TextView = view.textView_secondary_gun
        val mainDeaths: TextView = view.textView_secondary_deaths
        val secondaryDeaths: TextView = view.textView_main_deaths
    }
}