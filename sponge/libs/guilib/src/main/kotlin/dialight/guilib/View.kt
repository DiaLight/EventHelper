package dialight.guilib

import dialight.guilib.events.GuiOutsideClickEvent
import dialight.guilib.events.ItemClickEvent
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.item.inventory.ItemStackSnapshot

interface View : Iterable<View.Item?> {

    val capacity: Int

    val inventory: Inventory

    operator fun get(index: Int): View.Item?
    operator fun set(index: Int, item: View.Item?)

    fun onOutsideClick(event: GuiOutsideClickEvent)

    interface Item {

        val item: ItemStack

        fun onClick(event: ItemClickEvent)

    }

    override fun iterator() = VIterator(this)

    class VIterator(val view: View) : Iterator<Item?> {
        var index = 0
        override fun hasNext(): Boolean {
            return index < view.capacity
        }

        override fun next() = view[index++]

    }

}