package es.ulpgc.tfm.ecocsgo

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import es.ulpgc.tfm.ecocsgo.adapter.PlayerRecyclerViewAdapter
import es.ulpgc.tfm.ecocsgo.callback.PlayerCallback
import es.ulpgc.tfm.ecocsgo.model.*
import kotlinx.android.synthetic.main.player_list.*

class GameActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var twoPane: Boolean = false
    var game : Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        val teamSelected = intent.getStringExtra(ItemDetailFragment.ARG_TEAM)
        if(teamSelected != null){
            game = Game.getSingletonInstance(EquipmentTeam.valueOf(teamSelected))

            val firebase = DataFirebaseProvider(game!!, this)
            firebase.loadData()

            /*loadWeapons()
            loadUtilities()
            loadGrenades()
            loadEconomy()*/

            //game?.startRound(0)
        }

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        //setupRecyclerView()

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            Toast.makeText(this, "NO", Toast.LENGTH_SHORT).show() //super.onBackPressed()
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

    private fun setupRecyclerView() {
        val mAdapter = PlayerRecyclerViewAdapter(this, game!!.rounds[0].players, twoPane)
        val callback = PlayerCallback(mAdapter, this)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(player_list)
        player_list.adapter = mAdapter
    }
/*
    private fun loadWeapons() {
        val idPistolWeapons = resources.getStringArray(R.array.pistol_data)
        for (id : String in idPistolWeapons)
            game!!.pistolWeapons.add(SecondaryGunJava(id))

        val idSmgWeapons = resources.getStringArray(R.array.smg_data)
        for (id : String in idSmgWeapons)
            game!!.smgWeapons.add(MainGunJava(id))

        val idRifleWeapons = resources.getStringArray(R.array.rifle_data)
        for (id : String in idRifleWeapons)
            game!!.rifleWeapons.add(MainGunJava(id))

        val idHeavyWeapons = resources.getStringArray(R.array.heavy_data)
        for(id : String in idHeavyWeapons)
            game!!.heavyWeapons.add(MainGunJava(id))
    }

    private fun loadUtilities(){//4
        game?.kit = DefuseKit(resources.getString(R.string.defuse_kit_data))
        game?.helmet = Helmet(resources.getString(R.string.helmet_data))
        game?.taser = Taser(resources.getString(R.string.taser_data))
        game?.vest = Vest(resources.getString(R.string.vest_data))
    }

    private fun loadGrenades(){
        val idGrenades = resources.getStringArray(R.array.grenade_data)
        for (id : String in idGrenades)
            game!!.grenades.add(Grenade2(id))
    }

    private fun loadEconomy(){
        game!!.economy = EconomyGame(dialog, game)
    }

    private fun economyLoaded() : Boolean{
        return game!!.economy != null
    }
*/
    fun firstRound(){
        game?.startRound(0)
    }

    fun updateGame(){

    }

}