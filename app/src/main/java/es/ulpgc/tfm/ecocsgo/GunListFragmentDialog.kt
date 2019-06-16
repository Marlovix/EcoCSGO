package es.ulpgc.tfm.ecocsgo

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import es.ulpgc.tfm.ecocsgo.model.EquipmentCategory
import es.ulpgc.tfm.ecocsgo.model.Gun
import kotlinx.android.synthetic.main.item_category_gun_list.view.*
import kotlinx.android.synthetic.main.item_gun_list.view.*
import java.util.*

class GunListFragmentDialog : DialogFragment() {

    private var guns : Map<EquipmentCategory, List<Gun>> = HashMap()

    //private var listener: OnListFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.category_gun_list, container, false)

        val bundle = arguments
        guns = bundle?.getSerializable(ItemDetailFragment.ARG_GUNS) as Map<EquipmentCategory, List<Gun>>

        val recyclerView = rootView.findViewById(R.id.category_gun_list) as RecyclerView
        setupRecyclerView(recyclerView)

        return rootView
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        val mAdapter = EquipmentCategoryRecyclerViewAdapter(this, guns)
        recyclerView.adapter = mAdapter
    }

    class EquipmentCategoryRecyclerViewAdapter(
        private val parentActivity: DialogFragment,
        private val values: Map<EquipmentCategory, List<Gun>>
    ) :
        RecyclerView.Adapter<EquipmentCategoryRecyclerViewAdapter.ViewHolder>(){

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as GameActivityContent.PlayerContent
                //val kit = listFragment.intent?.getParcelableExtra(ItemDetailFragment.ARG_ITEM_KIT)
                //val intent = Intent(v.context, DetailPlayerActivity::class.java).apply {
                    /*putExtra(ItemDetailFragment.ARG_ITEM_KIT, listFragment.kit)
                    putExtra(ItemDetailFragment.ARG_HELMET, listFragment.helmet)
                    putExtra(ItemDetailFragment.ARG_VEST, listFragment.vest)

                    putExtra(ItemDetailFragment.ARG_GAME, listFragment.game)*/
                //}
                //v.context.startActivity(intent)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_category_gun_list, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.nameWeaponTextView.text = values.keys.elementAt(position).toString()
            holder.nameWeaponTextView.setTypeface(null, Typeface.BOLD)
            when (position) {
                0 -> holder.gunsRecyclerView.adapter =
                    values[EquipmentCategory.SMG]?.let { GunRecyclerViewAdapter(parentActivity, it) }
                1 -> holder.gunsRecyclerView.adapter =
                    values[EquipmentCategory.HEAVY]?.let { GunRecyclerViewAdapter(parentActivity, it) }
                2 -> holder.gunsRecyclerView.adapter =
                    values[EquipmentCategory.RIFLE]?.let { GunRecyclerViewAdapter(parentActivity, it) }
                else -> { // Note the block
                    print("x no es 1 o 2")
                }
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val nameWeaponTextView : TextView = view.textView_category
            val gunsRecyclerView : RecyclerView = view.gun_list
        }
    }

    class GunRecyclerViewAdapter(
        private val parentActivity: DialogFragment,
        private val values: List<Gun>
    ) :
        RecyclerView.Adapter<GunRecyclerViewAdapter.ViewHolder>(){

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as GameActivityContent.PlayerContent
                //val kit = listFragment.intent?.getParcelableExtra(ItemDetailFragment.ARG_ITEM_KIT)
                //val intent = Intent(v.context, DetailPlayerActivity::class.java).apply {
                /*putExtra(ItemDetailFragment.ARG_ITEM_KIT, listFragment.kit)
                putExtra(ItemDetailFragment.ARG_HELMET, listFragment.helmet)
                putExtra(ItemDetailFragment.ARG_VEST, listFragment.vest)

                putExtra(ItemDetailFragment.ARG_GAME, listFragment.game)*/
                //}
                //v.context.startActivity(intent)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_gun_list, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.nameWeaponTextView.text = values[position].name
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val nameWeaponTextView : TextView = view.item_number
            val gunsRecyclerView : TextView = view.content
        }
    }
}
