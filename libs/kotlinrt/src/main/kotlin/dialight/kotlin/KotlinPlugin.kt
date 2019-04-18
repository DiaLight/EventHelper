package dialight.kotlin

import com.google.inject.Inject
import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameStartedServerEvent
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer

@Plugin(id = "kotlinrt")
class KotlinPlugin @Inject constructor(
    val container: PluginContainer,
    val logger: Logger
) {


    @Listener
    fun onServerStart(event: GameStartedServerEvent) {
        logger.info("${container.name} v${container.version.orElse("null")} has been Enabled")
    }

}