package es.ulpgc.tfm.ecocsgo

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import es.ulpgc.tfm.ecocsgo.model.*
import kotlinx.android.synthetic.main.activity_detail_player.*
import kotlinx.android.synthetic.main.activity_game.*
import java.util.*

@Suppress("UNCHECKED_CAST")
class DetailPlayerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    GunListFragmentDialog.GunClickListener, DialogInterface.OnDismissListener {

    private var spinnerMainGuns: Spinner? = null
    private var spinnerSecondaryGuns: Spinner? = null

    private var textViewVest: TextView? = null
    private var textViewHelmet: TextView? = null
    private var textViewDefuseKit: TextView? = null

    private var editTextMainCasualties: EditText? = null
    private var editTextSecondaryCasualties: EditText? = null

    private var buttonMainGuns: ImageButton? = null
    private var buttonSecondaryGuns: ImageButton? = null

    private var buttonDeleteMainGun: ImageButton? = null
    private var buttonDeleteSecondaryGun: ImageButton? = null

    private var buttonAddMainCasualty: ImageButton? = null
    private var buttonRemoveMainCasualty: ImageButton? = null

    private var buttonAddSecondaryCasualty: ImageButton? = null
    private var buttonRemoveSecondaryCasualty: ImageButton? = null

    private var dialog : GunListFragmentDialog? = null
    private var mainDialog = false
    private var secondaryDialog = false

    private var game: Game? = null
    private var player: Player? = null

    private var mainGuns: Map<EquipmentCategory, List<MainGun>> =
        EnumMap(es.ulpgc.tfm.ecocsgo.model.EquipmentCategory::class.java)
    private var secondaryGuns: MutableMap<EquipmentCategory, List<SecondaryGun>> =
        EnumMap(es.ulpgc.tfm.ecocsgo.model.EquipmentCategory::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_player)
        setSupportActionBar(toolbar)

        // add back arrow to toolbar
        if (supportActionBar != null) {
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
        player = intent.getParcelableExtra<Player>(ItemDetailFragment.ARG_PLAYER)

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

    override fun onDismiss(dialog: DialogInterface?) {
        mainDialog = false
        secondaryDialog = false
    }

    override fun selectGun(view: View, category: EquipmentCategory, position: Int) {
        when(category){
            EquipmentCategory.PISTOL -> {
                if(secondaryDialog)
                    Toast.makeText(this, game!!.pistolWeapons[position].name, Toast.LENGTH_SHORT).show()
            }
            EquipmentCategory.HEAVY -> {
                if (mainDialog)
                    Toast.makeText(this, game!!.heavyWeapons[position].name, Toast.LENGTH_SHORT).show()

            }
            EquipmentCategory.SMG -> {
                if (mainDialog)
                    Toast.makeText(this, game!!.smgWeapons[position].name, Toast.LENGTH_SHORT).show()
            }
            EquipmentCategory.RIFLE -> {
                if (mainDialog)
                    Toast.makeText(this, game!!.rifleWeapons[position].name, Toast.LENGTH_SHORT).show()
            }
        }
        dialog?.dismiss()
    }

    private fun initViews() {
        textViewVest = findViewById(R.id.textView_vest)
        textViewHelmet = findViewById(R.id.textView_helmet)
        textViewDefuseKit = findViewById(R.id.textView_defuse_kit)

        editTextMainCasualties = findViewById(R.id.editText_main_casualties)
        editTextSecondaryCasualties = findViewById(R.id.editText_secondary_casualties)

        buttonMainGuns = findViewById(R.id.imageButton_add_main_gun)
        buttonSecondaryGuns = findViewById(R.id.imageButton_add_secondary_gun)

        buttonDeleteMainGun = findViewById(R.id.imageButton_delete_main_gun)
        buttonDeleteSecondaryGun = findViewById(R.id.imageButton_delete_secondary_gun)

        buttonAddMainCasualty = findViewById(R.id.imageButton_add_main_casualty)
        buttonAddSecondaryCasualty = findViewById(R.id.imageButton_add_secondary_casualty)

        buttonRemoveMainCasualty = findViewById(R.id.imageButton_remove_main_casualty)
        buttonRemoveSecondaryCasualty = findViewById(R.id.imageButton_remove_secondary_casualty)

        spinnerMainGuns = findViewById(R.id.spinner_main_guns)
        spinnerSecondaryGuns = findViewById(R.id.spinner_secondary_guns)
    }

    private fun prepareScreen() {

        initViews()

        textViewVest?.text = game!!.kit.name
        textViewHelmet?.text = game!!.helmet.name
        textViewDefuseKit?.text = game!!.vest.name

        val arrayMainGuns = arrayOfNulls<MainGun>(player!!.mainGuns.size)
        val arraySecondaryGuns = arrayOfNulls<SecondaryGun>(player!!.secondaryGuns.size)

        val adapterMainGuns = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayMainGuns)
        val adapterSecondaryGuns = ArrayAdapter(this, android.R.layout.simple_spinner_item, arraySecondaryGuns)

        adapterMainGuns.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapterSecondaryGuns.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        player!!.mainGuns.toArray(arrayMainGuns)
        player!!.secondaryGuns.toArray(arraySecondaryGuns)

        spinnerMainGuns!!.adapter = adapterMainGuns
        spinnerSecondaryGuns!!.adapter = adapterSecondaryGuns

        editTextMainCasualties!!.setText(
            if (player!!.lastMainGun == null) "0" else player!!.lastMainGun.casualty.toString()
        )
        editTextSecondaryCasualties!!.setText(
            if (player!!.lastSecondaryGun == null) "0" else player!!.lastSecondaryGun.casualty.toString()
        )

        buttonMainGuns?.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(
                ItemDetailFragment.ARG_GUNS,
                mainGuns as EnumMap<EquipmentCategory, List<MainGun>>
            )
            mainDialog = true
            openGunDialog(bundle)
        }

        buttonSecondaryGuns?.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(
                ItemDetailFragment.ARG_GUNS,
                secondaryGuns as EnumMap<EquipmentCategory, List<SecondaryGun>>
            )
            secondaryDialog = true
            openGunDialog(bundle)
        }
    }

    private fun openGunDialog(bundle: Bundle) {
        dialog = GunListFragmentDialog(this)
        dialog!!.arguments = bundle
        dialog!!.show(supportFragmentManager, null)
    }

}
