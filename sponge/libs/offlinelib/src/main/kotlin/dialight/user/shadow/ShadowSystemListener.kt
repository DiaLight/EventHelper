package dialight.user.shadow

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.event.network.ClientConnectionEvent
import org.spongepowered.api.scheduler.Task


class ShadowSystemListener(val system: ShadowSystem) {

    @Listener
    fun onLeave(e: ClientConnectionEvent.Disconnect, @First player: Player) {
        val shadow = system.shadows[player.uniqueId] ?: return
        val loc = player.location
        val rot = player.rotation
        Task.builder().execute { task ->
            val entity = system.spawnShadow(shadow)
            entity.setLocationAndRotation(loc, rot)
        }.submit(system.plugin)
    }

    @Listener
    fun onJoin(e: ClientConnectionEvent.Join, @First player: Player) {
        val shadow = system.shadows[player.uniqueId] ?: return
        shadow.entity?.let { entity ->
            player.setLocationAndRotation(entity.location, entity.rotation)
            entity.remove()
            shadow.entity = null
        }
    }

}