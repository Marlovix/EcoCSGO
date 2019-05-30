package es.ulpgc.tfm.ecocsgo

import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object GameActivityContent {

    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<PlayerContent> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, PlayerContent> = HashMap()

    private const val COUNT = 5

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(createDummyItem(i))
        }
    }

    private fun addItem(item: PlayerContent) {
        ITEMS.add(item)
        //ITEM_MAP.put(item.id, item)
        ITEM_MAP.put("-", item)
    }

    private fun createDummyItem(position: Int): PlayerContent {
        return PlayerContent(
            position.toString(),
            "Item " + position,
            makeDetails(position)
        )
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0 until position) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class PlayerContent(val id: String, val content: String, val details: String) {
        override fun toString(): String = content
    }
}
