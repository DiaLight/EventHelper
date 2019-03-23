package dialight.eventhelper

import com.google.inject.Inject
import dialight.eventhelper.gui.EventHelperGui
import dialight.guilib.GuiPlugin
import dialight.guilib.View
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

@Plugin(id = "eventhelper")
class EventHelperPlugin @Inject constructor(
    val container: PluginContainer,
    val logger: Logger,
    val pluginManager: PluginManager
) {

    lateinit var toollib: ToolPlugin
        private set
    lateinit var guilib: GuiPlugin
        private set
    lateinit var maingui: EventHelperGui
        private set

    val toolItemRegistry = observableMapOf<String, View.Item>()

    fun registerToolItem(id: String, item: View.Item) {
        toolItemRegistry[id] = item
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getPluginInstance(id: String): T? {
        val oplugin = pluginManager.getPlugin(id)
        if(!oplugin.isPresent) return null
        val plugin = oplugin.get()
        val oinstance = plugin.instance
        if(!oinstance.isPresent) return null
        return oinstance.get() as T
    }

    @Listener
    fun onConstruction(event: GameConstructionEvent) {
        toollib = getPluginInstance("toollib")!!
        guilib = getPluginInstance("guilib")!!
    }

    @Listener
    fun onServerStart(event: GameStartedServerEvent) {
        this.maingui = EventHelperGui(this)
        toollib.registerTool(EventHelperTool(this))
        registerCommands()
        logger.info("EventHelper v${container.version.orElse("null")} has been Enabled")
    }

    @Listener
    fun reload(event: GameReloadEvent) {
        logger.info("reload")
    }
}


