package es.ulpgc.tfm.ecocsgo

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import es.ulpgc.tfm.ecocsgo.fragment.DetailPlayerFragment
import es.ulpgc.tfm.ecocsgo.fragment.WeaponListFragmentDialog
import es.ulpgc.tfm.ecocsgo.model.*
import es.ulpgc.tfm.ecocsgo.viewmodel.PlayerViewModel
import kotlinx.android.synthetic.main.activity_detail_player.*
import kotlinx.android.synthetic.main.activity_game.*


class DetailPlayerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    DetailPlayerFragment.OnDetailPlayerFragmentInteraction, DialogInterface.OnDismissListener,
    WeaponListFragmentDialog.OnWeaponListFragmentInteraction{

    private var dialog : WeaponListFragmentDialog? = null
    private var mainDialog = false
    private var secondaryDialog = false

    private val playerViewModel: PlayerViewModel by viewModels()
    private var playerSelectedIndex : Int? = null

    private var detailPlayerFragment: DetailPlayerFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_player)
        setSupportActionBar(toolbar)

        // add back arrow to toolbar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        val player : Player? = intent.getParcelableExtra(ARG_PLAYER)
        playerSelectedIndex = intent.getIntExtra(ARG_PLAYER_INDEX, -1)
        playerViewModel.setPlayer(player!!)

        detailPlayerFragment = DetailPlayerFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_detail_player, detailPlayerFragment!!).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.detail_player_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {

        val intent = Intent()
        intent.putExtra(ARG_PLAYER, playerViewModel.getPlayer()?.value)
        intent.putExtra(ARG_PLAYER_INDEX, playerSelectedIndex)
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
        openGunDialog(bundle)
    }

    override fun openSecondaryWeaponsDialog() {
        secondaryDialog = true
        val secondaryWeapons = detailPlayerFragment!!.retrieveSecondaryWeapons()
        val secondaryWeaponInGame = playerViewModel.getPlayer()?.value?.getSecondaryWeaponInGame()

        val bundle = Bundle()
        bundle.putSerializable(ARG_WEAPONS, secondaryWeapons)
        bundle.putString(ARG_WEAPON_IN_GAME, secondaryWeaponInGame?.name)
        openGunDialog(bundle)
    }

    override fun selectWeapon(weapon: Weapon) {
        if (!weapon.inGame){
            if(weapon.numeration.category == EquipmentCategoryEnum.PISTOL)
                detailPlayerFragment?.addSecondaryWeapon(weapon as SecondaryWeapon)
            else
                detailPlayerFragment?.addMainWeapon(weapon as MainWeapon)

            detailPlayerFragment?.updatePlayerView()
        }

        dialog?.dismiss()
    }

    private fun openGunDialog(bundle: Bundle) {
        dialog = WeaponListFragmentDialog(this)
        dialog!!.arguments = bundle
        dialog!!.show(supportFragmentManager, null)
    }

    companion object {
        const val ARG_WEAPONS = "weapons"
        const val ARG_WEAPON_IN_GAME = "weapon_in_game"
        const val ARG_PLAYER = "player"
        const val ARG_PLAYER_INDEX = "player_selected"
    }

}
