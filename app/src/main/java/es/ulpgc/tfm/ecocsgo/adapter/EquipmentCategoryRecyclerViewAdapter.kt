package es.ulpgc.tfm.ecocsgo.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.ulpgc.tfm.ecocsgo.R
import es.ulpgc.tfm.ecocsgo.fragment.WeaponListFragmentDialog
import es.ulpgc.tfm.ecocsgo.model.Equipment
import es.ulpgc.tfm.ecocsgo.model.EquipmentCategoryEnum
import kotlinx.android.synthetic.main.item_category_weapon.view.*

class EquipmentCategoryRecyclerViewAdapter(
    private val values: Map<EquipmentCategoryEnum, List<Equipment>>,
    private val weaponInGame: String,
    private val interaction: WeaponListFragmentDialog.OnWeaponListFragmentInteraction?
) :
    RecyclerView.Adapter<EquipmentCategoryRecyclerViewAdapter.ViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_weapon, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (values.size == 1) {
            val titles = context!!.resources.getStringArray(R.array.secondary_weapon_titles)
            holder.nameWeaponTextView.text = titles[position]
            holder.nameWeaponTextView.setTypeface(null, Typeface.BOLD)
            holder.weaponsRecyclerView.adapter = values[EquipmentCategoryEnum.PISTOL]?.let {
                WeaponRecyclerViewAdapter(it, weaponInGame, interaction)
            }
        } else if (values.size == 3) {
            val titles = context!!.resources.getStringArray(R.array.main_weapon_titles)
            holder.nameWeaponTextView.text = titles[position]
            holder.nameWeaponTextView.setTypeface(null, Typeface.BOLD)
            var adapter: WeaponRecyclerViewAdapter? = null
            when (position) {
                0 -> adapter = values[EquipmentCategoryEnum.HEAVY]?.let {
                    WeaponRecyclerViewAdapter(it, weaponInGame, interaction)
                }
                1 -> adapter = values[EquipmentCategoryEnum.SMG]?.let {
                    WeaponRecyclerViewAdapter(it, weaponInGame, interaction)
                }
                2 -> adapter = values[EquipmentCategoryEnum.RIFLE]?.let {
                    WeaponRecyclerViewAdapter(it, weaponInGame, interaction)
                }
            }
            holder.weaponsRecyclerView.adapter = adapter
        }
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameWeaponTextView: TextView = view.textView_category
        val weaponsRecyclerView: RecyclerView = view.weapon_list
    }
}