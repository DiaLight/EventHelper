package dialight.guilib

import com.flowpowered.math.vector.Vector2i
import dialight.extensions.getOrNull
import dialight.extensions.set
import dialight.guilib.mixin.setValue
import dialight.guilib.mixin.title
import dialight.observable.map.observableMapOf
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.Slot
import org.spongepowered.api.item.inventory.property.Identifiable
import org.spongepowered.api.item.inventory.property.InventoryDimension
import org.spongepowered.api.scheduler.Task
import java.util.*

class GuiMap(val plugin: GuiPlugin) {

    companion object {
        private val UUID_ZERO = UUID.nameUUIDFromBytes(ByteArray(0) { 0 })
    }

    private val guimap = observableMapOf<UUID, Inventory>()

    operator fun get(player: Player) = guimap[player.uniqueId]
    operator fun get(uuid: UUID) = guimap[uuid]

    fun inventoryClone(from: Inventory, player: Player) {
        val usrinv = guimap[player.uniqueId] ?: return
        inventoryClone(from, usrinv)
    }
    fun inventoryClone(from: Inventory, to: Inventory) {
        to.title = from.title
        var index = 0
        for(slot in from.slots<Slot>()) {
            val oitem = slot.peek()
            to[index] = if(oitem.isPresent) oitem.get() else null
            index++
        }
        val ofrprop = from.getInventoryProperty(Identifiable::class.java)
        if(!ofrprop.isPresent) throw Exception("Can't identify from view")
        val idfrprop = ofrprop.get()
        val frid = idfrprop.value!!
        val otoprop = to.getInventoryProperty(Identifiable::class.java)
        if(!ofrprop.isPresent) throw Exception("Can't identify to view")
        val idtoprop = otoprop.get()
        idtoprop.setValue(frid)
    }

    private fun inventoryGetDimension(inv: Inventory): Vector2i? {
        val oprop = inv.getInventoryProperty(InventoryDimension::class.java)
        if(!oprop.isPresent) return null
        return oprop.get().value
    }

    fun openView(player: Player, view: View) {
        val inv = view.inventory
        val dim = inventoryGetDimension(inv)
        val usrinv = guimap.getOrPut(player.uniqueId) { Inventory.builder()
            .of(inv.archetype)
            .property(Identifiable(GuiMap.UUID_ZERO))
            .apply {
                if(dim != null) property(InventoryDimension.of(dim))
            }
            .build(plugin) }

        val playerInv = player.openInventory.getOrNull()
        if(playerInv != null && playerInv.containsInventory(usrinv)) {  // update current inventory
            // add delay for update to keep event update order
            Task.builder().execute { task ->
                inventoryClone(inv, usrinv)
            }.submit(plugin)
        } else {  // open new inventory
            inventoryClone(inv, usrinv)  // update now
            // add delay for open to keep event update order
            Task.builder().execute { task ->
                player.openInventory(usrinv)
            }.submit(plugin)
        }

    }

}