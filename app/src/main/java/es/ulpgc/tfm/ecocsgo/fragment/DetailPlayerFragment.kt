package es.ulpgc.tfm.ecocsgo.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import es.ulpgc.tfm.ecocsgo.R
import es.ulpgc.tfm.ecocsgo.db.AppDatabase
import es.ulpgc.tfm.ecocsgo.db.AppHelperDB
import es.ulpgc.tfm.ecocsgo.model.*
import es.ulpgc.tfm.ecocsgo.viewmodel.PlayerViewModel
import kotlinx.android.synthetic.main.fragment_detail_player.*
import java.util.*

class DetailPlayerFragment : Fragment() {

    private var interaction: OnDetailPlayerFragmentInteraction? = null

    private var vest: Vest? = null
    private var helmet: Helmet? = null
    private var defuseKit: DefuseKit? = null

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

        imageButton_add_main_weapon?.setOnClickListener {
            if (playerViewModel.getPlayer()?.value != null)
                interaction?.openMainWeaponsDialog()
        }
        imageButton_add_secondary_weapon?.setOnClickListener {
            if (playerViewModel.getPlayer()?.value != null)
                interaction?.openSecondaryWeaponsDialog()
        }
        imageButton_delete_main_weapon?.setOnClickListener {
            if (playerViewModel.getPlayer()?.value != null) interaction?.deleteWeapon(
                playerViewModel.getPlayer()?.value?.getMainWeaponInGame()!!
            )
        }
        imageButton_delete_secondary_weapon?.setOnClickListener {
            if (playerViewModel.getPlayer()?.value != null) interaction?.deleteWeapon(
                playerViewModel.getPlayer()?.value?.getSecondaryWeaponInGame()!!
            )
        }
        imageButton_add_main_casualty?.setOnClickListener {
            if (playerViewModel.getPlayer()?.value != null) interaction?.addCasualty(
                playerViewModel.getPlayer()?.value?.getMainWeaponInGame()!!
            )
        }
        imageButton_add?.setOnClickListener {
            if (playerViewModel.getPlayer()?.value != null) interaction?.addCasualty(
                playerViewModel.getPlayer()?.value?.getSecondaryWeaponInGame()!!
            )
        }
        imageButton_remove_main_casualty?.setOnClickListener {
            if (playerViewModel.getPlayer()?.value != null) interaction?.removeCasualty(
                playerViewModel.getPlayer()?.value?.getMainWeaponInGame()!!
            )
        }
        imageButton_remove?.setOnClickListener {
            if (playerViewModel.getPlayer()?.value != null) interaction?.removeCasualty(
                playerViewModel.getPlayer()?.value?.getSecondaryWeaponInGame()!!
            )
        }
        spinner_main_weapons.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long
            ) {
                if (playerViewModel.getPlayer()?.value != null) interaction?.selectWeaponInGame(
                    playerViewModel.getPlayer()!!.value?.mainWeapons?.get(position)!!
                )
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
        spinner_secondary_weapons.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long
            ) {
                if (playerViewModel.getPlayer()?.value != null) interaction?.selectWeaponInGame(
                    playerViewModel.getPlayer()?.value?.secondaryWeapons?.get(position)!!
                )
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }

        with(toggleButton_main_origin) {
            setOnCheckedChangeListener { _, isChecked ->
                if (playerViewModel.getPlayer()?.value != null &&
                    playerViewModel.getPlayer()?.value?.getMainWeaponInGame() != null
                )
                    playerViewModel.getPlayer()?.value?.getMainWeaponInGame()?.origin =
                        if (isChecked) OriginEquipmentEnum.PURCHASED
                        else OriginEquipmentEnum.NO_PURCHASED
            }
            isEnabled = playerViewModel.getPlayer()?.value?.getMainWeaponInGame() != null
        }

        with(toggleButton_secondary_origin) {
            setOnCheckedChangeListener { _, isChecked ->
                if (playerViewModel.getPlayer()?.value != null &&
                    playerViewModel.getPlayer()?.value?.getSecondaryWeaponInGame() != null
                )
                    playerViewModel.getPlayer()?.value?.getSecondaryWeaponInGame()?.origin =
                        if (isChecked) OriginEquipmentEnum.PURCHASED
                        else OriginEquipmentEnum.NO_PURCHASED
            }
            isEnabled = playerViewModel.getPlayer()?.value?.getSecondaryWeaponInGame() != null
        }

        initUtilityView()

        playerViewModel.getPlayer()?.observe(viewLifecycleOwner) { player ->
            updatePlayerView(player)
        }
    }

    override fun onDetach() {
        interaction = null
        super.onDetach()
    }

    fun updatePlayerView(player: Player) {
        val mainAdapter = activity?.let {
            ArrayAdapter(it, android.R.layout.simple_spinner_item, player.mainWeapons!!)
        }
        val secondaryAdapter = activity?.let {
            ArrayAdapter(it, android.R.layout.simple_spinner_item, player.secondaryWeapons!!)
        }

        mainAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        secondaryAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner_main_weapons!!.adapter = mainAdapter
        spinner_secondary_weapons!!.adapter = secondaryAdapter

        updateWeaponsView(player)

        switch_vest.isChecked = player.vest != null
        switch_helmet.isChecked = player.helmet != null
        switch_defuse_kit.isChecked = player.defuseKit != null
    }

    fun addMainWeapon(weapon: MainWeapon) {
        playerViewModel.getPlayer()!!.value?.registerMainWeapon(weapon)
    }

    fun addSecondaryWeapon(weapon: SecondaryWeapon) {
        playerViewModel.getPlayer()!!.value?.registerSecondaryWeapon(weapon)
    }

    fun retrieveMainWeapons(): EnumMap<EquipmentCategoryEnum, List<MainWeapon>> {
        val appDatabase = AppDatabase(activity)
        val appHelperDB = AppHelperDB(appDatabase)

        val mainWeapons: EnumMap<EquipmentCategoryEnum, List<MainWeapon>>

        appHelperDB.open()
        mainWeapons = appHelperDB.fetchMainWeapons()

        return mainWeapons
    }

    fun retrieveSecondaryWeapons(): EnumMap<EquipmentCategoryEnum, List<SecondaryWeapon>> {
        val appDatabase = AppDatabase(activity)
        val appHelperDB = AppHelperDB(appDatabase)

        val secondaryWeapons: EnumMap<EquipmentCategoryEnum, List<SecondaryWeapon>>

        appHelperDB.open()
        secondaryWeapons = appHelperDB.fetchSecondaryWeapons()

        return secondaryWeapons
    }

    fun deleteWeapon(weaponInGame: Weapon) {
        val adapter: SpinnerAdapter? =
            if (weaponInGame.numeration.category == EquipmentCategoryEnum.PISTOL) {
                playerViewModel.getPlayer()?.value?.removeSecondaryWeapon(weaponInGame)
                spinner_secondary_weapons!!.adapter
            } else {
                playerViewModel.getPlayer()?.value?.removeMainWeapon(weaponInGame)
                spinner_main_weapons!!.adapter
            }

        val arrayAdapter = adapter as ArrayAdapter<*>
        arrayAdapter.notifyDataSetChanged()

        updateWeaponsView(playerViewModel.getPlayer()?.value!!)
    }

    fun addCasualty(weapon: Weapon) {
        if (weapon.casualty < 5) {
            weapon.casualty = weapon.casualty.inc()

            if (weapon.numeration.category == EquipmentCategoryEnum.PISTOL)
                editText_value.setText(weapon.casualty.toString())
            else
                editText_main_casualties.setText(weapon.casualty.toString())
        }
    }

    fun removeCasualty(weapon: Weapon) {
        if (weapon.casualty > 0) {
            weapon.casualty = weapon.casualty.dec()

            if (weapon.numeration.category == EquipmentCategoryEnum.PISTOL)
                editText_value.setText(weapon.casualty.toString())
            else
                editText_main_casualties.setText(weapon.casualty.toString())
        }
    }

    fun selectWeaponInGame(weapon: Weapon) {
        if (weapon.numeration.category == EquipmentCategoryEnum.PISTOL) {
            playerViewModel.getPlayer()?.value?.updateSelectedWeapon(weapon)
            editText_value.setText(
                playerViewModel.getPlayer()?.value?.getSecondaryWeaponInGame()?.casualty.toString()
            )
        } else {
            playerViewModel.getPlayer()?.value?.updateSelectedWeapon(weapon)
            editText_main_casualties.setText(
                playerViewModel.getPlayer()?.value?.getMainWeaponInGame()!!.casualty.toString()
            )
        }
    }

    private fun updateWeaponsView(player: Player) {
        editText_main_casualties.setText("")
        toggleButton_main_origin.isChecked = false
        toggleButton_main_origin.isEnabled = false
        for (i in 0 until player.mainWeapons!!.size) {
            if (player.mainWeapons!!.isNotEmpty() &&
                player.mainWeapons!![i] == player.getMainWeaponInGame()
            ) {
                spinner_main_weapons!!.setSelection(i)
                toggleButton_main_origin.isEnabled = true
                toggleButton_main_origin.isChecked =
                    player.getMainWeaponInGame()?.origin == OriginEquipmentEnum.PURCHASED
                editText_main_casualties!!.setText(
                    player.getMainWeaponInGame()?.casualty.toString()
                )
                break
            }
        }

        editText_value!!.setText("")
        toggleButton_secondary_origin.isChecked = false
        toggleButton_secondary_origin.isEnabled = false
        for (i in 0 until player.secondaryWeapons!!.size) {
            if (player.secondaryWeapons!!.isNotEmpty() &&
                player.secondaryWeapons!![i] == player.getSecondaryWeaponInGame()
            ) {
                spinner_secondary_weapons!!.setSelection(i)
                toggleButton_secondary_origin.isEnabled = true
                toggleButton_secondary_origin.isChecked =
                    player.getSecondaryWeaponInGame()?.origin == OriginEquipmentEnum.PURCHASED
                editText_value!!.setText(
                    player.getSecondaryWeaponInGame()?.casualty.toString()
                )
                break
            }
        }
    }

    private fun initUtilityView() {
        val appDatabase = AppDatabase(context)
        val appHelperDB = AppHelperDB(appDatabase)

        val utilities: List<Equipment>

        appHelperDB.open()
        utilities = appHelperDB.fetchUtilityEquipment()

        for (utility in utilities) {
            when (utility.numeration.item) {
                1 -> vest = utility as Vest
                2 -> helmet = utility as Helmet
                4 -> defuseKit = utility as DefuseKit
            }
        }

        textView_vest.text = vest?.name
        textView_helmet.text = helmet?.name
        textView_defuse_kit.text = defuseKit?.name

        switch_vest.setOnCheckedChangeListener { _, isChecked ->
            if (playerViewModel.getPlayer()?.value == null) return@setOnCheckedChangeListener
            playerViewModel.getPlayer()?.value?.vest = if (isChecked) vest?.copy() else null
        }
        switch_helmet.setOnCheckedChangeListener { _, isChecked ->
            if (playerViewModel.getPlayer()?.value == null) return@setOnCheckedChangeListener
            playerViewModel.getPlayer()?.value?.helmet = if (isChecked) helmet?.copy() else null
        }
        switch_defuse_kit.setOnCheckedChangeListener { _, isChecked ->
            if (playerViewModel.getPlayer()?.value == null) return@setOnCheckedChangeListener
            playerViewModel.getPlayer()?.value?.defuseKit =
                if (isChecked) defuseKit?.copy() else null
        }
    }

    interface OnDetailPlayerFragmentInteraction {
        fun openMainWeaponsDialog()
        fun openSecondaryWeaponsDialog()
        fun deleteWeapon(weapon: Weapon)
        fun addCasualty(weapon: Weapon)
        fun removeCasualty(weapon: Weapon)
        fun selectWeaponInGame(weapon: Weapon)
    }

}
