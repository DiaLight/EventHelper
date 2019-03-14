package dialight.guilib.snapshot

import dialight.guilib.Gui
import dialight.guilib.View
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.property.Identifiable
import java.util.*

abstract class SnapshotGui : Gui {

    val opened = hashMapOf<UUID, Snapshot>()

    val id = Identifiable()

    override fun ownerOf(inv: Inventory): Boolean {
        val oprop = inv.getInventoryProperty(Identifiable::class.java)
        if(!oprop.isPresent) throw Exception("not present $inv")
        return id.value!! == oprop.get().value!!
    }

    abstract fun createSnapshot(player: Player): Snapshot

    override fun getView(player: Player): View {
        val snap = createSnapshot(player)
        opened[player.uniqueId] = snap
        return snap.current
    }

    override fun destroyFor(player: Player) {
        val snap = opened.remove(player.uniqueId)
    }

    override fun getViewOf(player: Player): View {
        val snap = opened[player.uniqueId] ?: throw Exception("There is no gui snapshot for ${player.name}")
        return snap.current
    }

    override fun onCloseView(player: Player): Boolean {
        val snap = opened[player.uniqueId] ?: throw Exception("There is no gui snapshot for ${player.name}")
        if(snap.changingPage) {
            snap.changingPage = false
            return false
        }
        return true
    }

}