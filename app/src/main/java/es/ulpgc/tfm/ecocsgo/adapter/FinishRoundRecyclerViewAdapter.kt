package es.ulpgc.tfm.ecocsgo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import es.ulpgc.tfm.ecocsgo.R
import es.ulpgc.tfm.ecocsgo.fragment.FinishRoundFragmentDialog
import es.ulpgc.tfm.ecocsgo.model.TypeVictoryGameEnum
import kotlinx.android.synthetic.main.item_finish_round.view.*

class FinishRoundRecyclerViewAdapter(
    private val values: Map<TypeVictoryGameEnum, String>,
    private val interaction: FinishRoundFragmentDialog.OnFinishRoundFragmentInteraction
) :  RecyclerView.Adapter<FinishRoundRecyclerViewAdapter.ViewHolder>()  {

    val indexValues : ArrayList<TypeVictoryGameEnum> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            FinishRoundRecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_finish_round, parent, false)
        val viewHolder = ViewHolder(view)
        view.setOnClickListener(viewHolder)
        return viewHolder
    }

    override fun onBindViewHolder(holder: FinishRoundRecyclerViewAdapter.ViewHolder,
                                  position: Int) {
        var i = 0
        for ((type, label) in values) {
            if(position == i){
                indexValues.add(position, type)
                holder.optionTextView.text = label
                break
            }
            i++
        }
    }

    override fun getItemCount(): Int {
        return values.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val optionTextView: TextView = view.textView_option

        override fun onClick(v: View?) {
            interaction.selectOption(indexValues[adapterPosition])
            //val weaponSelected = values[adapterPosition] as Weapon
            //interaction?.win(weaponSelected)
        }
    }

}