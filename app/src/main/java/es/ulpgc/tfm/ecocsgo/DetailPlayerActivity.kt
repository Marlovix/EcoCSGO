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
import kotlinx.android.synthetic.main.content_detail_player.*
import java.util.*
import kotlin.collections.ArrayList

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

    private var player: Player? = null
    private var pistolGuns: ArrayList<SecondaryGun>? = null
    private var heavyGuns: ArrayList<MainGun>? = null
    private var smgGuns: ArrayList<MainGun>? = null
    private var rifleGuns: ArrayList<MainGun>? = null
    private var defuseKit: DefuseKit? = null
    private var helmet: Helmet? = null
    private var vest: Vest? = null

    private var mainGuns: MutableMap<EquipmentCategory, ArrayList<MainGun>?> =
        EnumMap(EquipmentCategory::class.java)
    private var secondaryGuns: MutableMap<EquipmentCategory, ArrayList<SecondaryGun>?> =
        EnumMap(EquipmentCategory::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_player)
        setSupportActionBar(toolbar)

        // add back arrow to toolbar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        getData()
        prepareScreen()
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
                    pistolGuns?.get(position)?.let { player?.registerSecondaryGun(it) }
            }
            EquipmentCategory.HEAVY -> {
                if (mainDialog)
                    Toast.makeText(this, heavyGuns?.get(position)?.name, Toast.LENGTH_SHORT).show()
            }
            EquipmentCategory.SMG -> {
                if (mainDialog)
                    Toast.makeText(this, smgGuns?.get(position)?.name, Toast.LENGTH_SHORT).show()
            }
            EquipmentCategory.RIFLE -> {
                if (mainDialog)
                    Toast.makeText(this, rifleGuns?.get(position)?.name, Toast.LENGTH_SHORT).show()
            }
            else -> Toast.makeText(this, "NEIN!", Toast.LENGTH_SHORT).show()
        }
        updateView()
        dialog?.dismiss()
    }

    private fun updateView(){
        val adapterMainGuns =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, player!!.mainGuns!!)
        val adapterSecondaryGuns =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, player!!.secondaryGuns!!)

        adapterMainGuns.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapterSecondaryGuns.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerMainGuns!!.adapter = adapterMainGuns
        spinnerSecondaryGuns!!.adapter = adapterSecondaryGuns

        for (i in 0 .. player!!.mainGuns!!.size) {
            if(player?.mainGuns!!.isNotEmpty() &&
                player!!.mainGuns!![i] == player!!.mainGunInGame) {
                spinnerMainGuns!!.setSelection(i)
                break
            }
        }

        for (i in 0 .. player!!.secondaryGuns!!.size) {
            if(player?.secondaryGuns!!.isNotEmpty() &&
                player!!.secondaryGuns!![i] == player!!.secondaryGunInGame){
                spinnerSecondaryGuns!!.setSelection(i)
                break
            }
        }

        editTextMainCasualties!!.setText(
            if (player!!.mainGunInGame == null) "0" else player!!.mainGunInGame?.casualty.toString()
        )
        editTextSecondaryCasualties!!.setText(
            if (player!!.secondaryGunInGame == null) "0"
            else player!!.secondaryGunInGame?.casualty.toString()
        )
    }

    private fun getData() {
        player = intent.getParcelableExtra(ItemDetailFragment.ARG_PLAYER)
        pistolGuns = intent.getParcelableArrayListExtra(ItemDetailFragment.ARG_PISTOL)
        heavyGuns = intent.getParcelableArrayListExtra(ItemDetailFragment.ARG_HEAVY)
        smgGuns = intent.getParcelableArrayListExtra(ItemDetailFragment.ARG_SMG)
        rifleGuns = intent.getParcelableArrayListExtra(ItemDetailFragment.ARG_RIFLE)
        defuseKit = intent.getParcelableExtra(ItemDetailFragment.ARG_DEFUSE_KIT)
        helmet = intent.getParcelableExtra(ItemDetailFragment.ARG_HELMET)
        vest = intent.getParcelableExtra(ItemDetailFragment.ARG_VEST)

        secondaryGuns[EquipmentCategory.PISTOL] = pistolGuns
        mainGuns[EquipmentCategory.HEAVY] = heavyGuns
        mainGuns[EquipmentCategory.SMG] = smgGuns
        mainGuns[EquipmentCategory.RIFLE] = rifleGuns
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

        initButtons()

        spinnerMainGuns = findViewById(R.id.spinner_main_guns)
        spinnerSecondaryGuns = findViewById(R.id.spinner_secondary_guns)
    }

    private fun prepareScreen() {

        initViews()

        updateView()

        textViewVest?.text = defuseKit?.name
        textViewHelmet?.text = helmet?.name
        textViewDefuseKit?.text = vest?.name
    }

    private fun initButtons() {
        buttonMainGuns?.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(
                ItemDetailFragment.ARG_GUNS,
                mainGuns as EnumMap<*, *>
            )
            mainDialog = true
            openGunDialog(bundle)
        }

        buttonSecondaryGuns?.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(
                ItemDetailFragment.ARG_GUNS,
                secondaryGuns as EnumMap<*, *>
            )
            secondaryDialog = true
            openGunDialog(bundle)
        }

        buttonAddMainCasualty?.setOnClickListener {
            if(player?.mainGunInGame != null && player?.mainGunInGame?.casualty!! < 5){
                player?.mainGunInGame?.casualty!!.inc()
                editText_main_casualties.setText(player?.mainGunInGame?.casualty.toString())
            }
        }

        buttonRemoveMainCasualty?.setOnClickListener {
            if(player?.mainGunInGame != null && player?.mainGunInGame?.casualty!! > 0){
                player?.mainGunInGame?.casualty!!.dec()
                editText_main_casualties.setText(player?.mainGunInGame?.casualty.toString())
            }
        }

        buttonAddSecondaryCasualty?.setOnClickListener {
            if(player?.secondaryGunInGame != null && player?.secondaryGunInGame?.casualty!! < 5){
                player?.secondaryGunInGame?.casualty = player?.secondaryGunInGame?.casualty!!.inc()
                editText_secondary_casualties.setText(
                    player?.secondaryGunInGame?.casualty.toString())
            }
        }

        buttonRemoveSecondaryCasualty?.setOnClickListener {
            if(player?.secondaryGunInGame != null && player?.secondaryGunInGame?.casualty!! > 0){
                player?.secondaryGunInGame?.casualty = player?.secondaryGunInGame?.casualty!!.dec()
                editText_secondary_casualties.setText(
                    player?.secondaryGunInGame?.casualty.toString())
            }
        }
    }

    private fun openGunDialog(bundle: Bundle) {
        dialog = GunListFragmentDialog(this)
        dialog!!.arguments = bundle
        dialog!!.show(supportFragmentManager, null)
    }

}
