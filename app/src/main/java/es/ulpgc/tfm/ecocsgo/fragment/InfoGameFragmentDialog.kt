package es.ulpgc.tfm.ecocsgo.fragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import es.ulpgc.tfm.ecocsgo.GameActivity
import es.ulpgc.tfm.ecocsgo.R
import es.ulpgc.tfm.ecocsgo.model.Game
import es.ulpgc.tfm.ecocsgo.viewmodel.GameActivityViewModel

class InfoGameFragmentDialog(
    private var interaction: OnFormInfoGameFragmentInteraction?
) : DialogFragment() {

    private val gameViewModel: GameActivityViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFormInfoGameFragmentInteraction) {
            interaction = context
        } else {
            throw RuntimeException("$context must implement OnFormInfoGameFragmentInteraction")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =
            inflater.inflate(R.layout.form_info_game, container, false)

        retainInstance = true

        val bundle = arguments
        val title = bundle?.getString(GameActivity.ARG_TITLE_INFO_GAME_DIALOG)
        var value = bundle?.getInt(GameActivity.ARG_VALUE_INFO_GAME_DIALOG)

        val toolbar: Toolbar = rootView.findViewById(R.id.toolbar)
        val editTextValue: EditText = rootView.findViewById(R.id.editText_value)
        val imageButtonRemove: ImageButton = rootView.findViewById(R.id.imageButton_remove)
        val imageButtonAdd: ImageButton = rootView.findViewById(R.id.imageButton_add)

        toolbar.setNavigationIcon(android.R.drawable.ic_menu_close_clear_cancel)
        toolbar.setNavigationOnClickListener {
            dismiss()
        }

        // handle menu item click
        toolbar.setOnMenuItemClickListener { false }

        toolbar.title = title

        editTextValue.setText(value!!.toString())

        imageButtonRemove.setOnClickListener {
            if (value > 0) {
                interaction?.remove(title!!)
                value -= 1
                editTextValue.setText(value.toString())
            }
        }

        imageButtonAdd.setOnClickListener {
            if (value < Game.ENEMIES) {
                interaction?.add(title!!)
                value += 1
                editTextValue.setText(value.toString())
            }
        }

        return rootView
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width: Int = ViewGroup.LayoutParams.MATCH_PARENT
            val height: Int = resources.getDimension(R.dimen.width_info_game_dialog).toInt()

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

    override fun onDestroyView() {
        if (dialog != null && retainInstance) {
            dialog!!.setDismissMessage(null)
        }
        super.onDestroyView()
    }

    override fun onDetach() {
        interaction = null
        super.onDetach()
    }

    interface OnFormInfoGameFragmentInteraction {
        fun add(title: String)
        fun remove(title: String)
    }

}