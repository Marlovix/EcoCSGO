package es.ulpgc.tfm.ecocsgo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import es.ulpgc.tfm.ecocsgo.model.EquipmentTeamEnum
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private var startGameButton : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        startGameButton = findViewById(R.id.start_game)
        startGameButton?.visibility = View.GONE
        startGameButton?.setOnClickListener {

            val options = arrayOf<CharSequence>(
                getString(R.string.label_counter_terrorists),
                getString(R.string.label_terrorist))

            val builder = AlertDialog.Builder(this)
            builder.setCancelable(false)
            builder.setTitle("Select your option:")
            builder.setItems(options) { _, which ->
                val teamSelected = if (which == 0) EquipmentTeamEnum.CT.name else EquipmentTeamEnum.T.name
                val intent = Intent(this, GameActivity::class.java)
                intent.putExtra(ARG_TEAM, teamSelected)
                startActivity(intent)
                finish()
            }
            builder.setNegativeButton(getString(android.R.string.cancel)) { _, _ ->
                //the user clicked on Cancel
            }
            builder.show()
        }

        val repo = RepoEquipment(this)
        repo.loadData()
    }

    fun finishRepoLoading(){
        progress_bar.visibility = View.GONE
        startGameButton?.visibility = View.VISIBLE
    }

    companion object{
        const val ARG_TEAM = "ARG_TEAM"
    }

}
