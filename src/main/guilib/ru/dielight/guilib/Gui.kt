package ru.dielight.guilib

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.property.Identifiable
import org.spongepowered.api.item.inventory.property.InventoryDimension
import org.spongepowered.api.item.inventory.property.InventoryTitle
import org.spongepowered.api.text.Text

abstract class Gui {

    abstract fun openFor(player: Player)

    abstract fun ownerOf(inv: Inventory): Boolean

    abstract fun onClose(player: Player)

}