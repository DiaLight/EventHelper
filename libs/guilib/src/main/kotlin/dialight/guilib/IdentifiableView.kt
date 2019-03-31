package dialight.guilib

import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.property.Identifiable
import org.spongepowered.api.item.inventory.property.InventoryDimension
import org.spongepowered.api.item.inventory.property.InventoryTitle
import org.spongepowered.api.item.inventory.property.SlotIndex
import org.spongepowered.api.item.inventory.query.QueryOperationTypes
import dialight.extensions.*
import org.spongepowered.api.item.inventory.type.GridInventory
import org.spongepowered.api.text.Text

abstract class IdentifiableView(
    val guiplugin: GuiPlugin,
    val id: Identifiable,
    title: Text,
    val width: Int,
    val height: Int
) : View {

    override val capacity = width * height

    override val inventory = Inventory.builder()
        .property(InventoryTitle.of(title))
        .property(InventoryDimension.of(width, height))
        .property(id)
        .build(guiplugin)

    private val items = Array<View.Item?>(width * height) { null }

    override operator fun get(index: Int) = items[index]
    override operator fun set(index: Int, item: View.Item?) {
//        inventory.set(SlotIndex.of(line * width), info)
        inventory[index] = item?.item
        items[index] = item
    }

}