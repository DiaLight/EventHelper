package ru.dielight.guilib

import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent
import org.spongepowered.api.item.inventory.property.Identifiable
import ru.dialight.eventhelper.EventHelper

class GuiListener(val guistory: GuiStory) {

    @Listener
    fun onLeftClick(e: ClickInventoryEvent.Primary) {
        val oprop = e.targetInventory.getProperty(Identifiable::class.java)
        if(!oprop.isPresent) return
        val gui = guistory.currentGui(e.targetInventory.viewer, e.targetInventory) ?: return
        e.isCancelled = true
        
        
    }

    @Listener
    fun onMiddleClick(e: ClickInventoryEvent.Middle) {
        val oprop = e.targetInventory.getProperty(Identifiable::class.java)
        if(!oprop.isPresent) return
        val gui = guistory.currentGui(e.targetInventory.viewer, e.targetInventory) ?: return
        e.isCancelled = true
    }

    @Listener
    fun onRightClick(e: ClickInventoryEvent.Secondary) {
        val oprop = e.targetInventory.getProperty(Identifiable::class.java)
        if(!oprop.isPresent) return
        val gui = guistory.currentGui(e.targetInventory.viewer, e.targetInventory) ?: return
        e.isCancelled = true
    }

    @Listener
    fun onShiftLeftClick(e: ClickInventoryEvent.Shift.Primary) {
        val oprop = e.targetInventory.getProperty(Identifiable::class.java)
        if(!oprop.isPresent) return
        val gui = guistory.currentGui(e.targetInventory.viewer, e.targetInventory) ?: return
        e.isCancelled = true
    }

    @Listener
    fun onShiftRightClick(e: ClickInventoryEvent.Shift.Secondary) {
        val oprop = e.targetInventory.getProperty(Identifiable::class.java)
        if(!oprop.isPresent) return
        val gui = guistory.currentGui(e.targetInventory.viewer, e.targetInventory) ?: return
        e.isCancelled = true
    }

    @Listener
    fun onDoubleClick(e: ClickInventoryEvent.Double) {
        val oprop = e.targetInventory.getProperty(Identifiable::class.java)
        if(!oprop.isPresent) return
        val gui = guistory.currentGui(e.targetInventory.viewer, e.targetInventory) ?: return
        e.isCancelled = true
    }

    @Listener
    fun onCreativeClick(e: ClickInventoryEvent.Creative) {
        val oprop = e.targetInventory.getProperty(Identifiable::class.java)
        if(!oprop.isPresent) return
        val gui = guistory.currentGui(e.targetInventory.viewer, e.targetInventory) ?: return
        e.isCancelled = true
    }

    @Listener
    fun onDragLeft(e: ClickInventoryEvent.Drag.Primary) {
        val oprop = e.targetInventory.getProperty(Identifiable::class.java)
        if(!oprop.isPresent) return
        val gui = guistory.currentGui(e.targetInventory.viewer, e.targetInventory) ?: return
        e.isCancelled = true
    }

    @Listener
    fun onDragMiddle(e: ClickInventoryEvent.Drag.Middle) {
        val oprop = e.targetInventory.getProperty(Identifiable::class.java)
        if(!oprop.isPresent) return
        val gui = guistory.currentGui(e.targetInventory.viewer, e.targetInventory) ?: return
        e.isCancelled = true
    }

    @Listener
    fun onDragRight(e: ClickInventoryEvent.Drag.Secondary) {
        val oprop = e.targetInventory.getProperty(Identifiable::class.java)
        if(!oprop.isPresent) return
        val gui = guistory.currentGui(e.targetInventory.viewer, e.targetInventory) ?: return
        e.isCancelled = true

    }

    @Listener
    fun onDropFull(e: ClickInventoryEvent.Drop.Full) {
        val oprop = e.targetInventory.getProperty(Identifiable::class.java)
        if(!oprop.isPresent) return
        val gui = guistory.currentGui(e.targetInventory.viewer, e.targetInventory) ?: return
        e.isCancelled = true

    }

    @Listener
    fun onDropOutsideLeft(e: ClickInventoryEvent.Drop.Outside) {
        val oprop = e.targetInventory.getProperty(Identifiable::class.java)
        if(!oprop.isPresent) return
        val gui = guistory.currentGui(e.targetInventory.viewer, e.targetInventory) ?: return
        e.isCancelled = true

    }

    @Listener
    fun onDropOutsideRight(e: ClickInventoryEvent.Drop.Outside) {
        val oprop = e.targetInventory.getProperty(Identifiable::class.java)
        if(!oprop.isPresent) return
        val gui = guistory.currentGui(e.targetInventory.viewer, e.targetInventory) ?: return
        e.isCancelled = true

    }

    @Listener
    fun onDropSingle(e: ClickInventoryEvent.Drop.Single) {
        val oprop = e.targetInventory.getProperty(Identifiable::class.java)
        if(!oprop.isPresent) return
        val gui = guistory.currentGui(e.targetInventory.viewer, e.targetInventory) ?: return
        e.isCancelled = true

    }

    @Listener
    fun onNumberPress(e: ClickInventoryEvent.NumberPress) {
        val oprop = e.targetInventory.getProperty(Identifiable::class.java)
        if(!oprop.isPresent) return
        val gui = guistory.currentGui(e.targetInventory.viewer, e.targetInventory) ?: return
        e.isCancelled = true

    }

    @Listener
    fun onRecipeAll(e: ClickInventoryEvent.Recipe.All) {
        val oprop = e.targetInventory.getProperty(Identifiable::class.java)
        if(!oprop.isPresent) return
        val gui = guistory.currentGui(e.targetInventory.viewer, e.targetInventory) ?: return
        e.isCancelled = true

    }

    @Listener
    fun onRecipeSingle(e: ClickInventoryEvent.Recipe.Single) {
        val oprop = e.targetInventory.getProperty(Identifiable::class.java)
        if(!oprop.isPresent) return
        val gui = guistory.currentGui(e.targetInventory.viewer, e.targetInventory) ?: return
        e.isCancelled = true

    }

}