package dialight.teleporter.event

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.cause.Cause
import org.spongepowered.api.event.entity.living.humanoid.player.TargetPlayerEvent
import org.spongepowered.api.event.impl.AbstractEvent
import org.spongepowered.api.event.cause.EventContext
import org.spongepowered.api.event.cause.EventContextKey
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.util.generator.dummy.DummyObjectProvider
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World

abstract class PlayerTeleportEvent(
    private val player: Player,
    private val from: Location<World>,
    private val to: Location<World>
) : AbstractEvent(), TargetPlayerEvent {

    companion object {
        val BY_PLAYER = createFor<Player>("BY_PLAYER")
        val BY_PLUGIN = createFor<PluginContainer>("BY_PLUGIN")

        @Suppress("UNCHECKED_CAST")
        private fun <T> createFor(id: String): EventContextKey<T> {
            return DummyObjectProvider.createFor(EventContextKey::class.java, id) as EventContextKey<T>
        }
    }

    protected abstract val _cause: Cause

    override fun getCause(): Cause = this._cause
    override fun getTargetEntity(): Player = this.player

    fun getFrom() = from
    fun getTo() = to

    class ByPlayer(
        player: Player, private val byPlayer: Player,
        from: Location<World>, to: Location<World>
    ) : PlayerTeleportEvent(player, from, to) {

        override val _cause = Cause.builder()
            .append(player)
            .build(
                EventContext.builder()
                    .add(BY_PLAYER, byPlayer)
                    .build()
            )

        fun getByPlayer() = byPlayer

    }

    class ByPlugin(player: Player, private val byPlugin: PluginContainer, from: Location<World>, to: Location<World>) :
        PlayerTeleportEvent(player, from, to) {

        override val _cause = Cause.builder()
            .append(player)
            .build(
                EventContext.builder()
                    .add(BY_PLUGIN, byPlugin)
                    .build()
            )

        fun getByPlugin() = byPlugin

    }

}
