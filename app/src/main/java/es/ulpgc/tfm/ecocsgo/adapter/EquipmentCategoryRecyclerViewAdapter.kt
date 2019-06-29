package es.ulpgc.tfm.ecocsgo.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.ulpgc.tfm.ecocsgo.GunListFragmentDialog
import es.ulpgc.tfm.ecocsgo.R
import es.ulpgc.tfm.ecocsgo.model.Equipment
import es.ulpgc.tfm.ecocsgo.model.EquipmentCategory
import es.ulpgc.tfm.ecocsgo.model.Gun
import kotlinx.android.synthetic.main.item_category_gun_list.view.*

class EquipmentCategoryRecyclerViewAdapter(
    private val values: Map<EquipmentCategory, List<Equipment>>,
    private val listener: GunListFragmentDialog.GunClickListener
) :
    RecyclerView.Adapter<EquipmentCategoryRecyclerViewAdapter.ViewHolder>(){

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category_gun_list, parent, false)
        context = view.context
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(values.size == 1){
            val titles = context!!.resources.getStringArray(R.array.secondary_gun_titles)
            holder.nameWeaponTextView.text = titles[position]
            holder.nameWeaponTextView.setTypeface(null, Typeface.BOLD)
            holder.gunsRecyclerView.adapter = values[EquipmentCategory.PISTOL]?.let {
                    GunRecyclerViewAdapter(it, EquipmentCategory.PISTOL, listener)
                }
        }else if(values.size == 3){
            val titles = context!!.resources.getStringArray(R.array.main_gun_titles)
            holder.nameWeaponTextView.text = titles[position]
            holder.nameWeaponTextView.setTypeface(null, Typeface.BOLD)
            var adapter : GunRecyclerViewAdapter? = null
            when (position) {
                0 -> adapter = values[EquipmentCategory.HEAVY]?.let {
                    GunRecyclerViewAdapter(it, EquipmentCategory.HEAVY, listener)
                }
                1 -> adapter = values[EquipmentCategory.SMG]?.let {
                    GunRecyclerViewAdapter(it, EquipmentCategory.SMG, listener)
                }
                2 -> adapter = values[EquipmentCategory.RIFLE]?.let {
                    GunRecyclerViewAdapter(it, EquipmentCategory.RIFLE, listener)
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