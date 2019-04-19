package dialight.guilib.snapshot

import dialight.extensions.getOrNull
import dialight.guilib.Gui
import dialight.guilib.View
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.property.Identifiable
import java.util.*

abstract class SnapshotGui<T : Snapshot<*>> : Gui {

    val opened = hashMapOf<UUID, T>()

    val id = Identifiable()

    override fun ownerOf(
        player: Player,
        inv: Inventory
    ): Boolean {
        val prop = inv.getInventoryProperty(Identifiable::class.java).getOrNull() ?: return false
        return id.value!! == prop.value!!
    }

    abstract fun getSnapshot(player: Player): T

    override fun getView(player: Player): View {
        val snap = getSnapshot(player)
        opened[player.uniqueId] = snap
        return snap.current(player)
    }

    override fun destroyFor(player: Player) {
        val snap = opened.remove(player.uniqueId)
    }

    override fun getViewOf(player: Player): View {
        val snap = opened[player.uniqueId] ?: throw Exception("There is no gui snapshot for ${player.name}")
        return snap.current(player)
    }

    override fun onCloseView(player: Player): Boolean {
        val snap = opened[player.uniqueId] ?: throw Exception("There is no gui snapshot for ${player.name}")
        val state = snap.state(player)
        if(state.changingPage) {
            state.changingPage = false
            return false
        }
        return true
    }

}