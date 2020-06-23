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

class DetailPlayerFragment : Fragment(), WeaponListFragmentDialog.OnWeaponListFragmentInteraction {

    private var interaction: OnDetailPlayerFragmentInteraction? = null

    private var dialog : WeaponListFragmentDialog? = null
    private var mainDialog = false
    private var secondaryDialog = false

    private var player: Player? = null
    private val playerViewModel: PlayerViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        interaction = try {
            context as OnDetailPlayerFragmentInteraction
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnDetailPlayerFragmentInteraction")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButtons()

        updatePlayerView()
        // observe de viewmodel???
    }

    /*
    *
    * private fun updateView(){
        val adapterMainWeapons =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, player!!.mainWeapons!!)
        val adapterSecondaryWeapons =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, player!!.secondaryWeapons!!)

        adapterMainWeapons.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapterSecondaryWeapons.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerMainWeapons!!.adapter = adapterMainWeapons
        spinnerSecondaryWeapons!!.adapter = adapterSecondaryWeapons

        for (i in 0 .. player!!.mainWeapons!!.size) {
            if(player?.mainWeapons!!.isNotEmpty() &&
                player!!.mainWeapons!![i] == player!!.mainWeaponInGame) {
                spinnerMainWeapons!!.setSelection(i)
                break
            }
        }

        for (i in 0 .. player!!.secondaryWeapons!!.size) {
            if(player?.secondaryWeapons!!.isNotEmpty() &&
                player!!.secondaryWeapons!![i] == player!!.secondaryWeaponInGame){
                spinnerSecondaryWeapons!!.setSelection(i)
                break
            }
        }

        editTextMainCasualties!!.setText(
            if (player!!.mainWeaponInGame == null) "0" else player!!.mainWeaponInGame?.casualty.toString()
        )
        editTextSecondaryCasualties!!.setText(
            if (player!!.secondaryWeaponInGame == null) "0"
            else player!!.secondaryWeaponInGame?.casualty.toString()
        )
    }
    *
    * */

    fun updatePlayerView(){
        player = playerViewModel.getPlayer()?.value
        if (player != null){
            val mainWeaponsAdapters = activity?.let {
                ArrayAdapter(it, android.R.layout.simple_spinner_item, player!!.mainWeapons!!)
            }
            val secondaryWeaponsAdapters = activity?.let {
                ArrayAdapter(it, android.R.layout.simple_spinner_item, player!!.secondaryWeapons!!)
            }

            mainWeaponsAdapters?.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item)
            secondaryWeaponsAdapters?.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item)

            spinner_main_weapons!!.adapter = mainWeaponsAdapters
            spinner_secondary_weapons!!.adapter = secondaryWeaponsAdapters

            for (i in 0 .. player!!.mainWeapons!!.size) {
                if(player?.mainWeapons!!.isNotEmpty() &&
                    player!!.mainWeapons!![i] == player!!.mainWeaponInGame) {
                    spinner_main_weapons!!.setSelection(i)
                    break
                }
            }

            for (i in 0 .. player!!.secondaryWeapons!!.size) {
                if(player?.secondaryWeapons!!.isNotEmpty() &&
                    player!!.secondaryWeapons!![i] == player!!.secondaryWeaponInGame){
                    spinner_secondary_weapons!!.setSelection(i)
                    break
                }
            }

            editText_main_casualties!!.setText(
                if (player!!.mainWeaponInGame == null) ""
                else player!!.mainWeaponInGame?.casualty.toString()
            )
            editText_secondary_casualties!!.setText(
                if (player!!.secondaryWeaponInGame == null) ""
                else player!!.secondaryWeaponInGame?.casualty.toString()
            )

            switch_vest.isChecked = player!!.vest != null
            switch_helmet.isChecked = player!!.helmet != null
            switch_defuse_kit.isChecked = player!!.defuseKit != null
        }
    }

    private fun initButtons() {
        imageButton_add_main_casualty?.setOnClickListener {
            val bundle = Bundle()
            /*bundle.putSerializable(
                ARG_WEAPONS, mainWeapons as EnumMap<*, *>
            )*/
            mainDialog = true
            openWeaponDialog(bundle)
        }

        imageButton_add_secondary_weapon?.setOnClickListener {
            val bundle = Bundle()
            /*bundle.putSerializable(
                ARG_WEAPONS,
                secondaryWeapons as EnumMap<*, *>
            )*/
            secondaryDialog = true
            openWeaponDialog(bundle)
        }

        imageButton_add_main_casualty?.setOnClickListener {
            if(player?.mainWeaponInGame != null && player?.mainWeaponInGame?.casualty!! < 5){
                player?.mainWeaponInGame?.casualty!!.inc()
                editText_main_casualties.setText(player?.mainWeaponInGame?.casualty.toString())
            }
        }

        imageButton_delete_main_weapon?.setOnClickListener {
            if(player?.mainWeaponInGame != null && player?.mainWeaponInGame?.casualty!! > 0){
                player?.mainWeaponInGame?.casualty!!.dec()
                editText_main_casualties.setText(player?.mainWeaponInGame?.casualty.toString())
            }
        }

        imageButton_add_secondary_casualty?.setOnClickListener {
            if(player?.secondaryWeaponInGame != null && player?.secondaryWeaponInGame?.casualty!! < 5){
                player?.secondaryWeaponInGame?.casualty = player?.secondaryWeaponInGame?.casualty!!.inc()
                editText_secondary_casualties.setText(
                    player?.secondaryWeaponInGame?.casualty.toString())
            }
        }

        imageButton_delete_secondary_weapon?.setOnClickListener {
            if(player?.secondaryWeaponInGame != null && player?.secondaryWeaponInGame?.casualty!! > 0){
                player?.secondaryWeaponInGame?.casualty = player?.secondaryWeaponInGame?.casualty!!.dec()
                editText_secondary_casualties.setText(
                    player?.secondaryWeaponInGame?.casualty.toString())
            }
        }
    }

    private fun openWeaponDialog(bundle: Bundle) {
        dialog = WeaponListFragmentDialog(this)
        dialog!!.arguments = bundle
        dialog!!.show(requireActivity().supportFragmentManager, null)
    }

    companion object {
        const val ARG_WEAPONS = "weapons"
        const val ARG_PLAYER = "ARG_PLAYER"
    }

    interface OnDetailPlayerFragmentInteraction {
        fun prueba()
    }

    override fun selectWeapon(view: View, category: EquipmentCategoryEnum, position: Int) {
        TODO("Not yet implemented")
    }
}
