package es.ulpgc.tfm.ecocsgo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.ulpgc.tfm.ecocsgo.GunListFragmentDialog
import es.ulpgc.tfm.ecocsgo.R
import es.ulpgc.tfm.ecocsgo.model.Gun
import kotlinx.android.synthetic.main.item_gun_list.view.*

class GunRecyclerViewAdapter(
    private val values: List<Gun>,
    private val listener: GunListFragmentDialog.GunClickListener
) :
    RecyclerView.Adapter<GunRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_gun_list, parent, false)
        val viewHolder = ViewHolder(view)
        view.setOnClickListener(viewHolder)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameGunTextView.text = values[position].name
        val cost = values[position].cost.toString() + "$"
        holder.costGun.text = cost
        if(position % 2 == 0)
            holder.selectedGun.visibility = View.INVISIBLE
        else
            holder.selectedGun.visibility = View.VISIBLE
    }

    override fun getItemCount(): Int {
        return values.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val nameGunTextView: TextView = view.textView_name
        val costGun: TextView = view.textView_cost
        val selectedGun: ImageView = view.imageView_selected

        override fun onClick(v: View?) {
            listener.selectGun(v!!, adapterPosition)
        }
    }
}