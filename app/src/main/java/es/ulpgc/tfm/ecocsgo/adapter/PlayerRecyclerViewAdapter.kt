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
import es.ulpgc.tfm.ecocsgo.model.EquipmentCategory
import es.ulpgc.tfm.ecocsgo.model.MainGun
import kotlinx.android.synthetic.main.item_player_list.view.*
import java.util.*
import kotlin.collections.HashMap

class PlayerRecyclerViewAdapter(
    private val parentActivity: GameActivity,
    private val values: List<GameActivityContent.PlayerContent>,
    private val twoPane: Boolean
) :
    RecyclerView.Adapter<PlayerRecyclerViewAdapter.ViewHolder>(),
    PlayerCallback.ItemTouchHelperContract{

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
            val item = v.tag as GameActivityContent.PlayerContent
            if (twoPane) {
                val fragment = ItemDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(ItemDetailFragment.ARG_ITEM_KIT, item.id)
                    }
                }
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()
            } else {
                val intent = Intent(v.context, DetailPlayerActivity::class.java).apply {
                    putExtra(ItemDetailFragment.ARG_ITEM_KIT, parentActivity.kit)
                    putExtra(ItemDetailFragment.ARG_HELMET, parentActivity.helmet)
                    putExtra(ItemDetailFragment.ARG_VEST, parentActivity.vest)

                    val mainGuns = HashMap<EquipmentCategory, List<MainGun>>()
                    mainGuns[EquipmentCategory.HEAVY] = parentActivity.heavyWeapons
                    mainGuns[EquipmentCategory.SMG] = parentActivity.smgWeapons
                    mainGuns[EquipmentCategory.RIFLE] = parentActivity.rifleWeapons

                    putExtra(ItemDetailFragment.ARG_MAIN_GUNS, mainGuns)
                    putExtra(ItemDetailFragment.ARG_SECONDARY_GUNS, parentActivity.pistolWeapons)
                    putExtra(ItemDetailFragment.ARG_GAME, parentActivity.game)
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
        val item = values[position]
        holder.idView.text = "-"//item.id
        holder.contentView.text = item.content
        holder.death.text = "x0"
        holder.death2.text = "x0"

        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView : CardView = view.cardView
        val idView: TextView = view.sub_text2
        val contentView: TextView = view.sub_text
        val death: TextView = view.textView2
        val death2: TextView = view.textView3
    }
}