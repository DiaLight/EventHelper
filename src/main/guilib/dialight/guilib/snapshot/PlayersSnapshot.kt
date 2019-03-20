package dialight.guilib.snapshot

import dialight.guilib.GuiPlugin
import dialight.guilib.IdentifiableView
import dialight.guilib.View
import dialight.guilib.events.GuiOutsideClickEvent
import dialight.teleporter.gui.TeleporterSnapshot
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.property.Identifiable
import org.spongepowered.api.scheduler.Task
import org.spongepowered.api.text.Text
import java.util.*

open class PlayersSnapshot<T : PlayersSnapshot.Page>(
    guiplugin: GuiPlugin,
    id: Identifiable
) : Snapshot<T>(guiplugin, id) {

    open class Page(
        snap: PlayersSnapshot<*>,
        title: Text,
        width: Int,
        height: Int,
        index: Int
    ) : Snapshot.Page(
        snap, title, width, height, index
    ) {

        private val items = HashMap<UUID, Pair<Int, Item>>()

        override fun onOutsideClick(event: GuiOutsideClickEvent) {
            when(event.type) {
                GuiOutsideClickEvent.Type.LEFT -> {
                    snap.backward(event.player)
                }
                GuiOutsideClickEvent.Type.MIDDLE -> {
                    Task.builder().execute { task -> guiplugin.guistory.openPrev(event.player) }.submit(snap.guiplugin)
                }
                GuiOutsideClickEvent.Type.RIGHT -> {
                    snap.forward(event.player)
                }
            }
        }

        fun forEach(op: (Int, Item) -> Unit) {
            items.values.forEach { op(it.first, it.second) }
        }

        operator fun get(uuid: UUID) = items[uuid]
        override operator fun set(index: Int, item: View.Item?) {
            if(item == null) {
                val oldItem = this[index]
                if(oldItem != null)  {
                    if(oldItem is Item) {
                        items.remove(oldItem.uuid)
                    }
                }
            } else {
                if(item is Item) {
                    items[item.uuid] = Pair(index, item)
                }
            }
            super.set(index, item)
        }

        abstract class Item(val uuid: UUID, val name: String) : View.Item {

        }

    }


    class PageBuilderIt(
        val maxLines: Int,
        val maxColumns: Int,
        val sorted: Map<Char, List<View.Item>>,
        chars: List<Char>
    ): Iterator<Pair<String, HashMap<Int, View.Item>>> {

        private var currentChar: Char = ' '
        private val charsIt = chars.iterator()
        private var slotsIt: Iterator<View.Item> = emptyArray<View.Item>().iterator()

        override fun hasNext(): Boolean {
            if(slotsIt.hasNext()) return true
            nextChar()
            return slotsIt.hasNext()
        }

        override fun next(): Pair<String, HashMap<Int, View.Item>> {
            val builder = ColumnBuilderIt(maxLines, maxColumns)
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
            val maxLines: Int,
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
                        if (row == maxLines) {
                            break
                        }
                    }
                    column++
                }
            }
        }
    }

    open class Builder(val chars: List<Char>) {

        private fun findChar(name: String): Char {
            for (c in name.toLowerCase()) {
                if (chars.contains(c)) return c
            }
            return '_'
        }
        fun <T : Page.Item> sort(slots: ArrayList<T>): HashMap<Char, MutableList<T>> {
            val sorted = HashMap<Char, MutableList<T>>()
            for (slot in slots) {
                val c = findChar(slot.name)
                val charSlots = sorted.getOrPut(c) { ArrayList() }
                charSlots.add(slot)
            }
            return sorted
        }

    }

}