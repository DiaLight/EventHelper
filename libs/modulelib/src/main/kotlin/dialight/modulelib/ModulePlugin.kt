package dialight.modulelib

import com.google.inject.Inject
import dialight.observable.map.observableMapOf
import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameStartedServerEvent
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer

@Plugin(id = "modulelib")
class ModulePlugin @Inject constructor(
    val container: PluginContainer,
    val logger: Logger
) {

    val moduleregistry = observableMapOf<String, Module>()

    fun registerModule(tool: Module) {
        moduleregistry[tool.id] = tool
    }
    fun getModule(id: String) = moduleregistry[id]


    @Listener
    fun onServerStart(event: GameStartedServerEvent) {
        logger.info("ModuleLib v${container.version.orElse("null")} has been Enabled")
    }

}