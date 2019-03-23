package dialight.extensions

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.item.inventory.ItemStackSnapshot
import org.spongepowered.api.item.inventory.property.SlotIndex
import org.spongepowered.api.item.inventory.query.QueryOperationTypes
import org.spongepowered.api.scheduler.Task

operator fun Inventory.set(index: Int, item: ItemStack?) {
    val slot = query<Inventory>(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(index))).first<Inventory>()
    if (item == null) {
        slot.clear()
    } else {
        slot.set(item)
    }
}

fun Player.openInventoryLater(plugin: Any, inventory: Inventory) {
    Task.builder().execute { task -> openInventory(inventory) }.submit(plugin)
}

fun Player.closeInventoryLater(plugin: Any) {
    Task.builder().execute { task -> closeInventory() }.submit(plugin)
}
