package es.ulpgc.tfm.ecocsgo.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import es.ulpgc.tfm.ecocsgo.R
import es.ulpgc.tfm.ecocsgo.db.AppDatabase
import es.ulpgc.tfm.ecocsgo.db.AppHelperDB
import es.ulpgc.tfm.ecocsgo.model.*
import es.ulpgc.tfm.ecocsgo.viewmodel.PlayerViewModel
import kotlinx.android.synthetic.main.fragment_detail_player.*
import java.util.*

class DetailPlayerFragment : Fragment(){

    private var interaction: OnDetailPlayerFragmentInteraction? = null

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

        setUtilityLabelNames()

        updatePlayerView()
        // observe de viewModel???
    }

    override fun onDetach() {
        interaction = null
        super.onDetach()
    }

    fun updatePlayerView(){
        player = playerViewModel.getPlayer()?.value
        if (player != null){
            val mainAdapter = activity?.let {
                ArrayAdapter(it, android.R.layout.simple_spinner_item, player!!.mainWeapons!!)
            }
            val secondaryAdapter = activity?.let {
                ArrayAdapter(it, android.R.layout.simple_spinner_item, player!!.secondaryWeapons!!)
            }

            mainAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            secondaryAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            spinner_main_weapons!!.adapter = mainAdapter
            spinner_secondary_weapons!!.adapter = secondaryAdapter

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

    fun retrieveMainWeapons() : EnumMap<EquipmentCategoryEnum, List<MainWeapon>> {
        val appDatabase = AppDatabase(activity)
        val appHelperDB = AppHelperDB(appDatabase)

        val mainWeapons: EnumMap<EquipmentCategoryEnum, List<MainWeapon>>

        appHelperDB.open()
        mainWeapons = appHelperDB.fetchMainWeapons()
        appHelperDB.close()

        return mainWeapons
    }

    fun retrieveSecondaryWeapons() : EnumMap<EquipmentCategoryEnum, List<SecondaryWeapon>> {
        val appDatabase = AppDatabase(activity)
        val appHelperDB = AppHelperDB(appDatabase)

        val secondaryWeapons: EnumMap<EquipmentCategoryEnum, List<SecondaryWeapon>>

        appHelperDB.open()
        secondaryWeapons = appHelperDB.fetchSecondaryWeapons()
        appHelperDB.close()

        return secondaryWeapons
    }

    private fun initButtons() {
        imageButton_add_main_weapon?.setOnClickListener {
            interaction?.openMainWeaponsDialog()
        }

        imageButton_add_secondary_weapon?.setOnClickListener {
            interaction?.openSecondaryWeaponsDialog()
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
            val weapon = player?.secondaryWeaponInGame
            if(weapon != null && weapon.casualty < 5){
                weapon.casualty = weapon.casualty.inc()
                editText_secondary_casualties.setText(weapon.casualty.toString())
            }
        }

        imageButton_delete_secondary_weapon?.setOnClickListener {
            val weapon = player?.secondaryWeaponInGame
            if(weapon != null && weapon.casualty > 0){
                weapon.casualty = weapon.casualty.dec()
                editText_secondary_casualties.setText(weapon.casualty.toString())
            }
        }
    }

    private fun setUtilityLabelNames(){
        val appDatabase = AppDatabase(context)
        val appHelperDB = AppHelperDB(appDatabase)

        val utilities: List<Equipment>

        appHelperDB.open()
        utilities = appHelperDB.fetchUtilityEquipment()
        appHelperDB.close()

        var vest: Vest? = null
        var helmet: Helmet? = null
        var defuseKit: DefuseKit? = null

        for (utility in utilities){
            when(utility.numeration.item){
                1 -> vest = utility as Vest
                2 -> helmet = utility as Helmet
                4 -> defuseKit = utility as DefuseKit
            }
        }

        textView_vest.text = vest?.name
        textView_helmet.text = helmet?.name
        textView_defuse_kit.text = defuseKit?.name
    }

    interface OnDetailPlayerFragmentInteraction {
        fun openMainWeaponsDialog()
        fun openSecondaryWeaponsDialog()
    }

}
