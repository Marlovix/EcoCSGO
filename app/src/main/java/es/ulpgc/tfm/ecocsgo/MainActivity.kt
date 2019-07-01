package es.ulpgc.tfm.ecocsgo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import es.ulpgc.tfm.ecocsgo.model.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private var startGameButton : Button? = null

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
                val teamSelected = if (which == 0) EquipmentTeam.CT.name else EquipmentTeam.T.name
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra(ItemDetailFragment.ARG_TEAM, teamSelected)
                startActivity(intent)
                finish()
            }
            builder.setNegativeButton(getString(android.R.string.cancel)) { _, _ ->
                //the user clicked on Cancel
            }
            builder.show()
        }
    }

}
