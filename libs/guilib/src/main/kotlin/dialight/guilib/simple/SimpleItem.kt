package dialight.guilib.simple

import dialight.guilib.Gui
import dialight.guilib.View
import dialight.guilib.events.ItemClickEvent
import org.spongepowered.api.item.inventory.ItemStackSnapshot
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent


class SimpleItem(
    override val item: ItemStackSnapshot,
    val onClickOp: (ItemClickEvent) -> Unit = {}
) : View.Item {

    override fun onClick(event: ItemClickEvent) = onClickOp(event)


}