package dialight.freezer.gui

import dialight.freezer.FreezerPlugin
import dialight.guilib.Gui
import dialight.guilib.View
import dialight.guilib.simple.SimpleGui
import dialight.guilib.snapshot.Snapshot
import dialight.guilib.snapshot.SnapshotGui
import jekarus.colorizer.Text_colorized
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.property.Identifiable
import java.util.*

class FreezerGui(val plugin: FreezerPlugin) : SnapshotGui() {


    override fun createSnapshot(player: Player): Snapshot {
        return FreezerSnapshot.Builder(plugin, id, player).build()
    }

}