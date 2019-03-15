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
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.property.Identifiable
import org.spongepowered.api.scheduler.Task
import java.util.*

class FreezerGui(val plugin: FreezerPlugin) : SnapshotGui<FreezerSnapshot>() {

    override fun createSnapshot(player: Player) = FreezerSnapshot.Builder(plugin, id, player).build()

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
    }

}