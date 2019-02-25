package ru.dielight.guilib

import org.spongepowered.api.item.inventory.property.Identifiable
import java.util.*

class GuiRegistry(val plugin: Any) {

    val registry = hashMapOf<UUID, Gui>()

    fun get(id: Identifiable) = registry[id.value!!]
    fun register(id: Identifiable, gui: Gui) { registry[id.value!!] = gui }

}