package dialight.teleporter.gui

import dialight.extensions.*
import dialight.guilib.snapshot.Snapshot
import dialight.guilib.snapshot.SnapshotGui
import dialight.teleporter.TeleporterPlugin
import dialight.teleporter.event.TeleporterEvent
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.scheduler.Task

class TeleporterGui(val plugin: TeleporterPlugin) : SnapshotGui<TeleporterSnapshot>() {

    override fun createSnapshot(player: Player) = TeleporterSnapshot.Builder(plugin, id, player).build()

    @Listener
    fun onSelect(e: TeleporterEvent, @First player: Player) {
        val snap = opened[player.uniqueId] ?: return
        Task.builder().execute { task ->
            val inv = plugin.guilib!!.guimap[player] ?: return@execute
            for(sel in e.result.selected) {
                val (index, item) = snap.current[sel.uuid] ?: continue
//                println("$index $sel ${item.item.type} ${item.item.type.block}")
                inv[index] = item.item.createStack()
            }
            for(sel in e.result.unselected) {
                val (index, item) = snap.current[sel.uuid] ?: continue
                inv[index] = item.item.createStack()
            }
        }.delayTicks(10).submit(plugin)
    }

}