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
import androidx.lifecycle.observe
import es.ulpgc.tfm.ecocsgo.R
import es.ulpgc.tfm.ecocsgo.db.AppDatabase
import es.ulpgc.tfm.ecocsgo.db.AppHelperDB
import es.ulpgc.tfm.ecocsgo.model.*
import es.ulpgc.tfm.ecocsgo.viewmodel.PlayerViewModel
import kotlinx.android.synthetic.main.fragment_detail_player.*
import java.util.*


class DetailPlayerFragment : Fragment(){

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

        initButton()

        initSpinner()

        initSwitch()

        setUtilityLabelNames()

        //updatePlayerView()

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        playerViewModel.getPlayer()?.observe(viewLifecycleOwner) { player ->
            updatePlayerView()
        }
    }

    override fun onDetach() {
        interaction = null
        super.onDetach()
    }

    fun updatePlayerView(){
        val mainAdapter = activity?.let {
            ArrayAdapter(it, android.R.layout.simple_spinner_item,
                playerViewModel.getPlayer()!!.value!!.mainWeapons!!)
        }
        val secondaryAdapter = activity?.let {
            ArrayAdapter(it, android.R.layout.simple_spinner_item,
                playerViewModel.getPlayer()!!.value!!.secondaryWeapons!!)
        }

        mainAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        secondaryAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner_main_weapons!!.adapter = mainAdapter
        spinner_secondary_weapons!!.adapter = secondaryAdapter

        updateWeaponsView()

        switch_vest.isChecked = playerViewModel.getPlayer()!!.value!!.vest != null
        switch_helmet.isChecked = playerViewModel.getPlayer()!!.value!!.helmet != null
        switch_defuse_kit.isChecked = playerViewModel.getPlayer()!!.value!!.defuseKit != null
    }

    fun addMainWeapon(weapon: MainWeapon){
        playerViewModel.getPlayer()!!.value?.registerMainWeapon(weapon)
    }

    fun addSecondaryWeapon(weapon: SecondaryWeapon){
        playerViewModel.getPlayer()!!.value?.registerSecondaryWeapon(weapon)
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

    private fun updateWeaponsView() {
        val player = playerViewModel.getPlayer()!!.value

        for (i in 0 until player!!.mainWeapons!!.size) {
            if (player.mainWeapons!!.isNotEmpty() &&
                player.mainWeapons!![i] == player.getMainWeaponInGame()
            ) {
                spinner_main_weapons!!.setSelection(i)
                editText_main_casualties!!.setText(
                    player.getMainWeaponInGame()?.casualty.toString())
                break
            }
        }

        for (i in 0 until player.secondaryWeapons!!.size) {
            if (player.secondaryWeapons!!.isNotEmpty() &&
                player.secondaryWeapons!![i] == player.getSecondaryWeaponInGame()
            ) {
                spinner_secondary_weapons!!.setSelection(i)
                editText_secondary_casualties!!.setText(
                    player.getSecondaryWeaponInGame()?.casualty.toString())
                break
            }
        }
    }

    private fun initButton() {
        imageButton_add_main_weapon?.setOnClickListener {
            interaction?.openMainWeaponsDialog()
        }
        imageButton_add_secondary_weapon?.setOnClickListener {
            interaction?.openSecondaryWeaponsDialog()
        }
        imageButton_delete_main_weapon?.setOnClickListener {
            editText_main_casualties.setText("")

            deleteWeapon(playerViewModel.getPlayer()?.value?.mainWeapons!!,
                playerViewModel.getPlayer()?.value?.getMainWeaponInGame()!!)

            if (playerViewModel.getPlayer()?.value?.mainWeapons!!.isNotEmpty()){
                playerViewModel.getPlayer()?.value?.updateSelectedWeapon(
                    playerViewModel.getPlayer()?.value?.mainWeapons!!.first())
            }

            updateWeaponsView()
        }
        imageButton_delete_secondary_weapon?.setOnClickListener {
            editText_secondary_casualties.setText("")

            deleteWeapon(playerViewModel.getPlayer()?.value?.secondaryWeapons!!,
                playerViewModel.getPlayer()?.value?.getSecondaryWeaponInGame()!!)

            if (playerViewModel.getPlayer()?.value?.secondaryWeapons!!.isNotEmpty()){
                playerViewModel.getPlayer()?.value?.updateSelectedWeapon(
                    playerViewModel.getPlayer()?.value?.secondaryWeapons!!.first())
            }

            updateWeaponsView()
        }
        imageButton_add_main_casualty?.setOnClickListener {
            val weapon = playerViewModel.getPlayer()?.value?.getMainWeaponInGame()
            if(weapon != null && weapon.casualty < 5){
                weapon.casualty = weapon.casualty.inc()
                editText_main_casualties.setText(weapon.casualty.toString())
            }
        }
        imageButton_remove_main_casualty?.setOnClickListener {
            val weapon = playerViewModel.getPlayer()?.value?.getMainWeaponInGame()
            if(weapon != null && weapon.casualty > 0){
                weapon.casualty = weapon.casualty.dec()
                editText_main_casualties.setText(weapon.casualty.toString())
            }
        }
        imageButton_add_secondary_casualty?.setOnClickListener {
            val weapon =
                playerViewModel.getPlayer()?.value?.getSecondaryWeaponInGame()
            if(weapon != null && weapon.casualty < 5){
                weapon.casualty = weapon.casualty.inc()
                editText_secondary_casualties.setText(weapon.casualty.toString())
            }
        }
        imageButton_remove_secondary_casualty?.setOnClickListener {
            val weapon =
                playerViewModel.getPlayer()?.value?.getSecondaryWeaponInGame()
            if(weapon != null && weapon.casualty > 0){
                weapon.casualty = weapon.casualty.dec()
                editText_secondary_casualties.setText(weapon.casualty.toString())
            }
        }
    }

    private fun initSpinner(){
        spinner_main_weapons.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long
            ) {
                val weapon: MainWeapon =
                    playerViewModel.getPlayer()!!.value?.mainWeapons?.get(position)!!
                playerViewModel.getPlayer()?.value?.updateSelectedWeapon(weapon)
                editText_main_casualties.setText(
                    playerViewModel.getPlayer()?.value?.getMainWeaponInGame()!!.casualty.toString()
                )
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
        spinner_secondary_weapons.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View, position: Int, id: Long
            ) {
                val weapon : SecondaryWeapon =
                    playerViewModel.getPlayer()?.value?.secondaryWeapons?.get(position)!!
                playerViewModel.getPlayer()?.value?.updateSelectedWeapon(weapon)
                editText_secondary_casualties.setText(
                    playerViewModel.getPlayer()?.value?.
                    getSecondaryWeaponInGame()?.casualty.toString()
                )
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }
    }

    private fun initSwitch(){
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

    private fun deleteWeapon(weapons: List<Weapon>, weaponInGame: Weapon){
        for (weapon in weapons as ArrayList) {
            if (weaponInGame.name == weapon.name) {
                weapons.remove(weapon)
                val adapter: SpinnerAdapter? =
                    if (weapon.numeration.category == EquipmentCategoryEnum.PISTOL)
                        spinner_secondary_weapons!!.adapter
                    else
                        spinner_main_weapons!!.adapter
                val arrayAdapter = adapter as ArrayAdapter<*>
                arrayAdapter.notifyDataSetChanged()
                break
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
