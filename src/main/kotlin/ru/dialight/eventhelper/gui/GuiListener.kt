package ru.dialight.eventhelper.gui

import com.google.inject.Inject
import org.slf4j.Logger
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.property.InventoryDimension
import org.spongepowered.api.item.inventory.property.InventoryTitle
import org.spongepowered.api.text.Text
import ru.dialight.eventhelper.EventHelper

class GuiListener(val plugin: EventHelper) {

    @Listener
    fun onLeftClick(e: ClickInventoryEvent.Primary) {
        val instance = e.targetInventory.plugin.instance
        if(!instance.isPresent) return
        if(instance.get() != plugin) return
        val op = e.targetInventory.applicableProperties
    }

    @Listener
    fun onMiddleClick(e: ClickInventoryEvent.Middle) {

    }

    @Listener
    fun onRightClick(e: ClickInventoryEvent.Secondary) {

    }

    @Listener
    fun onShiftLeftClick(e: ClickInventoryEvent.Shift.Primary) {

    }

    @Listener
    fun onShiftRightClick(e: ClickInventoryEvent.Shift.Secondary) {

    }

    @Listener
    fun onDoubleClick(e: ClickInventoryEvent.Double) {

    }

    @Listener
    fun onCreativeClick(e: ClickInventoryEvent.Creative) {

    }

    @Listener
    fun onDragLeft(e: ClickInventoryEvent.Drag.Primary) {

    }

    @Listener
    fun onDragMiddle(e: ClickInventoryEvent.Drag.Middle) {

    }

    @Listener
    fun onDragRight(e: ClickInventoryEvent.Drag.Secondary) {

    }

    @Listener
    fun onDropFull(e: ClickInventoryEvent.Drop.Full) {

    }

    @Listener
    fun onDropOutsideLeft(e: ClickInventoryEvent.Drop.Outside) {

    }

    @Listener
    fun onDropOutsideRight(e: ClickInventoryEvent.Drop.Outside) {

    }

    @Listener
    fun onDropSingle(e: ClickInventoryEvent.Drop.Single) {

    }

    @Listener
    fun onNumberPress(e: ClickInventoryEvent.NumberPress) {

    }

    @Listener
    fun onRecipeAll(e: ClickInventoryEvent.Recipe.All) {

    }

    @Listener
    fun onRecipeSingle(e: ClickInventoryEvent.Recipe.Single) {

    }

}