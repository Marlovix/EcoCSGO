package es.ulpgc.tfm.ecocsgo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import es.ulpgc.tfm.ecocsgo.model.*
import kotlinx.android.synthetic.main.activity_detail_player.*
import kotlinx.android.synthetic.main.activity_game.*
import java.util.*
import android.widget.ArrayAdapter

@Suppress("UNCHECKED_CAST")
class DetailPlayerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    GunListFragmentDialog.GunClickListener {
    override fun selectGun(view: View, position: Int) {
        Toast.makeText(this, position.toString(), Toast.LENGTH_SHORT).show()
    }

    private var spinnerMainGuns : Spinner? = null
    private var spinnerSecondaryGuns : Spinner? = null

    private var textViewVest : TextView? = null
    private var textViewHelmet : TextView? = null
    private var textViewDefuseKit : TextView? = null

    private var buttonMainGuns : ImageButton? = null
    private var buttonSecondaryGuns : ImageButton? = null

    private var game: Game? = null

    private var mainGuns : Map<EquipmentCategory, List<MainGun>> =
        EnumMap(es.ulpgc.tfm.ecocsgo.model.EquipmentCategory::class.java)
    private var secondaryGuns : MutableMap<EquipmentCategory, List<SecondaryGun>> =
        EnumMap(es.ulpgc.tfm.ecocsgo.model.EquipmentCategory::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_player)
        setSupportActionBar(toolbar)

        // add back arrow to toolbar
        if (supportActionBar != null){
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
        /*val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        toolbar.setNavigationOnClickListener { finish() }*/

        //nav_view.setNavigationItemSelectedListener(this)

        game = intent.getParcelableExtra<Game>(ItemDetailFragment.ARG_GAME)

        mainGuns = EnumMap(EquipmentCategory::class.java)
        (mainGuns as EnumMap<EquipmentCategory, List<MainGun>>)[EquipmentCategory.HEAVY] = game?.heavyWeapons
        (mainGuns as EnumMap<EquipmentCategory, List<MainGun>>)[EquipmentCategory.SMG] = game?.smgWeapons
        (mainGuns as EnumMap<EquipmentCategory, List<MainGun>>)[EquipmentCategory.RIFLE] = game?.rifleWeapons

        secondaryGuns = EnumMap(EquipmentCategory::class.java)
        (secondaryGuns as EnumMap<EquipmentCategory, List<SecondaryGun>>)[EquipmentCategory.PISTOL] =
            game?.pistolWeapons

        prepareScreen()

        /*spinner2 = findViewById(R.id.spinner2)
        val arraySpinner = arrayOf("1", "2", "3", "4", "5", "6", "7")
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, arraySpinner
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2?.adapter = adapter*/
    }

    private fun initViews(){
        textViewVest = findViewById(R.id.textView_vest)
        textViewHelmet = findViewById(R.id.textView_helmet)
        textViewDefuseKit = findViewById(R.id.textView_defuse_kit)

        buttonMainGuns = findViewById(R.id.imageButton_add_main_gun)
        buttonSecondaryGuns = findViewById(R.id.imageButton_add_secondary_gun)

        spinnerMainGuns = findViewById(R.id.spinner_main_guns)
        spinnerSecondaryGuns = findViewById(R.id.spinner_secondary_guns)
    }

    private fun prepareScreen() {

        initViews()

        textViewVest?.text = game!!.kit.name
        textViewHelmet?.text = game!!.helmet.name
        textViewDefuseKit?.text = game!!.vest.name

        spinnerMainGuns

        val arraySpinner = arrayOf("1", "2", "3", "4", "5", "6", "7")
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, arraySpinner
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMainGuns!!.adapter = adapter

        buttonMainGuns?.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(ItemDetailFragment.ARG_GUNS,
                mainGuns as HashMap<EquipmentCategory, List<MainGun>>
            )
            openGunDialog(bundle)
        }

        buttonSecondaryGuns?.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(ItemDetailFragment.ARG_GUNS,
                secondaryGuns as HashMap<EquipmentCategory, List<SecondaryGun>>
            )
            openGunDialog(bundle)
        }
    }

    private fun openGunDialog(bundle: Bundle){
        val dialog = GunListFragmentDialog(this)
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, null)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.detail_player_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up buttonMainGuns, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
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

}
