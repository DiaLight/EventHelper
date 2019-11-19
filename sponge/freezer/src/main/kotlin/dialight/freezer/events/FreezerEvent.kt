package dialight.freezer.events

import dialight.freezer.Freezer
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.cause.Cause
import org.spongepowered.api.event.cause.EventContext
import org.spongepowered.api.event.cause.EventContextKey
import org.spongepowered.api.event.entity.living.humanoid.player.TargetPlayerEvent
import org.spongepowered.api.event.impl.AbstractEvent
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.util.generator.dummy.DummyObjectProvider

abstract class FreezerEvent(
    val result: Freezer.Result
) : AbstractEvent() {

    companion object {
        val FREEZER_RESULT = createFor<Freezer.Result>("FREEZER_RESULT")

        @Suppress("UNCHECKED_CAST")
        private fun <T> createFor(id: String): EventContextKey<T> {
            return DummyObjectProvider.createFor(EventContextKey::class.java, id) as EventContextKey<T>
        }
    }

    protected abstract val _cause: Cause

    override fun getCause(): Cause = this._cause


    class ByPlayer(
        private val player: Player,
        result: Freezer.Result
    ) : FreezerEvent(result), TargetPlayerEvent {

        override val _cause: Cause = Cause.builder()
            .append(player)
            .append(result)
            .build(
                EventContext.builder()
                    .add(FreezerEvent.FREEZER_RESULT, result)
                    .build()
            )

        override fun getTargetEntity() = player

    }

    class ByPlugin(
        val plugin: PluginContainer,
        result: Freezer.Result
    ) : FreezerEvent(result) {

        override val _cause: Cause = Cause.builder()
            .append(plugin)
            .append(result)
            .build(
                EventContext.builder()
                    .add(FreezerEvent.FREEZER_RESULT, result)
                    .build()
            )

    }

}