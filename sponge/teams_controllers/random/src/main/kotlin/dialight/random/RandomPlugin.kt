package dialight.random

import com.google.inject.Inject
import dialight.maingui.MainGuiPlugin
import dialight.extensions.getPluginInstance
import dialight.guilib.GuiPlugin
import dialight.modulelib.ModulePlugin
import dialight.random.gui.RandomItem
import dialight.teams.TeamsPlugin
import org.slf4j.Logger
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameConstructionEvent
import org.spongepowered.api.event.game.state.GameStartedServerEvent
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.plugin.PluginManager

@Plugin(id = "random")
class RandomPlugin @Inject constructor(
    val container: PluginContainer,
    val logger: Logger,
    val pluginManager: PluginManager
) {

    lateinit var modulelib: ModulePlugin
        private set
    lateinit var teams: TeamsPlugin
        private set
    var guilib: GuiPlugin? = null
        private set
    var eh: MainGuiPlugin? = null
        private set

    val module = RandomModule(this)

    @Listener
    fun onConstruction(event: GameConstructionEvent) {
        modulelib = pluginManager.getPluginInstance("modulelib")!!
        guilib = pluginManager.getPluginInstance("guilib")
        teams = pluginManager.getPluginInstance("teams")!!
        eh = pluginManager.getPluginInstance("maingui")
    }

    @Listener
    fun onServerStart(event: GameStartedServerEvent) {
        modulelib.moduleregistry[module.id] = module

        eh?.also {
            it.registerModuleItem(module.id, RandomItem(this))
        }

        logger.info("${container.name} v${container.version.orElse("null")} has been Enabled")
    }
}