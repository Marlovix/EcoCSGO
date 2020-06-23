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

class WeaponListFragmentDialog(
    private var interaction: OnWeaponListFragmentInteraction?
) : DialogFragment() {

    private var weapon : Map<EquipmentCategoryEnum, List<Equipment>> =
        EnumMap(EquipmentCategoryEnum::class.java)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnWeaponListFragmentInteraction) {
            interaction = context
        } else {
            throw RuntimeException("$context must implement OnWeaponListFragmentInteraction")
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.list_category_weapon, container, false)

        val recyclerView = rootView.findViewById(R.id.list_category_weapon) as RecyclerView

        val bundle = arguments
        weapon = bundle?.getSerializable(DetailPlayerFragment.ARG_WEAPONS) as Map<EquipmentCategoryEnum, ArrayList<Weapon>>

        if (rootView is RecyclerView) recyclerView.adapter = EquipmentCategoryRecyclerViewAdapter(weapon, interaction)

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

    interface OnWeaponListFragmentInteraction{
        fun selectWeapon(view: View, category: EquipmentCategoryEnum, position: Int)
    }

}
