package es.ulpgc.tfm.ecocsgo.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.ulpgc.tfm.ecocsgo.fragment.GunListFragmentDialog
import es.ulpgc.tfm.ecocsgo.R
import es.ulpgc.tfm.ecocsgo.model.Equipment
import es.ulpgc.tfm.ecocsgo.model.EquipmentCategoryEnum
import kotlinx.android.synthetic.main.item_category_gun.view.*

class EquipmentCategoryRecyclerViewAdapter(
    private val values: Map<EquipmentCategoryEnum, List<Equipment>>,
    private val interaction: GunListFragmentDialog.OnGunListFragmentInteraction?
) :
    RecyclerView.Adapter<EquipmentCategoryRecyclerViewAdapter.ViewHolder>(){

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_gun, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(values.size == 1){
            val titles = context!!.resources.getStringArray(R.array.secondary_gun_titles)
            holder.nameWeaponTextView.text = titles[position]
            holder.nameWeaponTextView.setTypeface(null, Typeface.BOLD)
            holder.gunsRecyclerView.adapter = values[EquipmentCategoryEnum.PISTOL]?.let {
                    GunRecyclerViewAdapter(it, EquipmentCategoryEnum.PISTOL, interaction)
                }
        }else if(values.size == 3){
            val titles = context!!.resources.getStringArray(R.array.main_gun_titles)
            holder.nameWeaponTextView.text = titles[position]
            holder.nameWeaponTextView.setTypeface(null, Typeface.BOLD)
            var adapter : GunRecyclerViewAdapter? = null
            when (position) {
                0 -> adapter = values[EquipmentCategoryEnum.HEAVY]?.let {
                    GunRecyclerViewAdapter(it, EquipmentCategoryEnum.HEAVY, interaction)
                }
                1 -> adapter = values[EquipmentCategoryEnum.SMG]?.let {
                    GunRecyclerViewAdapter(it, EquipmentCategoryEnum.SMG, interaction)
                }
                2 -> adapter = values[EquipmentCategoryEnum.RIFLE]?.let {
                    GunRecyclerViewAdapter(it, EquipmentCategoryEnum.RIFLE, interaction)
                }
            }
            holder.gunsRecyclerView.adapter = adapter
        }
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameWeaponTextView : TextView = view.textView_category
        val gunsRecyclerView : RecyclerView = view.gun_list
    }
}