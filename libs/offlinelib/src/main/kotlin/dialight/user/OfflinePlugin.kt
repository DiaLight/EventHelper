package dialight.user

import com.google.inject.Inject
import dialight.extensions.Server_getPlayer
import dialight.extensions.Server_getUser
import dialight.extensions.teleportSafe
import dialight.user.offline.OfflineTeleporter
import dialight.user.shadow.ShadowSystem
import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameConstructionEvent
import org.spongepowered.api.event.game.state.GameStartedServerEvent
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import java.util.*

@Plugin(id = "offlinelib")
class OfflinePlugin @Inject constructor(
    val container: PluginContainer,
    val logger: Logger
) {

    val shadow = ShadowSystem(container, logger)

    val onjoin = HashMap<UUID, (Player) -> Unit>()
    val offlineteleporter = OfflineTeleporter(this)

    @Listener
    fun onConstruction(event: GameConstructionEvent) {
        Sponge.getEventManager().registerListeners(this, shadow)
        Sponge.getEventManager().registerListeners(this, offlineteleporter)
    }
    @Listener
    fun onServerStart(event: GameStartedServerEvent) {
        logger.info("${container.name} v${container.version.orElse("null")} has been Enabled")
    }

    fun teleport(uuid: UUID, loc: Location<World>) {
        val user = Server_getUser(uuid)
        if(user == null || user.isOnline) return
        // TODO: этот код может работать не стабильно
        val shadow = shadow.getShadow(user)
        if(shadow != null) {
            shadow.entity?.location = loc
        } else {
            onjoin[user.uniqueId] = { player ->
                player.teleportSafe(loc)
            }
        }
    }

}