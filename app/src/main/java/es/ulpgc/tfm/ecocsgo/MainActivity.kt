package es.ulpgc.tfm.ecocsgo

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import es.ulpgc.tfm.ecocsgo.db.AppDatabase
import es.ulpgc.tfm.ecocsgo.db.AppHelperDB
import es.ulpgc.tfm.ecocsgo.db.RepoEquipment
import es.ulpgc.tfm.ecocsgo.model.EquipmentTeamEnum
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var startGameButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        startGameButton = findViewById(R.id.start_game)
        startGameButton?.visibility = View.GONE
        startGameButton?.setOnClickListener {

            val options = arrayOf<CharSequence>(
                getString(R.string.label_counter_terrorists),
                getString(R.string.label_terrorist)
            )

            val builder = AlertDialog.Builder(this)
            builder.setCancelable(false)
            builder.setTitle(getString(R.string.select_enemy_team) + ":")
            builder.setItems(options) { _, which ->
                if(dataAlreadyExists()){
                    val enemyTeam =
                        if (which == 0) EquipmentTeamEnum.CT.name else EquipmentTeamEnum.T.name
                    val intent = Intent(this, GameActivity::class.java)
                    intent.putExtra(ARG_TEAM, enemyTeam)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(it.context, it.context.getString(R.string.label_no_data),
                        Toast.LENGTH_LONG).show()
                }
            }
            builder.setNegativeButton(getString(android.R.string.cancel)) { _, _ ->
                //the user clicked on Cancel
            }
            builder.show()
        }

        if(isOnline(this)){
            val repo = RepoEquipment(this)
            repo.loadData()
        }else{
            Toast.makeText(this, getString(R.string.label_no_connection),
                Toast.LENGTH_LONG).show()
            finishRepoLoading()
        }
    }

    fun finishRepoLoading() {
        progress_bar.visibility = View.GONE
        startGameButton?.visibility = View.VISIBLE
    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    private fun dataAlreadyExists(): Boolean{
        val appDatabase = AppDatabase(this)
        val appHelperDB = AppHelperDB(appDatabase)

        appHelperDB.open()
        val result = !appHelperDB.fetchDefeatBonus().isNullOrEmpty()
        appHelperDB.close()

        return result
    }

    companion object {
        const val ARG_TEAM = "ARG_TEAM"
    }

}
