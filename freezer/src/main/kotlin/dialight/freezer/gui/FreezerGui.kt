package dialight.freezer.gui

import dialight.extensions.getOrNull
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

    private var snap = FreezerSnapshot.Builder(plugin, id).build()

    override fun getSnapshot(player: Player) = snap

    @Listener
    fun onSelect(e: FreezerEvent, @First player: Player) {
        Task.builder().execute { task ->
            for ((uuid, snap) in opened) {
                val inv = plugin.guilib!!.guimap[uuid] ?: continue
                val current = snap.current(uuid)
                for(sel in e.result.freezed) {
                    val (index, item) = current[sel.uuid] ?: continue
                    inv[index] = item.item
                }
                for(sel in e.result.unfreezed) {
                    val (index, item) = current[sel.uuid] ?: continue
                    inv[index] = item.item
                }
            }
        }.submit(plugin)
        snap.update(e.result)
    }

    @Listener
    fun onJoin(event: ClientConnectionEvent.Join, @First player: Player) {
        val srv = Sponge.getServer()
        for ((uuid, snap) in opened) {
            val inv = plugin.guilib!!.guimap[uuid] ?: continue
            val current = snap.current(uuid)
            val (index, item) = current[uuid] ?: continue
            inv[index] = item.item
        }
        if(!snap.update(player.uniqueId)) {  // rebuild snap
            snap = FreezerSnapshot.Builder(plugin, id).build()
        }
    }
    @Listener
    fun onExit(event: ClientConnectionEvent.Disconnect, @First player: Player) {
        Task.builder().execute { task ->
            for ((uuid, snap) in opened) {
                val inv = plugin.guilib!!.guimap[uuid] ?: continue
                val current = snap.current(uuid)
                val (index, item) = current[uuid] ?: continue
                inv[index] = item.item
            }
            snap.update(player.uniqueId)
        }.submit(plugin)
    }

}