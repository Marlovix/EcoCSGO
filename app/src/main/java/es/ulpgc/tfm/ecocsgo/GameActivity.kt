package es.ulpgc.tfm.ecocsgo

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.core.view.setMargins
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import es.ulpgc.tfm.ecocsgo.MainActivity.Companion.ARG_TEAM
import es.ulpgc.tfm.ecocsgo.adapter.PlayerRecyclerViewAdapter
import es.ulpgc.tfm.ecocsgo.callback.PlayerCallback
import es.ulpgc.tfm.ecocsgo.fragment.DetailPlayerFragment
import es.ulpgc.tfm.ecocsgo.fragment.GunListFragmentDialog
import es.ulpgc.tfm.ecocsgo.model.EquipmentCategoryEnum
import es.ulpgc.tfm.ecocsgo.model.EquipmentTeamEnum
import es.ulpgc.tfm.ecocsgo.model.Game
import es.ulpgc.tfm.ecocsgo.model.Player
import es.ulpgc.tfm.ecocsgo.viewmodel.GameActivityViewModel
import es.ulpgc.tfm.ecocsgo.viewmodel.PlayersViewModel
import kotlinx.android.synthetic.main.list_players.*
import java.util.*

class GameActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    GunListFragmentDialog.OnGunListFragmentInteraction,
    DetailPlayerFragment.OnDetailPlayerFragmentInteraction {

    private var twoPane: Boolean = false
    var game : Game? = null

    private var playerAdapter: PlayerRecyclerViewAdapter? = null
    private var playersViewModel: PlayersViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        val teamSelected = intent.getStringExtra(ARG_TEAM)
        if(teamSelected != null) game = Game(this, EquipmentTeamEnum.valueOf(teamSelected))

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        // large-screen layouts (res/values-w900dp) //
        if (item_detail_container != null) {
            twoPane = true
            val params = CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT,
                CoordinatorLayout.LayoutParams.MATCH_PARENT)
            params.gravity = Gravity.START or Gravity.BOTTOM
            params.setMargins(resources.getDimension(R.dimen.fab_margin).toInt())
            fab.layoutParams = params
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        setupRecyclerView()

        startGame()
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
        val playersViewModel : PlayersViewModel by viewModels()
        this.playersViewModel = playersViewModel

        val players = MutableLiveData<ArrayList<Player>>()
        players.value = game!!.rounds[0]?.players

        playerAdapter = playersViewModel!!.getPlayers()?.value?.let {
            PlayerRecyclerViewAdapter(this, it as ArrayList<Player>, twoPane)
        }

        list_players.adapter = playerAdapter

        val callback = playerAdapter?.let { PlayerCallback(it, this) }
        val touchHelper = callback?.let { ItemTouchHelper(it) }
        touchHelper?.attachToRecyclerView(list_players)

        val model = ViewModelProviders.of(this)[PlayersViewModel::class.java]
        model.getPlayers()?.observe(this, Observer<List<Player>>{ players ->
            for (player : Player in players){
                player.toString()
            }
        })
    }

    private fun startGame() {
        //game?.initRound()
    }

    fun firstRound(){
        //game?.startRound(0)
    }

    fun updateGame(){

    }

    override fun prueba() {
        Toast.makeText(this, "GameActivity", Toast.LENGTH_LONG).show()
    }

    override fun selectGun(view: View, category: EquipmentCategoryEnum, position: Int) {
        TODO("Not yet implemented")
    }

}