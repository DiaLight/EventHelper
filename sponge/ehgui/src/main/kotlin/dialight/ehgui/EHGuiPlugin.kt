package dialight.ehgui

import com.google.inject.Inject
import dialight.ehgui.gui.EHGui
import dialight.ehgui.gui.EHGuiItem
import dialight.extensions.getPluginInstance
import dialight.guilib.GuiPlugin
import dialight.guilib.View
import dialight.modulelib.ModulePlugin
import dialight.observable.map.observableMapOf
import dialight.toollib.ToolPlugin
import org.slf4j.Logger
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.GameReloadEvent
import org.spongepowered.api.event.game.state.GameConstructionEvent
import org.spongepowered.api.event.game.state.GameStartedServerEvent
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.plugin.PluginManager

@Plugin(id = "ehgui")
class EHGuiPlugin @Inject constructor(
    val container: PluginContainer,
    val logger: Logger,
    val pluginManager: PluginManager
) {

    lateinit var toollib: ToolPlugin
        private set
    lateinit var modulelib: ModulePlugin
        private set
    lateinit var guilib: GuiPlugin
        private set
    lateinit var maingui: EHGui
        private set

    val toolItemRegistry = observableMapOf<String, View.Item>()
    val moduleItemRegistry = observableMapOf<String, View.Item>()

    fun registerToolItem(id: String, item: View.Item) {
        toolItemRegistry[id] = item
    }

    fun registerModuleItem(id: String, item: View.Item) {
        moduleItemRegistry[id] = item
    }

    val tool = EHGuiTool(this)

    @Listener
    fun onConstruction(event: GameConstructionEvent) {
        toollib = pluginManager.getPluginInstance("toollib")!!
        modulelib = pluginManager.getPluginInstance("modulelib")!!
        guilib = pluginManager.getPluginInstance("guilib")!!
    }

    @Listener
    fun onServerStart(event: GameStartedServerEvent) {
        this.maingui = EHGui(this)
        toollib.registerTool(tool)
        registerToolItem(tool.id, EHGuiItem(this))
        registerCommands()
        logger.info("${container.name} v${container.version.orElse("null")} has been Enabled")
    }

    @Listener
    fun reload(event: GameReloadEvent) {
        logger.info("reload")
    }
}


