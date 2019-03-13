package dialight.guilib

import com.flowpowered.math.vector.Vector2i
import dialight.extensions.openInventoryLater
import dialight.extensions.set
import dialight.guilib.mixin.*
import dialight.observable.map.observableMapOf
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.Slot
import org.spongepowered.api.item.inventory.property.Identifiable
import org.spongepowered.api.item.inventory.property.InventoryDimension
import org.spongepowered.api.item.inventory.property.SlotIndex
import org.spongepowered.api.item.inventory.query.QueryOperationTypes
import org.spongepowered.api.scheduler.Task
import org.spongepowered.api.text.Text
import java.util.*
import java.util.stream.Collectors


class GuiStory(val plugin: GuiPlugin) {

    companion object {
        private val DEBUG = false
        private val UUID_ZERO = UUID.nameUUIDFromBytes(ByteArray(0) { 0 })
    }

    private val guiStory = HashMap<UUID, Story>()
    private val guimap = observableMapOf<UUID, Inventory>()

    private fun getPlayerInv(player: Player): Inventory? {
        val oPlayerInv = player.openInventory
        if(!oPlayerInv.isPresent) return null
        return oPlayerInv.get()
    }

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
            .property(Identifiable(UUID_ZERO))
            .apply {
                if(dim != null) property(InventoryDimension.of(dim))
            }
            .build(plugin) }

        val playerInv = getPlayerInv(player)
        if(playerInv != null && playerInv.containsInventory(usrinv)) {  // update current inventory
            // add delay for update to keep event update order
            Task.builder().execute { task ->
                plugin.guistory.inventoryClone(inv, usrinv)
            }.submit(plugin)
        } else {  // open new inventory
            plugin.guistory.inventoryClone(inv, usrinv)  // update now
            // add delay for open to keep event update order
            Task.builder().execute { task ->
                player.openInventory(usrinv)
            }.submit(plugin)
        }

    }

    fun openGui(player: Player, gui: Gui) {
        val story = getOrCreate(player)
        if (!story.opened) {
            val iterator = story.queue.descendingIterator()
            while (iterator.hasNext()) {
                val next = iterator.next()
                if (gui === next) break
                iterator.remove()
            }
        }
        if (story.queue.peekLast() !== gui) story.queue.addLast(gui)
        openView(player, gui.getView(player))
        story.opened = true
        if (DEBUG) Sponge.getServer().broadcastChannel.send(Text.of("opened: " + toString(player)))
    }

    fun openPrev(player: Player): Boolean {
        val story = guiStory[player.uniqueId] ?: return false
        val last = story.queue.pollLast()
        if(last != null) {
            last.destroyFor(player)
            val prev = story.queue.peekFirst()
            if(prev != null) {
                openView(player, prev.getView(player))
                story.opened = true
                return true
            }
        }
        player.closeInventory()
        clearStory(player)
        return false
    }

    fun openLast(player: Player): Boolean {
        val story = guiStory[player.uniqueId] ?: return false
        val gui = story.queue.peekLast() ?: return false
        openView(player, gui.getView(player))
        story.opened = true
        return true
    }

    fun currentGui(player: Player, inv: Inventory, remove: Boolean = false): Gui? {
        val oprop = inv.getInventoryProperty(Identifiable::class.java)
        if(!oprop.isPresent) return null
        val story = guiStory[player.uniqueId]
        if (story == null) {
            if (DEBUG) Sponge.getServer().broadcastChannel.send(Text.of("story == null"))
            return null
        }
        val iterator = story.queue.descendingIterator()
        var gui: Gui
        var found = false
        var toRemove = 0
        while (iterator.hasNext()) {
            gui = iterator.next()
            if (gui.ownerOf(inv)) {
                if (DEBUG) {
//                    val uuid = inv.getProperty(Identifiable::class.java).let { if(it.isPresent) it.get() else null } ?.value
                    val uuid = inv.getInventoryProperty(Identifiable::class.java).let { if(it.isPresent) it.get() else null } ?.value
                    Sponge.getServer().broadcastChannel.send(Text.of("  owner: " + toString(gui) + " by uuid " + uuid))
                }
                found = true
                break
            }
            toRemove++
        }
        if (!found) return null
        if(remove) {
            for (i in 0 until toRemove) {
                story.queue.removeLast().destroyFor(player)
            }
        }
        if (DEBUG) Sponge.getServer().broadcastChannel.send(
            Text.of(
                "current($toRemove): " + toString(story.queue.peekLast()) + " " + toString(player)
            )
        )
        return story.queue.peekLast()
    }

    fun onCloseGui(player: Player, inv: Inventory) {
        val story = guiStory[player.uniqueId] ?: return
        val iterator = story.queue.descendingIterator()
        var gui: Gui

        if (!iterator.hasNext()) { //1
            clearStory(player)
            return
        }
        gui = iterator.next()
        if (gui.ownerOf(inv)) {
            gui.destroyFor(player)
            story.opened = false
            if (DEBUG) Sponge.getServer().broadcastChannel.send(Text.of("closed: " + toString(gui) + " " + toString(player)))
            return
        }

        if (!iterator.hasNext()) return  //2
        gui = iterator.next()
        if (gui.ownerOf(inv)) {
            if (DEBUG) Sponge.getServer().broadcastChannel.send(Text.of("closed for open: " + toString(gui) + " " + toString(player)))
            return
        }
        story.opened = false
        if (DEBUG) Sponge.getServer().broadcastChannel.send(Text.of("nothing: " + toString(player)))
    }

    fun toString(player: Player): String {
        val story = guiStory[player.uniqueId]
        return if (story == null) "null" else toString(story.queue)
    }

    private fun toString(gui: Gui): String {
        return gui.javaClass.simpleName
    }

    private fun toString(story: Collection<Gui>): String {
        return story.stream().map { this.toString(it) }.collect(Collectors.joining(", ", "[", "]"))
    }

    fun clearStory(player: Player) {
        val story = guiStory.remove(player.uniqueId)
        if (story != null) {
            for (gui in story.queue) {
                gui.destroyFor(player)
            }
        }
    }

    private fun getOrCreate(player: Player) = guiStory.getOrPut(player.uniqueId) { Story() }

    fun closeAll() {
        val it = guiStory.entries.iterator()
        while (it.hasNext()) {
            val e = it.next()
            val oplayer = Sponge.getServer().getPlayer(e.key)
            if(!oplayer.isPresent) continue
            val player = oplayer.get()
            player.closeInventory()
            for (gui in e.value.queue) {
                gui.destroyFor(player)
            }
            it.remove()
        }
    }

    private class Story {

        val queue = LinkedList<Gui>()
        var opened = false

    }
}
