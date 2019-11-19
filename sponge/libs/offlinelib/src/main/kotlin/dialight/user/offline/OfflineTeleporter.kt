package dialight.user.offline

import dialight.user.OfflinePlugin
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.network.ClientConnectionEvent

class OfflineTeleporter(val plugin: OfflinePlugin) {

    @Listener
    fun onJoin(event: ClientConnectionEvent.Join, @First player: Player) {
        val op = plugin.onjoin.remove(player.uniqueId) ?: return
        op(player)
    }

}