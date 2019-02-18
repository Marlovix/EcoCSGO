package es.ulpgc.tfm.ecocsgo

import android.app.ListActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity

import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private var startGameButton : Button? = null
    private val message = ""

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
            builder.setItems(options) { dialog, which ->
                val intent = Intent(this, ItemListActivity::class.java)
                // To pass any data to next activity
                intent.putExtra("keyIdentifier", "")
                // start your next activity
                when (which) {
                    1 -> print("x == 1")
                    2 -> print("x == 2")
                }
                startActivity(intent)
            }
            builder.setNegativeButton(getString(android.R.string.cancel)) { dialog, which ->
                //the user clicked on Cancel
            }
            builder.show()
        }

        // Write a message to the database
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("prueba")

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue(Int::class.java)
                Log.d("", "Value is: " + value!!)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("", "Failed to read value.", error.toException())
            }
        })
    }

}
