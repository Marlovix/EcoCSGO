package es.ulpgc.tfm.ecocsgo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.ulpgc.tfm.ecocsgo.R
import es.ulpgc.tfm.ecocsgo.fragment.WeaponListFragmentDialog
import es.ulpgc.tfm.ecocsgo.model.Equipment
import es.ulpgc.tfm.ecocsgo.model.Weapon
import kotlinx.android.synthetic.main.item_weapon.view.*

class WeaponRecyclerViewAdapter(
    private val values: List<Equipment>,
    private val weaponInGame: String,
    private val interaction: WeaponListFragmentDialog.OnWeaponListFragmentInteraction?
) : RecyclerView.Adapter<WeaponRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_weapon, parent, false)
        val viewHolder = ViewHolder(view)
        view.setOnClickListener(viewHolder)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weapon = values[position] as Weapon
        holder.nameWeaponTextView.text = weapon.name
        val cost = weapon.cost.toString() + "$"
        holder.costWeapon.text = cost
        if (weapon.name == weaponInGame) {
            weapon.inGame = true
            holder.selectedWeapon.visibility = View.VISIBLE
        } else {
            holder.selectedWeapon.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return values.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val nameWeaponTextView: TextView = view.textView_name
        val costWeapon: TextView = view.textView_cost
        val selectedWeapon: ImageView = view.imageView_selected

        override fun onClick(v: View?) {
            val weaponSelected = values[adapterPosition] as Weapon
            interaction?.selectWeapon(weaponSelected)
        }
    }
}