package dialight.guilib.simple

import dialight.guilib.Gui
import dialight.guilib.View
import dialight.guilib.events.ItemClickEvent
import org.spongepowered.api.item.inventory.ItemStackSnapshot
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent
import org.spongepowered.api.item.inventory.ItemStack


class SimpleItem(
    srcItem: ItemStack,
    val onClickOp: (ItemClickEvent) -> Unit = {}
) : View.Item {

    private val itemSnap = srcItem.createSnapshot()

    override val item: ItemStack get() = itemSnap.createStack()

    override fun onClick(event: ItemClickEvent) = onClickOp(event)


}