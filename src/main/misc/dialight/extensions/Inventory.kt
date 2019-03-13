package dialight.extensions

import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.item.inventory.ItemStackSnapshot
import org.spongepowered.api.item.inventory.property.SlotIndex
import org.spongepowered.api.item.inventory.query.QueryOperationTypes

operator fun Inventory.set(index: Int, item: ItemStack?) {
    val slot = query<Inventory>(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(index))).first<Inventory>()
    if (item == null) {
        slot.clear()
    } else {
        slot.set(item)
    }
}