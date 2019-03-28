package dialight.guilib

import dialight.extensions.getOrNull
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.property.Identifiable
import org.spongepowered.api.text.Text
import java.util.*
import java.util.stream.Collectors


class GuiStory(val plugin: GuiPlugin) {

    companion object {
        private val DEBUG = false
    }

    private val guiStory = HashMap<UUID, Story>()

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
        plugin.openView(player, gui.getView(player))
        story.opened = true
        if (DEBUG) Sponge.getServer().broadcastChannel.send(Text.of("opened: " + toString(player)))
    }

    fun openPrev(player: Player): Boolean {
        val story = guiStory[player.uniqueId] ?: return false
        val last = story.queue.pollLast()
        if(last != null) {
            last.destroyFor(player)
            val prev = story.queue.peekLast()
            if(prev != null) {
                plugin.openView(player, prev.getView(player))
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
        plugin.openView(player, gui.getView(player))
        story.opened = true
        return true
    }

    fun currentGui(player: Player, inv: Inventory, remove: Boolean = false): Gui? {
//        if(!oprop.isPresent) return null
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
            if (gui.ownerOf(player, inv)) {
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
        if (gui.ownerOf(player, inv)) {
            gui.destroyFor(player)
            story.opened = false
            if (DEBUG) Sponge.getServer().broadcastChannel.send(Text.of("closed: " + toString(gui) + " " + toString(player)))
            return
        }

        if (!iterator.hasNext()) return  //2
        gui = iterator.next()
        if (gui.ownerOf(player, inv)) {
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
