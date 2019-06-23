package es.ulpgc.tfm.ecocsgo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView

import es.ulpgc.tfm.ecocsgo.model.*
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.activity_detail_player.toolbar
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@Suppress("UNCHECKED_CAST")
class DetailPlayerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    GunListFragmentDialog.GunClickListener {
    override fun selectGun(view: View, position: Int) {
        Toast.makeText(this, position.toString(), Toast.LENGTH_SHORT).show()
    }

    private var textViewVest : TextView? = null
    private var textViewHelmet : TextView? = null
    private var textViewDefuseKit : TextView? = null

    private var buttonMainGuns : ImageButton? = null
    private var buttonSecondaryGuns : ImageButton? = null

    private var mainGuns : Map<EquipmentCategory, List<MainGun>> =
        EnumMap(es.ulpgc.tfm.ecocsgo.model.EquipmentCategory::class.java)
    private var secondaryGuns : MutableMap<EquipmentCategory, List<SecondaryGun>> =
        EnumMap(es.ulpgc.tfm.ecocsgo.model.EquipmentCategory::class.java)
    private var secondaryList : List<SecondaryGun> = ArrayList()

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

        val game = intent.getParcelableExtra<Game>(ItemDetailFragment.ARG_GAME)
        mainGuns = (intent.getSerializableExtra(ItemDetailFragment.ARG_MAIN_GUNS)
                as Map<EquipmentCategory, List<MainGun>>)
        secondaryList = intent.getParcelableArrayListExtra(ItemDetailFragment.ARG_SECONDARY_GUNS)
        secondaryGuns[EquipmentCategory.PISTOL] = secondaryList

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

    private fun prepareScreen() {
        val kit = intent.getParcelableExtra<DefuseKit>(ItemDetailFragment.ARG_ITEM_KIT)
        val helmet = intent.getParcelableExtra<Helmet>(ItemDetailFragment.ARG_HELMET)
        val vest = intent.getParcelableExtra<Vest>(ItemDetailFragment.ARG_VEST)

        textViewVest = findViewById(R.id.textView_vest)
        textViewHelmet = findViewById(R.id.textView_helmet)
        textViewDefuseKit = findViewById(R.id.textView_defuse_kit)

        textViewVest?.text = vest?.name
        textViewHelmet?.text = helmet?.name
        textViewDefuseKit?.text = kit.name

        buttonMainGuns = findViewById(R.id.imageButton_add_main_gun)
        buttonMainGuns?.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(ItemDetailFragment.ARG_GUNS,
                mainGuns as HashMap<EquipmentCategory, List<MainGun>>
            )
            openGunDialog(bundle)
        }

        buttonSecondaryGuns = findViewById(R.id.imageButton_add_secondary_gun)
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
