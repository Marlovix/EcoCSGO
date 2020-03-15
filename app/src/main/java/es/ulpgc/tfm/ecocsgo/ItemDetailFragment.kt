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
            if (it.containsKey(ARG_PLAYER)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = GameActivityContent.ITEM_MAP[it.getString(ARG_PLAYER)]
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
        const val ARG_DEFUSE_KIT = "ARG_DEFUSE_KIT"
        const val ARG_HELMET = "ARG_HELMET"
        const val ARG_VEST = "ARG_VEST"
        const val ARG_GUNS = "guns"
        const val ARG_PISTOL = "ARG_PISTOL"
        const val ARG_HEAVY = "ARG_HEAVY"
        const val ARG_SMG = "ARG_SMG"
        const val ARG_RIFLE = "ARG_RIFLE"
        const val ARG_PLAYER = "ARG_PLAYER"
    }
}
