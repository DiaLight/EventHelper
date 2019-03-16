package dialight.freezer.gui

import dialight.extensions.set
import dialight.freezer.FreezerPlugin
import dialight.freezer.events.FreezerEvent
import dialight.guilib.Gui
import dialight.guilib.View
import dialight.guilib.simple.SimpleGui
import dialight.guilib.snapshot.Snapshot
import dialight.guilib.snapshot.SnapshotGui
import jekarus.colorizer.Text_colorized
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.network.ClientConnectionEvent
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.property.Identifiable
import org.spongepowered.api.scheduler.Task
import java.util.*

class FreezerGui(val plugin: FreezerPlugin) : SnapshotGui<FreezerSnapshot>() {

    val snap = FreezerSnapshot.Builder(plugin, id).build()

    override fun createSnapshot(player: Player) = snap

    @Listener
    fun onSelect(e: FreezerEvent, @First player: Player) {
        Task.builder().execute { task ->
            for ((uuid, snap) in opened) {
                val inv = plugin.guilib!!.guimap[uuid] ?: continue
                for(sel in e.result.freezed) {
                    val (index, item) = snap.current[sel.uuid] ?: continue
                    inv[index] = item.item.createStack()
                }
                for(sel in e.result.unfreezed) {
                    val (index, item) = snap.current[sel.uuid] ?: continue
                    inv[index] = item.item.createStack()
                }
            }
        }.submit(plugin)
        snap.update(e.result)
    }

    @Listener
    fun onJoin(event: ClientConnectionEvent.Join, @First player: Player) {
        for ((uuid, snap) in opened) {
            val inv = plugin.guilib!!.guimap[uuid] ?: continue
            val (index, item) = snap.current[player.uniqueId] ?: continue
            inv[index] = item.item.createStack()
        }
        snap.update(player.uniqueId)
    }
    @Listener
    fun onExit(event: ClientConnectionEvent.Disconnect, @First player: Player) {
        Task.builder().execute { task ->
            for ((uuid, snap) in opened) {
                val inv = plugin.guilib!!.guimap[uuid] ?: continue
                val (index, item) = snap.current[player.uniqueId] ?: continue
                inv[index] = item.item.createStack()
            }
            snap.update(player.uniqueId)
        }.submit(plugin)
    }

}