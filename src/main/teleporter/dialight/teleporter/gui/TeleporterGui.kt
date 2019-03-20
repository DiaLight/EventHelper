package dialight.teleporter.gui

import dialight.extensions.*
import dialight.guilib.snapshot.Snapshot
import dialight.guilib.snapshot.SnapshotGui
import dialight.teleporter.TeleporterPlugin
import dialight.teleporter.event.TeleporterEvent
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.network.ClientConnectionEvent
import org.spongepowered.api.scheduler.Task

class TeleporterGui(val plugin: TeleporterPlugin) : SnapshotGui<TeleporterSnapshot>() {

    override fun createSnapshot(player: Player) = TeleporterSnapshot.Builder(plugin, id, player).build()

    @Listener
    fun onSelect(e: TeleporterEvent, @First player: Player) {
        val snap = opened[player.uniqueId] ?: return
        Task.builder().execute { task ->
            val inv = plugin.guilib!!.guimap[player] ?: return@execute
            val current = snap.current(player)
            for(sel in e.result.selected) {
                val (index, item) = current[sel.uuid] ?: continue
                inv[index] = item.item.createStack()
            }
            for(sel in e.result.unselected) {
                val (index, item) = current[sel.uuid] ?: continue
                inv[index] = item.item.createStack()
            }
        }.submit(plugin)
        snap.update(e.result)
    }

    @Listener
    fun onJoin(event: ClientConnectionEvent.Join, @First player: Player) {
        for ((uuid, snap) in opened) {
            snap.update(player.uniqueId)
            val inv = plugin.guilib!!.guimap[uuid] ?: continue
            val current = snap.current(uuid)
            val (index, item) = current[player.uniqueId] ?: continue
            inv[index] = item.item.createStack()
        }
    }
    @Listener
    fun onExit(event: ClientConnectionEvent.Disconnect, @First player: Player) {
        Task.builder().execute { task ->
            for ((uuid, snap) in opened) {
                snap.update(player.uniqueId)
                val inv = plugin.guilib!!.guimap[uuid] ?: continue
                val current = snap.current(uuid)
                val (index, item) = current[player.uniqueId] ?: continue
                inv[index] = item.item.createStack()
            }
        }.submit(plugin)
    }

}