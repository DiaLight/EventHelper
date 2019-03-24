package dialight.random.gui

import dialight.extensions.ItemStackBuilderEx
import dialight.guilib.View
import dialight.guilib.events.ItemClickEvent
import dialight.guilib.simple.SimpleItem
import dialight.random.RandomPlugin
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.inventory.ItemStackSnapshot

class RandomItem(plugin: RandomPlugin) : View.Item {

    override val item = ItemStackBuilderEx(ItemTypes.ENDER_EYE)
        .build()

    override fun onClick(event: ItemClickEvent) {

    }

}