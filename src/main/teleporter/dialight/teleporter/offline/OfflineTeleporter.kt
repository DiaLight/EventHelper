package dialight.teleporter.offline

import dialight.teleporter.event.PlayerTeleportEvent
import dialight.teleporter.TeleporterPlugin
import dialight.teleporter.teleportSafe
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.network.ClientConnectionEvent
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import java.util.*

class OfflineTeleporter(val plugin: TeleporterPlugin) {

    val registry = HashMap<UUID, Location<World>>()

    @Listener
    fun onJoin(event: ClientConnectionEvent.Join, @First player: Player) {
        val loc = registry.remove(player.uniqueId) ?: return
        Sponge.getEventManager().post(PlayerTeleportEvent.ByPlugin(player, plugin.container, player.location, loc))
        player.teleportSafe(loc)
    }

    fun teleport(user: User, loc: Location<World>) {
        registry[user.uniqueId] = loc
    }

}