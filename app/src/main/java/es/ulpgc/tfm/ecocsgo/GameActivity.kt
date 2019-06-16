package es.ulpgc.tfm.ecocsgo

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import es.ulpgc.tfm.ecocsgo.model.*
import kotlinx.android.synthetic.main.item_player_list.view.*
import kotlinx.android.synthetic.main.player_list.*
import java.util.*
import kotlin.collections.HashMap

class GameActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var twoPane: Boolean = false

    private val pistolWeapons = ArrayList<SecondaryGun>()
    private val smgWeapons = ArrayList<MainGun>()
    private val rifleWeapons = ArrayList<MainGun>()
    private val heavyWeapons = ArrayList<MainGun>()
    private val grenades = ArrayList<Grenade>()

    private var kit = DefuseKit()
    private var helmet = Helmet()
    private var vest = Vest()

    private var game : Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setupRecyclerView(player_list)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        kit = intent.getParcelableExtra(ItemDetailFragment.ARG_ITEM_KIT)
        helmet = intent.getParcelableExtra(ItemDetailFragment.ARG_HELMET)
        vest = intent.getParcelableExtra(ItemDetailFragment.ARG_VEST)

        game = intent.getParcelableExtra(ItemDetailFragment.ARG_GAME)

        loadWeapons()
        //loadGrenades()
        //loadRoundEconomy()
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            Toast.makeText(this, "NO", Toast.LENGTH_SHORT).show() //super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.game_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_tools -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        val mAdapter = PlayerRecyclerViewAdapter(this, GameActivityContent.ITEMS, twoPane)
        val callback = ItemMoveCallback(mAdapter, this)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)
        recyclerView.adapter = mAdapter
    }

    class ItemMoveCallback(private val mAdapter: ItemTouchHelperContract, val activity: Activity) : ItemTouchHelper.Callback() {

        private var from : String = ""
        private var to: String = ""

        override fun isLongPressDragEnabled(): Boolean {
            return true
        }

        override fun isItemViewSwipeEnabled(): Boolean {
            return true
        }


        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {

        }

        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, 0)
        }

        override fun onMove(
            recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            from = viewHolder.adapterPosition.toString()
            to = target.adapterPosition.toString()
            //mAdapter.onRowMoved(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }

        override fun onSelectedChanged(
            viewHolder: RecyclerView.ViewHolder?,
            actionState: Int
        ) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                /*if (viewHolder is EquipmentCategoryRecyclerViewAdapter.ViewHolder) {
                    val myViewHolder = viewHolder as EquipmentCategoryRecyclerViewAdapter.ViewHolder?
                    if (myViewHolder != null) {
                        mAdapter.onRowSelected(myViewHolder)
                    }
                }*/
            }else if(actionState == ItemTouchHelper.ACTION_STATE_IDLE){
                Toast.makeText(activity, "From: $from - To: $to", Toast.LENGTH_SHORT).show()
            }

            //super.onSelectedChanged(viewHolder, actionState)
        }

        override fun clearView(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ) {
            super.clearView(recyclerView, viewHolder)

            if (viewHolder is PlayerRecyclerViewAdapter.ViewHolder) {
                mAdapter.onRowClear(viewHolder)
            }
        }

        interface ItemTouchHelperContract {

            fun onRowMoved(fromPosition: Int, toPosition: Int)
            fun onRowSelected(myViewHolder: PlayerRecyclerViewAdapter.ViewHolder)
            fun onRowClear(myViewHolder: PlayerRecyclerViewAdapter.ViewHolder)

        }

    }

    class PlayerRecyclerViewAdapter(
        private val parentActivity: GameActivity,
        private val values: List<GameActivityContent.PlayerContent>,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<PlayerRecyclerViewAdapter.ViewHolder>(),
        ItemMoveCallback.ItemTouchHelperContract{

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as GameActivityContent.PlayerContent
                if (twoPane) {
                    val fragment = ItemDetailFragment().apply {
                        arguments = Bundle().apply {
                            putString(ItemDetailFragment.ARG_ITEM_KIT, item.id)
                        }
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit()
                } else {
                    //val kit = parentActivity.intent?.getParcelableExtra(ItemDetailFragment.ARG_ITEM_KIT)
                    val intent = Intent(v.context, DetailPlayerActivity::class.java).apply {
                        putExtra(ItemDetailFragment.ARG_ITEM_KIT, parentActivity.kit)
                        putExtra(ItemDetailFragment.ARG_HELMET, parentActivity.helmet)
                        putExtra(ItemDetailFragment.ARG_VEST, parentActivity.vest)

                        val mainGuns = HashMap<EquipmentCategory, List<MainGun>>()
                        mainGuns[EquipmentCategory.HEAVY] = parentActivity.heavyWeapons
                        mainGuns[EquipmentCategory.SMG] = parentActivity.smgWeapons
                        mainGuns[EquipmentCategory.RIFLE] = parentActivity.rifleWeapons

                        putExtra(ItemDetailFragment.ARG_MAIN_GUNS, mainGuns)
                        putExtra(ItemDetailFragment.ARG_SECONDARY_GUNS, parentActivity.pistolWeapons)
                        putExtra(ItemDetailFragment.ARG_GAME, parentActivity.game)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_player_list, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = "-"//item.id
            holder.contentView.text = item.content
            holder.death.text = "x0"
            holder.death2.text = "x0"

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val cardView : CardView = view.cardView
            val idView: TextView = view.sub_text2
            val contentView: TextView = view.sub_text
            val death: TextView = view.textView2
            val death2: TextView = view.textView3
        }

        override fun onRowMoved(fromPosition: Int, toPosition: Int) {
            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    Collections.swap(values, i, i + 1)
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap(values, i, i - 1)
                }
            }
            notifyItemMoved(fromPosition, toPosition)
        }

        override fun onRowSelected(myViewHolder: ViewHolder) {
            myViewHolder.cardView.setBackgroundColor(Color.GRAY)
        }

        override fun onRowClear(myViewHolder: ViewHolder) {
            myViewHolder.cardView.setBackgroundColor(Color.WHITE)
        }
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

    private fun loadRoundEconomy(){//knife

    }
}
