package dialight.guilib.snapshot

import dialight.guilib.View
import java.util.HashMap


class SortedPlayersPageBuilder(
    val maxColumns: Int,
    val lines: Int,
    val sorted: Map<Char, List<View.Item>>,
    chars: List<Char>
): Iterator<Pair<String, HashMap<Int, View.Item>>>, Iterable<Pair<String, HashMap<Int, View.Item>>> {

    private var currentChar: Char = ' '
    private val charsIt = chars.iterator()
    private var slotsIt: Iterator<View.Item> = emptyArray<View.Item>().iterator()

    override fun iterator() = this
    override fun hasNext(): Boolean {
        if(slotsIt.hasNext()) return true
        nextChar()
        return slotsIt.hasNext()
    }

    override fun next(): Pair<String, HashMap<Int, View.Item>> {
        val builder = ColumnBuilderIt(lines, maxColumns)
        while(builder.hasNext() && hasNext()) {
            builder.next(slotsIt, currentChar)
        }
        return Pair(builder.nameBuilder.toString(), builder.slotCache)
    }

    fun nextChar() {
        while(charsIt.hasNext()) {
            currentChar = charsIt.next()
            val slots = sorted[currentChar]
            if(slots != null) {
                slotsIt = slots.iterator()
                break
            }
        }
    }

    class ColumnBuilderIt(
        val lines: Int,
        val maxColumns: Int
    ) {
        var displayChar: Char = ' '
        val nameBuilder = StringBuilder()
        val slotCache = HashMap<Int, View.Item>()
        var column = 0

        fun hasNext(): Boolean {
            return column != maxColumns
        }

        fun next(slotsIt: Iterator<View.Item>, currentChar: Char) {
            displayChar = currentChar
            while(slotsIt.hasNext() && column != maxColumns) {
                nameBuilder.append("  ").append(displayChar)
                nameBuilder.append(if (column % 2 == 0) " " else "  ")
                displayChar = ' '
                var row = 0
                while (slotsIt.hasNext()) {
                    val slot = slotsIt.next()
                    slotCache[column + row * maxColumns] = slot
                    row++
                    if (row == lines) {
                        break
                    }
                }
                column++
            }
        }
    }
}
