package dialight.captain.gui

import dialight.captain.CaptainPlugin
import dialight.guilib.snapshot.SnapshotGui
import org.spongepowered.api.entity.living.player.Player

class CaptainGui(val plugin: CaptainPlugin) : SnapshotGui<CaptainSnapshot>() {

    private var snap = CaptainSnapshot.Builder(plugin, id).build()

    override fun getSnapshot(player: Player) = snap

}