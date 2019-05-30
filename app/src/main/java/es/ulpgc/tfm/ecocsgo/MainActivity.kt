package es.ulpgc.tfm.ecocsgo

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import es.ulpgc.tfm.ecocsgo.model.*

class MainActivity : AppCompatActivity() {

    private var startGameButton : Button? = null

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
                val game = Game.getSingletonInstance(EquipmentTeam.CT)

                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra(ItemDetailFragment.ARG_ITEM_KIT, kit)
                intent.putExtra(ItemDetailFragment.ARG_HELMET, helmet)
                intent.putExtra(ItemDetailFragment.ARG_VEST, vest)

                intent.putExtra(ItemDetailFragment.ARG_GAME, game)
                startActivity(intent)
                finish()
            }
            builder.setNegativeButton(getString(android.R.string.cancel)) { _, _ ->
                //the user clicked on Cancel
            }
            builder.show()
        }
        loadUtilities()
    }

    private fun loadUtilities(){//4
        kit = DefuseKit(resources.getString(R.string.defuse_kit_data))
        helmet = Helmet(resources.getString(R.string.helmet_data))
        taser = Taser(resources.getString(R.string.taser_data))
        vest = Vest(resources.getString(R.string.vest_data))
    }
}
