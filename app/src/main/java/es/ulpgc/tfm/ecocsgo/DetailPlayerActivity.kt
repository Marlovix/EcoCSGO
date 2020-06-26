package es.ulpgc.tfm.ecocsgo

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
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

    private var player: Player? = null
    private val playerViewModel: PlayerViewModel by viewModels()

    private var detailPlayerFragment: DetailPlayerFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_player)
        setSupportActionBar(toolbar)

        // add back arrow to toolbar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        player = intent.getParcelableExtra(ARG_PLAYER)
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
        onBackPressed()
        return true
    }

    override fun onDismiss(dialog: DialogInterface?) {
        mainDialog = false
        secondaryDialog = false
    }

    override fun openMainWeaponsDialog() {
        mainDialog = true
        val mainWeapons = detailPlayerFragment!!.retrieveMainWeapons()

        val bundle = Bundle()
        bundle.putSerializable(ARG_WEAPONS, mainWeapons)
        openGunDialog(bundle)
    }

    override fun openSecondaryWeaponsDialog() {
        secondaryDialog = true
        val secondaryWeapons = detailPlayerFragment!!.retrieveSecondaryWeapons()

        val bundle = Bundle()
        bundle.putSerializable(ARG_WEAPONS, secondaryWeapons)
        openGunDialog(bundle)
    }

    override fun selectWeapon(view: View, category: EquipmentCategoryEnum, weapon: Weapon) {
        when(category){
            EquipmentCategoryEnum.PISTOL -> {
                if(secondaryDialog)
                    Toast.makeText(this, weapon.name, Toast.LENGTH_SHORT).show()
            }
            EquipmentCategoryEnum.HEAVY -> {
                if (mainDialog)
                    Toast.makeText(this, weapon.name, Toast.LENGTH_SHORT).show()

            }
            EquipmentCategoryEnum.SMG -> {
                if (mainDialog)
                    Toast.makeText(this, weapon.name, Toast.LENGTH_SHORT).show()
            }
            EquipmentCategoryEnum.RIFLE -> {
                if (mainDialog)
                    Toast.makeText(this, weapon.name, Toast.LENGTH_SHORT).show()
            }
            else -> Toast.makeText(this, "", Toast.LENGTH_SHORT).show()
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
        const val ARG_PLAYER = "ARG_PLAYER"
    }

}
