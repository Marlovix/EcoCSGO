package es.ulpgc.tfm.ecocsgo

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import es.ulpgc.tfm.ecocsgo.fragment.DetailPlayerFragment
import es.ulpgc.tfm.ecocsgo.fragment.WeaponListFragmentDialog
import es.ulpgc.tfm.ecocsgo.model.*
import es.ulpgc.tfm.ecocsgo.viewmodel.PlayerViewModel
import kotlinx.android.synthetic.main.activity_detail_player.*

class DetailPlayerActivity : AppCompatActivity(),
    DetailPlayerFragment.OnDetailPlayerFragmentInteraction, DialogInterface.OnDismissListener,
    WeaponListFragmentDialog.OnWeaponListFragmentInteraction {

    private var dialog: WeaponListFragmentDialog? = null
    private var mainDialog = false
    private var secondaryDialog = false

    private val playerViewModel: PlayerViewModel by viewModels()

    private var detailPlayerFragment: DetailPlayerFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_player)
        setSupportActionBar(toolbar)

        // add back arrow to toolbar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val player: Player? = intent.getParcelableExtra(ARG_PLAYER)
        playerViewModel.setPlayer(player!!)

        detailPlayerFragment = DetailPlayerFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_detail_player, detailPlayerFragment!!).commit()

        if(mainDialog){
            openMainWeaponsDialog()
        }else if(secondaryDialog){
            openSecondaryWeaponsDialog()
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        val intent = Intent()
        intent.putExtra(ARG_PLAYER, playerViewModel.getPlayer()?.value)
        setResult(GameActivity.ARG_RESPONSE_PLAYER, intent)
        finish() //finishing activity

        //onBackPressed()
        return true
    }

    override fun onDismiss(dialog: DialogInterface?) {
        mainDialog = false
        secondaryDialog = false
    }

    override fun openMainWeaponsDialog() {
        mainDialog = true
        val mainWeapons = detailPlayerFragment!!.retrieveMainWeapons()
        val mainWeaponInGame = playerViewModel.getPlayer()?.value?.getMainWeaponInGame()

        val bundle = Bundle()
        bundle.putSerializable(ARG_WEAPONS, mainWeapons)
        bundle.putString(ARG_WEAPON_IN_GAME, mainWeaponInGame?.name)
        openWeaponDialog(bundle)
    }

    override fun openSecondaryWeaponsDialog() {
        secondaryDialog = true
        val secondaryWeapons = detailPlayerFragment!!.retrieveSecondaryWeapons()
        val secondaryWeaponInGame = playerViewModel.getPlayer()?.value?.getSecondaryWeaponInGame()

        val bundle = Bundle()
        bundle.putSerializable(ARG_WEAPONS, secondaryWeapons)
        bundle.putString(ARG_WEAPON_IN_GAME, secondaryWeaponInGame?.name)
        openWeaponDialog(bundle)
    }

    override fun deleteWeapon(weapon: Weapon) {
        detailPlayerFragment?.deleteWeapon(weapon)
    }

    override fun addCasualty(weapon: Weapon) {
        detailPlayerFragment?.addCasualty(weapon)
    }

    override fun removeCasualty(weapon: Weapon) {
        detailPlayerFragment?.removeCasualty(weapon)
    }

    override fun selectWeaponInGame(weapon: Weapon) {
        detailPlayerFragment?.selectWeaponInGame(weapon)
    }

    override fun selectWeapon(weapon: Weapon) {
        if (!weapon.inGame) {
            if (weapon.numeration.category == EquipmentCategoryEnum.PISTOL)
                detailPlayerFragment?.addSecondaryWeapon(weapon as SecondaryWeapon)
            else
                detailPlayerFragment?.addMainWeapon(weapon as MainWeapon)

            detailPlayerFragment?.updatePlayerView(playerViewModel.getPlayer()?.value!!)
        }

        dialog?.dismiss()
    }

    private fun openWeaponDialog(bundle: Bundle) {
        dialog = WeaponListFragmentDialog(this)
        dialog!!.arguments = bundle
        dialog!!.show(supportFragmentManager, null)
    }

    companion object {
        const val ARG_WEAPONS = "weapons"
        const val ARG_WEAPON_IN_GAME = "weapon_in_game"
        const val ARG_PLAYER = "player"
    }

}
