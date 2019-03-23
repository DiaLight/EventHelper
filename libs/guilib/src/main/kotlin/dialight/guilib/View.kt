package dialight.guilib

import dialight.guilib.events.GuiOutsideClickEvent
import dialight.guilib.events.ItemClickEvent
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.ItemStackSnapshot

interface View {

    val capacity: Int

    val inventory: Inventory

    operator fun get(index: Int): View.Item?
    operator fun set(index: Int, item: View.Item?)

    fun onOutsideClick(event: GuiOutsideClickEvent)

    interface Item {

        val item: ItemStackSnapshot

        fun onClick(event: ItemClickEvent)

    }

}