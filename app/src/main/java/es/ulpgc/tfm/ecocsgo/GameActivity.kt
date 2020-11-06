package es.ulpgc.tfm.ecocsgo

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
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
import es.ulpgc.tfm.ecocsgo.fragment.*
import es.ulpgc.tfm.ecocsgo.model.*
import es.ulpgc.tfm.ecocsgo.viewmodel.GameActivityViewModel
import es.ulpgc.tfm.ecocsgo.viewmodel.PlayerViewModel
import kotlinx.android.synthetic.main.list_players.*
import java.util.*

class GameActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    GameListPlayersFragment.OnListPlayersFragmentInteraction, DialogInterface.OnDismissListener,
    DetailPlayerFragment.OnDetailPlayerFragmentInteraction,
    WeaponListFragmentDialog.OnWeaponListFragmentInteraction,
    InfoGameFragmentDialog.OnFormInfoGameFragmentInteraction,
    FinishRoundFragmentDialog.OnFinishRoundFragmentInteraction{

    private var dialogWeapons: WeaponListFragmentDialog? = null
    private var dialogInfoGame: InfoGameFragmentDialog? = null
    private var dialogFinishRound: FinishRoundFragmentDialog? = null
    private var mainDialog = false
    private var secondaryDialog = false
    private var infoGameDialog = false
    private var finishRoundDialog = false

    var twoPane: Boolean = false

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
        val game = gameViewModel.getGame().value

        if (game?.players?.isEmpty()!!) {
            game.createPlayers(EquipmentTeamEnum.valueOf(intent.getStringExtra(ARG_TEAM)!!))
            game.initFirstRound()
            game.players?.let { gameViewModel.updatePlayers(it) }
            game.enemyEconomy.let { gameViewModel.setEnemyEconomy(it) }
            game.infoGame?.let { gameViewModel.setInfoGameLiveData(it) }
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
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        if(mainDialog){
            openMainWeaponsDialog()
        }else if(secondaryDialog){
            openSecondaryWeaponsDialog()
        }
    }

    override fun onDismiss(dialog: DialogInterface?) {
        mainDialog = false
        secondaryDialog = false
        infoGameDialog = false
        finishRoundDialog = false
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ARG_RESPONSE_PLAYER) {
            val player = data?.getParcelableExtra<Player>(DetailPlayerActivity.ARG_PLAYER)
            updatePlayerData(player!!)
        } else if (requestCode == SPEECH_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val spokenText: String? =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).let { results ->
                    results?.get(0)
                }
            spokenText?.let { parseVoiceToNumeration(it) }
            //Toast.makeText(this, spokenText, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val game = gameViewModel.getGame().value
        var title = ""
        var value = 0

        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_grenade_death -> {
                title = resources.getString(R.string.menu_grenade_death)
                value = game?.infoGame?.grenadeDeaths!!
            }
            R.id.nav_knife_death -> {
                title = resources.getString(R.string.menu_knife_death)
                value = game?.infoGame?.knifeDeaths!!
            }
            R.id.nav_partner_death -> {
                title = resources.getString(R.string.menu_partner_death)
                value = game?.infoGame?.partnerDeaths!!
            }
            R.id.nav_zeus -> {
                title = resources.getString(R.string.menu_zeus)
                value = game?.infoGame?.zeus!!
            }
            R.id.nav_smoke -> {
                title = resources.getString(R.string.menu_smoke)
                value = game?.infoGame?.smoke!!
            }
            R.id.nav_flash -> {
                title = resources.getString(R.string.menu_flash)
                value = game?.infoGame?.flash!!
            }
            R.id.nav_he -> {
                title = resources.getString(R.string.menu_he)
                value = game?.infoGame?.he!!
            }
            R.id.nav_molotov -> {
                title = resources.getString(R.string.menu_molotov)
                value = game?.infoGame?.molotov!!
            }
            R.id.nav_decoy -> {
                title = resources.getString(R.string.menu_decoy)
                value = game?.infoGame?.decoy!!
            }
        }

        openInfoGameDialog(title, value)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
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
            R.id.action_finish_round -> {
                if (gameViewModel.getGame().value?.isThereNextRound()!!) {
                    openFinishRoundDialog()
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun selectPlayer(selectedPlayerIndex: Int) {
        val player = gameViewModel.getGame().value?.players?.get(selectedPlayerIndex)

        gameViewModel.setSelectedPlayer(selectedPlayerIndex)

        if (fragment_detail_player != null) {
            playerViewModel.setPlayer(player!!)
        } else {
            val intent = Intent(this, DetailPlayerActivity::class.java)
            intent.putExtra(DetailPlayerActivity.ARG_PLAYER, player)
            startActivityForResult(intent, ARG_RESPONSE_PLAYER)
        }
    }

    override fun openSpeechRecognize() {
        displaySpeechRecognizer()
    }

    override fun openMainWeaponsDialog() {
        mainDialog = true
        val mainWeapons = detailPlayerFragment!!.retrieveMainWeapons()
        val mainWeaponInGame = playerViewModel.getPlayer()?.value?.getMainWeaponInGame()

        val bundle = Bundle()
        bundle.putSerializable(DetailPlayerActivity.ARG_WEAPONS, mainWeapons)
        bundle.putString(DetailPlayerActivity.ARG_WEAPON_IN_GAME, mainWeaponInGame?.name)
        openWeaponsDialog(bundle)
    }

    override fun openSecondaryWeaponsDialog() {
        secondaryDialog = true
        val secondaryWeapons = detailPlayerFragment!!.retrieveSecondaryWeapons()
        val secondaryWeaponInGame = playerViewModel.getPlayer()?.value?.getSecondaryWeaponInGame()

        val bundle = Bundle()
        bundle.putSerializable(DetailPlayerActivity.ARG_WEAPONS, secondaryWeapons)
        bundle.putString(DetailPlayerActivity.ARG_WEAPON_IN_GAME, secondaryWeaponInGame?.name)
        openWeaponsDialog(bundle)
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
        if (weapon.numeration.category == EquipmentCategoryEnum.PISTOL)
            detailPlayerFragment?.addSecondaryWeapon(weapon as SecondaryWeapon)
        else
            detailPlayerFragment?.addMainWeapon(weapon as MainWeapon)

        detailPlayerFragment?.updatePlayerView(playerViewModel.getPlayer()?.value!!)
        updatePlayersView()

        dialogWeapons?.dismiss()
    }

    override fun add(title: String) {
        val infoGame = gameViewModel.getInfoGameLiveData().value
        when (title) {
            resources.getString(R.string.menu_grenade_death) ->
                infoGame?.grenadeDeaths = infoGame?.grenadeDeaths?.inc()!!
            resources.getString(R.string.menu_knife_death) ->
                infoGame?.knifeDeaths = infoGame?.knifeDeaths?.inc()!!
            resources.getString(R.string.menu_partner_death) ->
                infoGame?.partnerDeaths = infoGame?.partnerDeaths?.inc()!!
            resources.getString(R.string.menu_zeus) ->
                infoGame?.zeus = infoGame?.zeus?.inc()!!
            resources.getString(R.string.menu_smoke) ->
                infoGame?.smoke = infoGame?.smoke?.inc()!!
            resources.getString(R.string.menu_flash) ->
                infoGame?.flash = infoGame?.flash?.inc()!!
            resources.getString(R.string.menu_he) ->
                infoGame?.he = infoGame?.he?.inc()!!
            resources.getString(R.string.menu_molotov) ->
                infoGame?.molotov = infoGame?.molotov?.inc()!!
            resources.getString(R.string.menu_decoy) ->
                infoGame?.decoy = infoGame?.decoy?.inc()!!
        }
        infoGame?.let { gameViewModel.setInfoGameLiveData(it) }
    }

    override fun remove(title: String) {
        val infoGame = gameViewModel.getInfoGameLiveData().value
        when (title) {
            resources.getString(R.string.menu_grenade_death) ->
                infoGame?.grenadeDeaths = infoGame?.grenadeDeaths?.dec()!!
            resources.getString(R.string.menu_knife_death) ->
                infoGame?.knifeDeaths = infoGame?.knifeDeaths?.dec()!!
            resources.getString(R.string.menu_partner_death) ->
                infoGame?.partnerDeaths = infoGame?.partnerDeaths?.dec()!!
            resources.getString(R.string.menu_zeus) ->
                infoGame?.zeus = infoGame?.zeus?.dec()!!
            resources.getString(R.string.menu_smoke) ->
                infoGame?.smoke = infoGame?.smoke?.dec()!!
            resources.getString(R.string.menu_flash) ->
                infoGame?.flash = infoGame?.flash?.dec()!!
            resources.getString(R.string.menu_he) ->
                infoGame?.he = infoGame?.he?.dec()!!
            resources.getString(R.string.menu_molotov) ->
                infoGame?.molotov = infoGame?.molotov?.dec()!!
            resources.getString(R.string.menu_decoy) ->
                infoGame?.decoy = infoGame?.decoy?.dec()!!
        }
        infoGame?.let { gameViewModel.setInfoGameLiveData(it) }
    }

    override fun win(team: EquipmentTeamEnum) {
        val options : MutableMap<TypeFinalRoundEnum, String> =
            EnumMap(TypeFinalRoundEnum::class.java)

        if(team == EquipmentTeamEnum.CT){
            options[TypeFinalRoundEnum.TEAM] = getString(R.string.menu_deleted_enemies)
            options[TypeFinalRoundEnum.DEFUSE] = getString(R.string.menu_defused_bomb)
            options[TypeFinalRoundEnum.TIME] = getString(R.string.menu_expired_time)
        } else if(team == EquipmentTeamEnum.T){
            options[TypeFinalRoundEnum.TEAM] = getString(R.string.menu_deleted_enemies_no_bomb)
            options[TypeFinalRoundEnum.TEAM_BOMB] = getString(R.string.menu_deleted_enemies_bomb)
            options[TypeFinalRoundEnum.EXPLOSION] = getString(R.string.menu_detonated_bomb)
        }

        dialogFinishRound?.setVictoryOptions(options)
    }

    override fun lose(team: EquipmentTeamEnum) {
        val options : MutableMap<TypeFinalRoundEnum, String> =
            EnumMap(TypeFinalRoundEnum::class.java)

        if(team == EquipmentTeamEnum.T){
            options[TypeFinalRoundEnum.TEAM_BOMB] = getString(R.string.menu_defeat_with_bomb)
            options[TypeFinalRoundEnum.TEAM] = getString(R.string.menu_defeat_without_bomb)
        }

        if(options.isNotEmpty()){
            dialogFinishRound?.setDefeatOptions(options)
        } else {
            selectOption(null, false)
        }
    }

    override fun selectOption(option: TypeFinalRoundEnum?, isVictory: Boolean) {
        // Calculate economy after finish round //
        if (isVictory) {
            option.let { gameViewModel.getGame().value?.calculateVictoryToEnemyEconomy(it!!) }
        } else {
            var type: TypeFinalRoundEnum = TypeFinalRoundEnum.TEAM
            if (option != null) {
                type = option
            }
            gameViewModel.getGame().value?.calculateDefeatToEnemyEconomy(type)
        }

        gameViewModel.getGame().value?.calculateInfoGameToEnemyEconomy()

        gameViewModel.getGame().value?.calculatePlayersDataToEnemyEconomy()

        gameViewModel.getGame().value?.players?.let { gameViewModel.updatePlayers(it) }

        gameViewModel.getGame().value?.validEnemyEconomy()

        // Update Round //

        gameViewModel.getGame().value?.roundInGame =
            gameViewModel.getGame().value?.roundInGame?.plus(1)!!

        gameViewModel.getGame().value?.enemyEconomy?.let { gameViewModel.setEnemyEconomy(it) }

        // Changing of team //
        if (gameViewModel.getGame().value?.roundInGame == (Game.ROUNDS / 2) + 1) {
            if (gameViewModel.getGame().value?.enemyTeam == EquipmentTeamEnum.CT) {
                gameViewModel.getGame().value?.enemyTeam = EquipmentTeamEnum.T
            } else if (gameViewModel.getGame().value?.enemyTeam == EquipmentTeamEnum.T) {
                gameViewModel.getGame().value?.enemyTeam = EquipmentTeamEnum.CT
            }

            gameViewModel.getGame().value?.initFirstRound()
            gameViewModel.getGame().value?.enemyEconomy?.let { gameViewModel.setEnemyEconomy(it) }
        }

        updateToolBar()

        dialogFinishRound?.dismiss()
    }

    private fun parseVoiceToNumeration(voice: String) {
        if (voice.length != 2) {
            Toast.makeText(
                this,
                "No se ha reconocido la numeración. Por favor, vuelva a intentarlo.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        var item = 0
        var categoryId = 0

        for (i in voice.indices) {
            if (voice[i].isDigit()) {
                val number = voice[i].toString().toInt()
                if (i == 0) {
                    categoryId = number
                } else if (i == 1) {
                    item = number
                }
            } else {
                Toast.makeText(
                    this,
                    "No se ha reconocido la numeración. Por favor, vuelva a intentarlo.",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
        }

        val categoryPistol = EquipmentCategoryEnum.PISTOL.id
        val categoryHeavy = EquipmentCategoryEnum.HEAVY.id
        val categorySmg = EquipmentCategoryEnum.SMG.id
        val categoryRifle = EquipmentCategoryEnum.RIFLE.id

        if ((categoryId == categoryPistol || categoryId == categoryHeavy || categoryId == categorySmg || categoryId == categoryRifle) && (item in 1..6)) {
            val numeration: EquipmentNumeration
            for (categoryEnum in EquipmentCategoryEnum.values()) {
                if (categoryEnum.id == categoryId) {
                    numeration = EquipmentNumeration(item, categoryEnum)
                    val weapon = gameViewModel.getGame().value?.findWeaponByNumeration(numeration)
                    weapon?.let { gameViewModel.getGame().value?.addWeaponByVoice(it) }
                    gameViewModel.getGame().value?.players?.let { gameViewModel.updatePlayers(it) }
                    break
                }
            }
        } else {
            Toast.makeText(
                this,
                "No se ha encontrado ningún arma con esa numeración",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun displaySpeechRecognizer() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.label_add_weapon_by_voice))
        }
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE)
    }

    private fun openFinishRoundDialog() {
        val team = gameViewModel.getGame().value?.enemyTeam
        val bundle = Bundle()
        bundle.putParcelable(ARG_TEAM_FINISH_ROUND, team)

        finishRoundDialog = true
        dialogFinishRound = FinishRoundFragmentDialog(this)
        dialogFinishRound?.arguments = bundle
        dialogFinishRound?.show(supportFragmentManager, null)
    }

    private fun updateToolBar() {
        supportActionBar?.title = resources.getString(R.string.label_round) + " " +
                gameViewModel.getGame().value?.roundInGame
    }

    private fun openWeaponsDialog(bundle: Bundle) {
        dialogWeapons = WeaponListFragmentDialog(this)
        dialogWeapons?.arguments = bundle
        dialogWeapons?.show(supportFragmentManager, null)
    }

    private fun openInfoGameDialog(title: String, value: Int) {
        infoGameDialog = true

        val bundle = Bundle()
        bundle.putString(ARG_TITLE_INFO_GAME_DIALOG, title)
        bundle.putInt(ARG_VALUE_INFO_GAME_DIALOG, value)

        dialogInfoGame = InfoGameFragmentDialog(this)
        dialogInfoGame?.arguments = bundle
        dialogInfoGame?.show(supportFragmentManager, null)
    }

    private fun updatePlayersView() {
        val selectedPlayerIndex = gameViewModel.getSelectedPlayer().value!!
        val player = gameViewModel.getGame().value?.players?.get(selectedPlayerIndex)
        updatePlayerData(player!!)
    }

    private fun updatePlayerData(player: Player) {
        val selectedPlayerIndex = gameViewModel.getSelectedPlayer().value!!
        gameViewModel.getGame().value?.players?.set(selectedPlayerIndex, player)
        gameViewModel.getGame().value?.players?.let { gameViewModel.updatePlayers(it) }
    }

    companion object {
        const val ARG_RESPONSE_PLAYER = 1
        const val ARG_TITLE_INFO_GAME_DIALOG = "TITLE"
        const val ARG_VALUE_INFO_GAME_DIALOG = "VALUE"
        const val ARG_TEAM_FINISH_ROUND = "TEAM"
        const val SPEECH_REQUEST_CODE = 0
    }

}