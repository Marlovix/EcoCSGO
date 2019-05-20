package es.ulpgc.tfm.ecocsgo

import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main2.*
import android.widget.ArrayAdapter
import android.widget.TextView
import es.ulpgc.tfm.ecocsgo.model.*
import kotlinx.android.synthetic.main.content_main2.*


class Main2Activity : AppCompatActivity() {

    private var spinner2 : Spinner? = null

    private val pistolWeapons = java.util.ArrayList<SecondaryGun>()
    private val smgWeapons = java.util.ArrayList<MainGun>()
    private val rifleWeapons = java.util.ArrayList<MainGun>()
    private val heavyWeapons = java.util.ArrayList<MainGun>()
    private val grenades = java.util.ArrayList<Grenade>()
    private var helmet : Helmet? = null
    private var vest : Vest? = null
    private var kit : DefuseKit? = null
    private var taser : Taser? = null

    private var textViewVest : TextView? = null
    private var textViewHelmet : TextView? = null
    private var textViewDefuseKit : TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setSupportActionBar(toolbar)

        toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        toolbar.setNavigationOnClickListener { finish() }

        loadWeapons()
        loadGrenades()
        loadUtilities()
        loadRoundEconomy()

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
        textViewVest = findViewById(R.id.textView_vest)
        textViewHelmet = findViewById(R.id.textView1_helmet)
        textViewDefuseKit = findViewById(R.id.textView_defuse_kit)

        textViewVest?.text = vest?.name
        textViewHelmet?.text = helmet?.name
        textViewDefuseKit?.text = kit?.name
    }

    private fun loadWeapons() {
        val idPistolWeapons = resources.getStringArray(R.array.pistol_data)
        for (id : String in idPistolWeapons)
            pistolWeapons.add(SecondaryGun(id))

        val idSmgWeapons = resources.getStringArray(R.array.smg_data)
        for (id : String in idSmgWeapons)
            smgWeapons.add(MainGun(id))

        val idRifleWeapons = resources.getStringArray(R.array.rifle_data)
        for (id : String in idRifleWeapons)
            rifleWeapons.add(MainGun(id))

        val idHeavyWeapons = resources.getStringArray(R.array.heavy_data)
        for (id : String in idHeavyWeapons)
            heavyWeapons.add(MainGun(id))
    }

    private fun loadGrenades(){
        val idGrenades = resources.getStringArray(R.array.grenade_data)
        for (id : String in idGrenades)
            grenades.add(Grenade(id))
    }

    private fun loadUtilities(){//4
        kit = DefuseKit(resources.getString(R.string.defuse_kit_data))
        helmet = Helmet(resources.getString(R.string.helmet_data))
        taser = Taser(resources.getString(R.string.taser_data))
        vest = Vest(resources.getString(R.string.vest_data))
    }

    private fun loadRoundEconomy(){//knife

    }

}
