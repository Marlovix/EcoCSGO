package es.ulpgc.tfm.ecocsgo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.zzzzzitem_detail.view.*

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [GameActivity]
 * in two-pane mode (on tablets) or a [ItemDetailActivity]
 * on handsets.
 */
class ItemDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private var item: GameActivityContent.PlayerContent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val s: LinearLayoutManager
        arguments?.let {
            if (it.containsKey(ARG_GAME)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = GameActivityContent.ITEM_MAP[it.getString(ARG_GAME)]
                //activity?.toolbar_layout?.title = item?.content
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.zzzzzitem_detail, container, false)

        // Show the dummy content as text in a TextView.
        item?.let {
            rootView.item_detail.text = it.details
        }

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_KIT = "kit"
        const val ARG_HELMET = "helmet"
        const val ARG_VEST = "vest"

        const val ARG_GUNS = "guns"
        const val ARG_MAIN_GUNS = "mainGuns"
        const val ARG_SECONDARY_GUNS = "secondaryGuns"

        const val ARG_GAME = "game"
        const val ARG_PLAYER = "player"
    }
}
