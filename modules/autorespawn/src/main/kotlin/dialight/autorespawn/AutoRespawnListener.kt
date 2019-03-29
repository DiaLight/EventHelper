package dialight.autorespawn

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.entity.DestructEntityEvent
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.scheduler.Task


class AutoRespawnListener(val plugin: AutoRespawnPlugin) {

    @Listener
    fun onDeath(e: DestructEntityEvent.Death, @First player: Player) {
        if(!plugin.moduule.enabled) return
        Task.builder().execute { task ->
            player.respawnPlayer()
        }.submit(plugin)
    }

}