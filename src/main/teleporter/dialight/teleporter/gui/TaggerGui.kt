package dialight.teleporter.gui

import dialight.guilib.Gui
import dialight.guilib.View
import dialight.guilib.snapshot.Snapshot
import dialight.guilib.snapshot.SnapshotGui
import dialight.teleporter.TeleporterPlugin
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.property.Identifiable
import java.util.*

class TaggerGui(val plugin: TeleporterPlugin) : SnapshotGui() {

    override fun createSnapshot(player: Player): Snapshot {
        return TaggerSnapshot.Builder(plugin, id, player).build()
    }


}