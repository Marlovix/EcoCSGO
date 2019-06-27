package es.ulpgc.tfm.ecocsgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import es.ulpgc.tfm.ecocsgo.model.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private var startGameButton : Button? = null

    private val pistolWeapons = ArrayList<SecondaryGun>()
    private val smgWeapons = ArrayList<MainGun>()
    private val rifleWeapons = ArrayList<MainGun>()
    private val heavyWeapons = ArrayList<MainGun>()
    private val grenades = ArrayList<Grenade>()

    private var helmet : Helmet? = null
    private var vest : Vest? = null
    private var kit : DefuseKit? = null
    private var taser : Taser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        startGameButton = findViewById(R.id.start_game)
        startGameButton?.setOnClickListener {

            val options = arrayOf<CharSequence>(
                getString(R.string.label_counter_terrorists),
                getString(R.string.label_terrorist))

            val builder = AlertDialog.Builder(this)
            builder.setCancelable(false)
            builder.setTitle("Select your option:")
            builder.setItems(options) { _, which ->

                val teamSelected = if (which == 0) EquipmentTeam.CT else EquipmentTeam.T
                val game = Game.getSingletonInstance(teamSelected)

                game.pistolWeapons = pistolWeapons
                game.heavyWeapons = heavyWeapons
                game.smgWeapons = smgWeapons
                game.rifleWeapons = rifleWeapons
                game.grenades = grenades
                game.kit = kit
                game.helmet = helmet
                game.vest = vest

                val intent = Intent(this, GameActivity::class.java)

                intent.putExtra(ItemDetailFragment.ARG_GAME, game)
                startActivity(intent)
                finish()
            }
            builder.setNegativeButton(getString(android.R.string.cancel)) { _, _ ->
                //the user clicked on Cancel
            }
            builder.show()
        }
        loadWeapons()
        loadUtilities()
        loadGrenades()
        loadEconomy()
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

    private fun loadUtilities(){//4
        kit = DefuseKit(resources.getString(R.string.defuse_kit_data))
        helmet = Helmet(resources.getString(R.string.helmet_data))
        taser = Taser(resources.getString(R.string.taser_data))
        vest = Vest(resources.getString(R.string.vest_data))
    }

    private fun loadGrenades(){
        val idGrenades = resources.getStringArray(R.array.grenade_data)
        for (id : String in idGrenades)
            grenades.add(Grenade(id))
    }

    private fun loadEconomy(){//knife

    }

}
