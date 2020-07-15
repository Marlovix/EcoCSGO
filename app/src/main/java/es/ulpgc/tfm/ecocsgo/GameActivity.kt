package es.ulpgc.tfm.ecocsgo

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import es.ulpgc.tfm.ecocsgo.MainActivity.Companion.ARG_TEAM
import es.ulpgc.tfm.ecocsgo.fragment.DetailPlayerFragment
import es.ulpgc.tfm.ecocsgo.fragment.GameListPlayersFragment
import es.ulpgc.tfm.ecocsgo.fragment.WeaponListFragmentDialog
import es.ulpgc.tfm.ecocsgo.model.*
import es.ulpgc.tfm.ecocsgo.viewmodel.GameActivityViewModel
import es.ulpgc.tfm.ecocsgo.viewmodel.PlayerViewModel
import kotlinx.android.synthetic.main.list_players.*

class GameActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    GameListPlayersFragment.OnListPlayersFragmentInteraction, DialogInterface.OnDismissListener,
    DetailPlayerFragment.OnDetailPlayerFragmentInteraction,
    WeaponListFragmentDialog.OnWeaponListFragmentInteraction {

    private var dialog : WeaponListFragmentDialog? = null
    private var mainDialog = false
    private var secondaryDialog = false

    var twoPane: Boolean = false
    private var game: Game? = null

    private var gameListPlayersFragment: GameListPlayersFragment? = null
    private var detailPlayerFragment: DetailPlayerFragment? = null

    private val gameViewModel: GameActivityViewModel by viewModels()
    private val playerViewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Init the game //
        game = gameViewModel.getGame().value

        if (game?.players?.isEmpty()!!){
            game?.createPlayers(EquipmentTeamEnum.valueOf(intent.getStringExtra(ARG_TEAM)!!))
            game?.initRound()
            game?.players?.let { gameViewModel.updatePlayers(it) }
            game?.enemyEconomy?.let { gameViewModel.setEnemyEconomy(it) }
        }

        updateToolBar()

        gameListPlayersFragment = GameListPlayersFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_list_players, gameListPlayersFragment!!).commit()

        // large-screen layouts (res/values-w900dp) //
        if (fragment_detail_player != null) {
            twoPane = true
            detailPlayerFragment = DetailPlayerFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_detail_player, detailPlayerFragment!!).commit()
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
    }

    override fun onDismiss(dialog: DialogInterface?) {
        mainDialog = false
        secondaryDialog = false
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            Toast.makeText(this, "NO", Toast.LENGTH_SHORT).show() //super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ARG_RESPONSE_PLAYER) {
            val player = data?.getParcelableExtra<Player>(DetailPlayerActivity.ARG_PLAYER)

            updatePlayerData(player!!)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.game_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_tools -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun selectPlayer(selectedPlayerIndex: Int) {
        val player = gameViewModel.getGame().value?.players?.get(selectedPlayerIndex)

        gameViewModel.setSelectedPlayer(selectedPlayerIndex)

        if (fragment_detail_player != null) {
            playerViewModel.setPlayer(player!!)
        }else{
            val intent = Intent(this, DetailPlayerActivity::class.java)
            intent.putExtra(DetailPlayerActivity.ARG_PLAYER, player)
            startActivityForResult(intent, ARG_RESPONSE_PLAYER)
        }
    }

    override fun openMainWeaponsDialog() {
        mainDialog = true
        val mainWeapons = detailPlayerFragment!!.retrieveMainWeapons()
        val mainWeaponInGame = playerViewModel.getPlayer()?.value?.getMainWeaponInGame()

        val bundle = Bundle()
        bundle.putSerializable(DetailPlayerActivity.ARG_WEAPONS, mainWeapons)
        bundle.putString(DetailPlayerActivity.ARG_WEAPON_IN_GAME, mainWeaponInGame?.name)
        openGunDialog(bundle)
    }

    override fun openSecondaryWeaponsDialog() {
        secondaryDialog = true
        val secondaryWeapons = detailPlayerFragment!!.retrieveSecondaryWeapons()
        val secondaryWeaponInGame = playerViewModel.getPlayer()?.value?.getSecondaryWeaponInGame()

        val bundle = Bundle()
        bundle.putSerializable(DetailPlayerActivity.ARG_WEAPONS, secondaryWeapons)
        bundle.putString(DetailPlayerActivity.ARG_WEAPON_IN_GAME, secondaryWeaponInGame?.name)
        openGunDialog(bundle)
    }

    override fun deleteWeapon(weapon: Weapon) {
        detailPlayerFragment?.deleteWeapon(weapon)
        updatePlayersView()
    }

    override fun addCasualty(weapon: Weapon) {
        detailPlayerFragment?.addCasualty(weapon)
        updatePlayersView()
    }

    override fun removeCasualty(weapon: Weapon) {
        detailPlayerFragment?.removeCasualty(weapon)
        updatePlayersView()
    }

    override fun selectWeaponInGame(weapon: Weapon) {
        detailPlayerFragment?.selectWeaponInGame(weapon)
        updatePlayersView()
    }

    override fun selectWeapon(weapon: Weapon) {
        if(weapon.numeration.category == EquipmentCategoryEnum.PISTOL)
            detailPlayerFragment?.addSecondaryWeapon(weapon as SecondaryWeapon)
        else
            detailPlayerFragment?.addMainWeapon(weapon as MainWeapon)

        detailPlayerFragment?.updatePlayerView(playerViewModel.getPlayer()?.value!!)
        updatePlayersView()

        dialog?.dismiss()
    }

    private fun updateToolBar(){
        supportActionBar?.title = resources.getString(R.string.label_round) + " " +
                gameViewModel.getGame().value?.roundInGame
    }

    private fun openGunDialog(bundle: Bundle) {
        dialog = WeaponListFragmentDialog(this)
        dialog!!.arguments = bundle
        dialog!!.show(supportFragmentManager, null)
    }

    private fun updatePlayersView(){
        val selectedPlayerIndex = gameViewModel.getSelectedPlayer().value!!
        val player = gameViewModel.getGame().value?.players?.get(selectedPlayerIndex)
        updatePlayerData(player!!)
    }

    private fun updatePlayerData(player: Player){
        val selectedPlayerIndex = gameViewModel.getSelectedPlayer().value!!
        gameViewModel.getGame().value?.players?.set(selectedPlayerIndex, player)
        gameViewModel.getGame().value?.players?.let { gameViewModel.updatePlayers(it) }
    }

    companion object {
        const val ARG_RESPONSE_PLAYER = 1
    }

}