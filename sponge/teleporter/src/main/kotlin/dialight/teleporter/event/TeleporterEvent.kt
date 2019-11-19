package dialight.teleporter.event

import dialight.teleporter.Teleporter
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.cause.Cause
import org.spongepowered.api.event.cause.EventContext
import org.spongepowered.api.event.cause.EventContextKey
import org.spongepowered.api.event.entity.living.humanoid.player.TargetPlayerEvent
import org.spongepowered.api.event.impl.AbstractEvent
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.util.generator.dummy.DummyObjectProvider

class TeleporterEvent(
    private val player: Player,
    val result: Teleporter.Result
) : AbstractEvent(), TargetPlayerEvent {

    companion object {
        val SELECT_RESULT = createFor<Teleporter.Result>("SELECT_RESULT")

        @Suppress("UNCHECKED_CAST")
        private fun <T> createFor(id: String): EventContextKey<T> {
            return DummyObjectProvider.createFor(EventContextKey::class.java, id) as EventContextKey<T>
        }
    }

    private val _cause: Cause = Cause.builder()
        .append(player)
        .append(result)
        .build(
            EventContext.builder()
                .add(SELECT_RESULT, result)
                .build()
        )

    override fun getCause(): Cause = this._cause
    override fun getTargetEntity() = player

}