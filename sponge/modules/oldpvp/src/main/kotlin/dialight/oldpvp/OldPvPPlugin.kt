package dialight.oldpvp

import com.google.inject.Inject
import dialight.ehgui.EHGuiPlugin
import dialight.extensions.getPluginInstance
import dialight.guilib.GuiPlugin
import dialight.modulelib.ModulePlugin
import dialight.oldpvp.gui.OldPvPItem
import org.slf4j.Logger
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameConstructionEvent
import org.spongepowered.api.event.game.state.GameStartedServerEvent
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.plugin.PluginManager

@Plugin(id = "oldpvp")
class OldPvPPlugin @Inject constructor(
    val container: PluginContainer,
    val logger: Logger,
    val pluginManager: PluginManager
) {

    lateinit var modulelib: ModulePlugin
        private set
    var guilib: GuiPlugin? = null
        private set
    var eh: EHGuiPlugin? = null
        private set

    val module = OldPvPModule(this)
    val listener = OldPvPListener(this)

    @Listener
    fun onConstruction(event: GameConstructionEvent) {
        modulelib = pluginManager.getPluginInstance("modulelib")!!
        guilib = pluginManager.getPluginInstance("guilib")
        eh = pluginManager.getPluginInstance("ehgui")
    }

    @Listener
    fun onServerStart(event: GameStartedServerEvent) {
        modulelib.moduleregistry[module.id] = module

        eh?.also {
            it.registerModuleItem(module.id, OldPvPItem(this))
        }

        logger.info("${container.name} v${container.version.orElse("null")} has been Enabled")
    }
}