package es.ulpgc.tfm.ecocsgo.callback

import android.app.Activity
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import es.ulpgc.tfm.ecocsgo.adapter.PlayerRecyclerViewAdapter

class PlayerCallback(private val mAdapter: ItemTouchHelperContract, val activity: Activity) : ItemTouchHelper.Callback() {

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
        return makeMovementFlags(dragFlags, 0)
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
            if (viewHolder is PlayerRecyclerViewAdapter.ViewHolder) {
                val myViewHolder = viewHolder as PlayerRecyclerViewAdapter.ViewHolder?
                if (myViewHolder != null) {
                    mAdapter.onRowSelected(myViewHolder)
                }
            }
        }else if(actionState == ItemTouchHelper.ACTION_STATE_IDLE){
            Toast.makeText(activity, "From: $from - To: $to", Toast.LENGTH_SHORT).show()
        }

        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ) {
        if (viewHolder is PlayerRecyclerViewAdapter.ViewHolder)
            mAdapter.onRowClear(viewHolder)
        super.clearView(recyclerView, viewHolder)
    }

    interface ItemTouchHelperContract {
        fun onRowMoved(fromPosition: Int, toPosition: Int)
        fun onRowSelected(myViewHolder: PlayerRecyclerViewAdapter.ViewHolder)
        fun onRowClear(myViewHolder: PlayerRecyclerViewAdapter.ViewHolder)
    }

}