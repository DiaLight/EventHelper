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
    private val openmap = observableMapOf<UUID, UUID>()

    operator fun get(player: Player) = guimap[player.uniqueId]
    operator fun get(uuid: UUID) = guimap[uuid]

    fun inventoryClone(from: View, to: Inventory) {
        val inv = from.inventory
        to.title = inv.title
        var index = 0
        for(item in from) {
            to[index] = item?.item
            index++
        }
        val idfrprop = inv.getInventoryProperty(Identifiable::class.java).getOrNull()?.value ?: throw Exception("Can't identify from view")
        val idtoprop = to.getInventoryProperty(Identifiable::class.java).getOrNull() ?: throw Exception("Can't identify to view")
        idtoprop.setValue(idfrprop)
    }


    fun createInventoryFromView(view: View): Inventory {
        val inv = view.inventory
        val dim = inv.getInventoryProperty(InventoryDimension::class.java).getOrNull()?.value
        return Inventory.builder()
        .of(inv.archetype)
        .property(Identifiable(GuiMap.UUID_ZERO))
        .apply {
            if(dim != null) property(InventoryDimension.of(dim))
        }
        .build(plugin)
    }

    fun openView(player: Player, view: View) {
        var usrinv = guimap[player.uniqueId]
        if(usrinv == null) {
            usrinv = createInventoryFromView(view)
            guimap[player.uniqueId] = usrinv
        } else {
            if(usrinv.archetype != view.inventory.archetype) {
                usrinv = createInventoryFromView(view)
                guimap[player.uniqueId] = usrinv
            }
        }

        val playerInv = player.openInventory.getOrNull()
        if(playerInv != null && playerInv.containsInventory(usrinv)) {  // update current inventory
            // add delay for update to keep event update order
            Task.builder().execute { task ->
                openmap[player.uniqueId] = view.inventory.getInventoryProperty(Identifiable::class.java).getOrNull()!!.value!!
                inventoryClone(view, usrinv)
            }.submit(plugin)
        } else {  // open new inventory
            inventoryClone(view, usrinv)  // update now
            // add delay for open to keep event update order
            Task.builder().execute { task ->
                openmap[player.uniqueId] = view.inventory.getInventoryProperty(Identifiable::class.java).getOrNull()!!.value!!
                player.openInventory(usrinv)
            }.submit(plugin)
        }

    }

    fun closeInventory(gui: Gui, player: Player) {
        openmap.remove(player.uniqueId)
    }

    fun getCurrentUuid(player: Player): UUID? {
        return openmap[player.uniqueId]
    }

}