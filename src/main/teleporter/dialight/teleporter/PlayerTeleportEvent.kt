package dialight.teleporter

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.cause.Cause
import org.spongepowered.api.event.entity.living.humanoid.player.TargetPlayerEvent
import org.spongepowered.api.event.impl.AbstractEvent
import org.spongepowered.api.Sponge
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.cause.EventContext
import org.spongepowered.api.event.filter.cause.First
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.text.Text
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World

open class PlayerTeleportEvent(
    private val player: Player, private val cause: Cause,
    private val from: Location<World>, private val to: Location<World>
) : AbstractEvent(), TargetPlayerEvent {

    override fun getCause(): Cause = this.cause
    override fun getTargetEntity(): Player = this.player

    fun getFrom() = from
    fun getTo() = to

    class ByPlayer(
        player: Player, private val byPlayer: Player, cause: Cause,
        from: Location<World>, to: Location<World>
    ) : PlayerTeleportEvent(player, cause, from, to) {

        fun getByPlayer() = byPlayer

    }

    class ByPlugin(
        player: Player, private val byPlugin: PluginContainer, cause: Cause,
        from: Location<World>, to: Location<World>
    ) : PlayerTeleportEvent(player, cause, from, to) {

        fun getByPlugin() = byPlugin

    }

}
