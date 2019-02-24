package ru.dialight.eventhelper.gui

import org.spongepowered.api.data.key.Key
import org.spongepowered.api.data.type.DyeColor
import org.spongepowered.api.data.value.mutable.Value
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.InventoryProperty
import org.spongepowered.api.item.inventory.property.InventoryDimension
import org.spongepowered.api.item.inventory.property.InventoryTitle
import org.spongepowered.api.text.Text
import org.spongepowered.api.util.generator.dummy.DummyObjectProvider
import ru.dialight.eventhelper.EventHelper

abstract class Gui(val plugin: EventHelper) {

    companion object {
//        val p = InventoryProperty.
        val DATA_KEY = DummyObjectProvider.createExtendedFor<Key<*>, Key<Value<String>>>(Key::class.java, "EH_DATA")
    }

    abstract val width:Int
    abstract val height:Int

    val inventory = Inventory.builder()
        .property("title", InventoryTitle.of(Text.of("Ультра мега инвентарь!")))
        .property("inventorydimension", InventoryDimension.of(9, 6))
//        .listener(ClickInventoryEvent.Primary::class.java) { it.isCancelled = true; this.onLeftClick(it) }
//        .listener(ClickInventoryEvent.Middle::class.java) { it.isCancelled = true; this.onMiddleClick(it) }
//        .listener(ClickInventoryEvent.Secondary::class.java) { it.isCancelled = true; this.onRightClick(it) }
//        .listener(ClickInventoryEvent.Shift.Primary::class.java) { it.isCancelled = true; this.onShiftLeftClick(it) }
//        .listener(ClickInventoryEvent.Shift.Secondary::class.java) { it.isCancelled = true; this.onShiftRightClick(it) }
//        .listener(ClickInventoryEvent.Double::class.java) { it.isCancelled = true; this.onDoubleClick(it) }
//        .listener(ClickInventoryEvent.Creative::class.java) { it.isCancelled = true; this.onCreativeClick(it) }
//        .listener(ClickInventoryEvent.Drag.Primary::class.java) { it.isCancelled = true; this.onDragLeft(it) }
//        .listener(ClickInventoryEvent.Drag.Middle::class.java) { it.isCancelled = true; this.onDragMiddle(it) }
//        .listener(ClickInventoryEvent.Drag.Secondary::class.java) { it.isCancelled = true; this.onDragRight(it) }
//        .listener(ClickInventoryEvent.Drop.Full::class.java) { it.isCancelled = true; this.onDropFull(it) }
//        .listener(ClickInventoryEvent.Drop.Outside.Primary::class.java) { it.isCancelled = true; this.onDropOutsideLeft(it) }
//        .listener(ClickInventoryEvent.Drop.Outside.Secondary::class.java) { it.isCancelled = true; this.onDropOutsideRight(it) }
//        .listener(ClickInventoryEvent.Drop.Single::class.java) { it.isCancelled = true; this.onDropSingle(it) }
//        .listener(ClickInventoryEvent.NumberPress::class.java) { it.isCancelled = true; this.onNumberPress(it) }
//        .listener(ClickInventoryEvent.Recipe.All::class.java) { it.isCancelled = true; this.onRecipeAll(it) }
//        .listener(ClickInventoryEvent.Recipe.Single::class.java) { it.isCancelled = true; this.onRecipeSingle(it) }
        .build(plugin)

}