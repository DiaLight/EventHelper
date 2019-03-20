package dialight.guilib.snapshot

import dialight.guilib.View
import java.util.HashMap

class SequentialPageBuilder(
    val maxColumns: Int,
    val maxLines: Int,
    val items: List<View.Item>,
    val offsetColumn: Int = 0,
    val offsetLine: Int = 0
) : Iterator<HashMap<Int, View.Item>>, Iterable<HashMap<Int, View.Item>> {

    private var slotsIt: Iterator<View.Item> = items.iterator()

    override fun iterator() = this
    override fun hasNext(): Boolean {
        return slotsIt.hasNext()
    }

    override fun next(): HashMap<Int, View.Item> {
        val map = HashMap<Int, View.Item>()
        for (i in offsetLine..(maxLines - 1)) {
            for (j in offsetColumn..(maxColumns - 1)) {
                val index = (i * 9 + j)
                if(!slotsIt.hasNext()) return map
                val item = slotsIt.next()
                map[index] = item
            }
        }
        return map
    }

}