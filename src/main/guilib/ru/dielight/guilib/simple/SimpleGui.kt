package ru.dielight.guilib.simple

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.property.Identifiable
import org.spongepowered.api.item.inventory.property.InventoryDimension
import org.spongepowered.api.item.inventory.property.InventoryTitle
import org.spongepowered.api.text.Text
import ru.dielight.guilib.Gui
import ru.dielight.guilib.GuiRegistry


abstract class SimpleGui(
    registry: GuiRegistry,
    title: Text,
    val width: Int,
    val height: Int
) : Gui() {

    val id = Identifiable.random()

    val inventory = Inventory.builder()
        .property(InventoryTitle.of(title))
        .property(InventoryDimension.of(width, height))
        .property(id)
        .build(registry.plugin)

    override fun openFor(player: Player) {
        player.openInventory(inventory)
    }

    override fun ownerOf(inv: Inventory): Boolean {
        val oprop = inv.getProperty(Identifiable::class.java)
        if(!oprop.isPresent) return false
        return id.value!! == oprop.get().value!!
    }

}
