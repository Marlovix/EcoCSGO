package es.ulpgc.tfm.ecocsgo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import es.ulpgc.tfm.ecocsgo.*
import es.ulpgc.tfm.ecocsgo.model.Player
import kotlinx.android.synthetic.main.item_player.view.*
import java.util.*

class PlayersRecyclerViewAdapter(
    private var players: ArrayList<Player>, private val listener : View.OnClickListener
) : RecyclerView.Adapter<PlayersRecyclerViewAdapter.ViewHolder>(){

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

        var color = android.R.color.white
        if (player.getMainWeaponInGame() != null){
            color = player.getMainWeaponInGame()?.origin?.colorResource!!
        }
        holder.mainWeapon.background = ContextCompat.getDrawable(holder.itemView.context, color)

        color = android.R.color.white
        if (player.getSecondaryWeaponInGame() != null){
            color = player.getSecondaryWeaponInGame()?.origin?.colorResource!!
        }
        holder.secondaryWeapon.background = ContextCompat.getDrawable(holder.itemView.context, color)

        holder.togglePlayerAlive.isChecked = player.alive
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mainWeapon: TextView = view.textView_main_weapon
        val secondaryWeapon: TextView = view.textView_secondary_weapon
        val mainDeaths: TextView = view.textView_main_deaths
        val secondaryDeaths: TextView = view.textView_secondary_deaths
        val togglePlayerAlive: ToggleButton = view.toggleButton_player_alive
    }
}