package es.ulpgc.tfm.ecocsgo.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import es.ulpgc.tfm.ecocsgo.R
import es.ulpgc.tfm.ecocsgo.model.*
import es.ulpgc.tfm.ecocsgo.viewmodel.PlayerViewModel
import kotlinx.android.synthetic.main.fragment_detail_player.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [GameActivity]
 * in two-pane mode (on tablets) or a [DetailPlayerActivity]
 * on handsets.
 */
class DetailPlayerFragment : Fragment(), GunListFragmentDialog.OnGunListFragmentInteraction {

    private var button: Button? = null
    private var interaction: OnDetailPlayerFragmentInteraction? = null

    private var spinnerMainGuns: Spinner? = null
    private var spinnerSecondaryGuns: Spinner? = null

    private var textViewVest: TextView? = null
    private var textViewHelmet: TextView? = null
    private var textViewDefuseKit: TextView? = null

    private var editTextMainCasualties: EditText? = null
    private var editTextSecondaryCasualties: EditText? = null

    private var buttonMainGuns: ImageButton? = null
    private var buttonSecondaryGuns: ImageButton? = null

    private var buttonDeleteMainGun: ImageButton? = null
    private var buttonDeleteSecondaryGun: ImageButton? = null

    private var buttonAddMainCasualty: ImageButton? = null
    private var buttonRemoveMainCasualty: ImageButton? = null

    private var buttonAddSecondaryCasualty: ImageButton? = null
    private var buttonRemoveSecondaryCasualty: ImageButton? = null

    private var dialog : GunListFragmentDialog? = null
    private var mainDialog = false
    private var secondaryDialog = false

    private var player: Player? = null
    private var pistolGuns: ArrayList<SecondaryWeapon>? = null
    private var heavyGuns: ArrayList<MainWeapon>? = null
    private var smgGuns: ArrayList<MainWeapon>? = null
    private var rifleGuns: ArrayList<MainWeapon>? = null
    private var defuseKit: DefuseKit? = null
    private var helmet: Helmet? = null
    private var vest: Vest? = null

    private var mainGuns: MutableMap<EquipmentCategoryEnum, ArrayList<MainWeapon>?> =
        EnumMap(EquipmentCategoryEnum::class.java)
    private var secondaryGuns: MutableMap<EquipmentCategoryEnum, ArrayList<SecondaryWeapon>?> =
        EnumMap(EquipmentCategoryEnum::class.java)

    private val playerViewModel: PlayerViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        interaction = try {
            context as OnDetailPlayerFragmentInteraction
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnDetailPlayerFragmentInteraction")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareScreen()

        updatePlayerView()
        // observe de viewmodel???
    }

    fun updatePlayerView(){
        player = playerViewModel.getPlayer()?.value
        if (player != null){
            editText_secondary_casualties.setText(player!!.secondaryGunInGame?.casualty.toString())
        }
    }

    private fun prepareScreen() {

        initViews()

        //updateView(view)
/*
        textViewVest?.text = defuseKit?.name
        textViewHelmet?.text = helmet?.name
        textViewDefuseKit?.text = vest?.name*/
    }

    private fun initViews() {
        initButtons()
    }

    private fun updateView(view: View){
        val adapterMainGuns =
            ArrayAdapter(view.context, android.R.layout.simple_spinner_item, player!!.mainGuns!!)
        val adapterSecondaryGuns =
            ArrayAdapter(view.context, android.R.layout.simple_spinner_item, player!!.secondaryGuns!!)

        adapterMainGuns.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapterSecondaryGuns.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerMainGuns!!.adapter = adapterMainGuns
        spinnerSecondaryGuns!!.adapter = adapterSecondaryGuns

        for (i in 0 .. player!!.mainGuns!!.size) {
            if(player?.mainGuns!!.isNotEmpty() &&
                player!!.mainGuns!![i] == player!!.mainGunInGame) {
                spinnerMainGuns!!.setSelection(i)
                break
            }
        }

        for (i in 0 .. player!!.secondaryGuns!!.size) {
            if(player?.secondaryGuns!!.isNotEmpty() &&
                player!!.secondaryGuns!![i] == player!!.secondaryGunInGame){
                spinnerSecondaryGuns!!.setSelection(i)
                break
            }
        }

        editTextMainCasualties!!.setText(
            if (player!!.mainGunInGame == null) "0" else player!!.mainGunInGame?.casualty.toString()
        )
        editTextSecondaryCasualties!!.setText(
            if (player!!.secondaryGunInGame == null) "0"
            else player!!.secondaryGunInGame?.casualty.toString()
        )
    }

    private fun initButtons() {
        buttonMainGuns?.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(
                DetailPlayerFragment.ARG_GUNS,
                mainGuns as EnumMap<*, *>
            )
            mainDialog = true
            openGunDialog(it, bundle)
        }

        buttonSecondaryGuns?.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(
                DetailPlayerFragment.ARG_GUNS,
                secondaryGuns as EnumMap<*, *>
            )
            secondaryDialog = true
            openGunDialog(it, bundle)
        }

        buttonAddMainCasualty?.setOnClickListener {
            if(player?.mainGunInGame != null && player?.mainGunInGame?.casualty!! < 5){
                player?.mainGunInGame?.casualty!!.inc()
                editText_main_casualties.setText(player?.mainGunInGame?.casualty.toString())
            }
        }

        buttonRemoveMainCasualty?.setOnClickListener {
            if(player?.mainGunInGame != null && player?.mainGunInGame?.casualty!! > 0){
                player?.mainGunInGame?.casualty!!.dec()
                editText_main_casualties.setText(player?.mainGunInGame?.casualty.toString())
            }
        }

        buttonAddSecondaryCasualty?.setOnClickListener {
            if(player?.secondaryGunInGame != null && player?.secondaryGunInGame?.casualty!! < 5){
                player?.secondaryGunInGame?.casualty = player?.secondaryGunInGame?.casualty!!.inc()
                editText_secondary_casualties.setText(
                    player?.secondaryGunInGame?.casualty.toString())
            }
        }

        buttonRemoveSecondaryCasualty?.setOnClickListener {
            if(player?.secondaryGunInGame != null && player?.secondaryGunInGame?.casualty!! > 0){
                player?.secondaryGunInGame?.casualty = player?.secondaryGunInGame?.casualty!!.dec()
                editText_secondary_casualties.setText(
                    player?.secondaryGunInGame?.casualty.toString())
            }
        }
    }

    private fun openGunDialog(view: View, bundle: Bundle) {
        dialog = GunListFragmentDialog(this)
        dialog!!.arguments = bundle
        dialog!!.show(requireActivity().supportFragmentManager, null)
    }

    companion object {
        const val ARG_DEFUSE_KIT = "ARG_DEFUSE_KIT"
        const val ARG_HELMET = "ARG_HELMET"
        const val ARG_VEST = "ARG_VEST"
        const val ARG_GUNS = "guns"
        const val ARG_PISTOL = "ARG_PISTOL"
        const val ARG_HEAVY = "ARG_HEAVY"
        const val ARG_SMG = "ARG_SMG"
        const val ARG_RIFLE = "ARG_RIFLE"
        const val ARG_PLAYER = "ARG_PLAYER"
    }

    interface OnDetailPlayerFragmentInteraction {
        fun prueba()
    }

    override fun selectGun(view: View, category: EquipmentCategoryEnum, position: Int) {
        TODO("Not yet implemented")
    }
}
