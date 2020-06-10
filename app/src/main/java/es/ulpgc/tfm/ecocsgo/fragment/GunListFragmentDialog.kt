package es.ulpgc.tfm.ecocsgo.fragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import es.ulpgc.tfm.ecocsgo.R
import es.ulpgc.tfm.ecocsgo.adapter.EquipmentCategoryRecyclerViewAdapter
import es.ulpgc.tfm.ecocsgo.model.Equipment
import es.ulpgc.tfm.ecocsgo.model.EquipmentCategoryEnum
import es.ulpgc.tfm.ecocsgo.model.Weapon
import java.util.*

class GunListFragmentDialog(
    private var interaction: OnGunListFragmentInteraction?
) : DialogFragment() {

    private var guns : Map<EquipmentCategoryEnum, List<Equipment>> = EnumMap(EquipmentCategoryEnum::class.java)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnGunListFragmentInteraction) {
            interaction = context
        } else {
            throw RuntimeException("$context must implement OnGunListFragmentInteraction")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.list_category_gun, container, false)

        val recyclerView = rootView.findViewById(R.id.list_category_gun) as RecyclerView

        val bundle = arguments
        guns = bundle?.getSerializable(DetailPlayerFragment.ARG_GUNS) as Map<EquipmentCategoryEnum, ArrayList<Weapon>>

        if (rootView is RecyclerView) recyclerView.adapter = EquipmentCategoryRecyclerViewAdapter(guns, interaction)

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

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val activity = activity
        if (activity is DialogInterface.OnDismissListener) {
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)
        }
    }

    override fun onDetach() {
        interaction = null
        super.onDetach()
    }

    interface OnGunListFragmentInteraction{
        fun selectGun(view: View, category: EquipmentCategoryEnum, position: Int)
    }

}
