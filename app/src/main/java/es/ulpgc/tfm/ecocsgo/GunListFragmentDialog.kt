package es.ulpgc.tfm.ecocsgo

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import es.ulpgc.tfm.ecocsgo.adapter.EquipmentCategoryRecyclerViewAdapter
import es.ulpgc.tfm.ecocsgo.model.EquipmentCategory
import es.ulpgc.tfm.ecocsgo.model.Gun
import java.util.*
import android.app.Activity
import es.ulpgc.tfm.ecocsgo.model.Equipment

@Suppress("UNCHECKED_CAST")
class GunListFragmentDialog(
    private var listener: GunClickListener
) : DialogFragment() {

    private var guns : Map<EquipmentCategory, List<Equipment>> = EnumMap(EquipmentCategory::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.category_gun_list, container, false)

        val recyclerView = rootView.findViewById(R.id.category_gun_list) as RecyclerView

        val bundle = arguments
        guns = bundle?.getSerializable(ItemDetailFragment.ARG_GUNS) as EnumMap<EquipmentCategory, List<Equipment>>

        if (rootView is RecyclerView) recyclerView.adapter = EquipmentCategoryRecyclerViewAdapter(guns, listener)

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is GunClickListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement GunClickListener")
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val activity = activity
        if (activity is DialogInterface.OnDismissListener) {
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)
        }
    }

    interface GunClickListener{
        fun selectGun(view: View, category: EquipmentCategory, position: Int)
    }

}
