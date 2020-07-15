package es.ulpgc.tfm.ecocsgo.fragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import es.ulpgc.tfm.ecocsgo.R
import es.ulpgc.tfm.ecocsgo.model.Weapon
import es.ulpgc.tfm.ecocsgo.viewmodel.GameActivityViewModel

class GrenadeFragmentDialog(
    private var interaction: OnGrenadeFormFragmentInteraction?
) : DialogFragment() {

    private val gameViewModel: GameActivityViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnGrenadeFormFragmentInteraction) {
            interaction = context
        } else {
            throw RuntimeException("$context must implement OnGrenadeFormFragmentInteraction")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =
            inflater.inflate(R.layout.list_category_weapon, container, false)

        return rootView
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width : Int = resources.getDimension(R.dimen.width_weapon_dialog).toInt()
            val height : Int = resources.getDimension(R.dimen.width_weapon_dialog).toInt()

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

    interface OnGrenadeFormFragmentInteraction{
        fun selectWeapon(weapon: Weapon)
    }

}