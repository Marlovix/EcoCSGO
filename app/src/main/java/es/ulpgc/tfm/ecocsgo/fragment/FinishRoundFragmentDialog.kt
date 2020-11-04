package es.ulpgc.tfm.ecocsgo.fragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import es.ulpgc.tfm.ecocsgo.GameActivity
import es.ulpgc.tfm.ecocsgo.R
import es.ulpgc.tfm.ecocsgo.adapter.FinishRoundRecyclerViewAdapter
import es.ulpgc.tfm.ecocsgo.model.EquipmentTeamEnum
import es.ulpgc.tfm.ecocsgo.model.TypeFinalRoundEnum
import kotlinx.android.synthetic.main.finish_round.*

class FinishRoundFragmentDialog(
    private var interaction: OnFinishRoundFragmentInteraction?
) : DialogFragment() {

    private val adapter: FinishRoundRecyclerViewAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFinishRoundFragmentInteraction) {
            interaction = context
        } else {
            throw RuntimeException("$context must implement OnFinishRoundFragmentInteraction")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView =
            inflater.inflate(R.layout.finish_round, container, false)

        retainInstance = true

        val title = context?.resources?.getString(R.string.label_how_round_finish)

        val toolbar: Toolbar = rootView.findViewById(R.id.toolbar)
        val textView: TextView = rootView.findViewById(R.id.textView_finish_round_question)
        val buttonWin: TextView = rootView.findViewById(R.id.button_won)
        val buttonLose: TextView = rootView.findViewById(R.id.button_lost)

        toolbar.setNavigationIcon(android.R.drawable.ic_menu_close_clear_cancel)
        toolbar.setNavigationOnClickListener {
            dismiss()
        }

        // handle menu item click
        toolbar.setOnMenuItemClickListener { false }

        toolbar.title = title

        val bundle = arguments
        val team =
            bundle?.getParcelable<EquipmentTeamEnum>(GameActivity.ARG_TEAM_FINISH_ROUND)

        textView.text = team?.let { getTitle(it) }

        buttonWin.setOnClickListener {
            interaction?.win(team!!)
        }

        buttonLose.setOnClickListener {
            interaction?.lose(team!!)
        }

        return rootView
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width: Int = ViewGroup.LayoutParams.MATCH_PARENT
            val height: Int = resources.getDimension(R.dimen.height_without_option_dialog).toInt()
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

    fun setVictoryOptions(options: Map<TypeFinalRoundEnum, String>){
        val width: Int = ViewGroup.LayoutParams.MATCH_PARENT
        val height: Int = resources.getDimension(R.dimen.height_option_victory_dialog).toInt()
        dialog?.window?.setLayout(width, height)

        options_finish_round.adapter = interaction?.let {
            FinishRoundRecyclerViewAdapter(options, it, true)
        }
        adapter?.notifyDataSetChanged()
    }

    fun setDefeatOptions(options: Map<TypeFinalRoundEnum, String>){
        val width: Int = ViewGroup.LayoutParams.MATCH_PARENT
        val height: Int = resources.getDimension(R.dimen.height_option_defeat_dialog).toInt()
        dialog?.window?.setLayout(width, height)

        options_finish_round.adapter = interaction?.let {
            FinishRoundRecyclerViewAdapter(options, it, false)
        }
        adapter?.notifyDataSetChanged()
    }

    private fun getTitle(team: EquipmentTeamEnum): String? {
        if (team == EquipmentTeamEnum.T) {
            return context?.resources?.getString(R.string.label_terrorist_have) + ":"
        } else if (team == EquipmentTeamEnum.CT) {
            return context?.resources?.getString(R.string.label_counter_terrorist_have) + ":"
        }
        return ""
    }

    interface OnFinishRoundFragmentInteraction {
        fun win(team: EquipmentTeamEnum)
        fun lose(team: EquipmentTeamEnum)
        fun selectOption(option: TypeFinalRoundEnum?, isVictory: Boolean)
    }

}