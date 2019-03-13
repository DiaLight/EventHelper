package dialight.teleporter

import com.google.inject.Inject
import dialight.eventhelper.EventHelperPlugin
import dialight.extensions.getPluginInstance
import dialight.guilib.GuiPlugin
import dialight.teleporter.offline.OfflineTeleporter
import dialight.teleporter.gui.TaggerGui
import dialight.teleporter.gui.TeleporterItem
import dialight.toollib.ToolPlugin
import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameConstructionEvent
import org.spongepowered.api.event.game.state.GameStartedServerEvent
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.plugin.PluginManager

@Plugin(id = "teleporter")
class TeleporterPlugin @Inject constructor(
    val container: PluginContainer,
    val logger: Logger,
    val pluginManager: PluginManager
) {

    lateinit var toollib: ToolPlugin
        private set
    var guilib: GuiPlugin? = null
        private set
    var eh: EventHelperPlugin? = null
        private set

    val tool = TeleporterTool(this)
    lateinit var taggergui: TaggerGui
        private set

    val teleporter = Teleporter()
    val offlineteleporter = OfflineTeleporter(this)

    @Listener
    fun onConstruction(event: GameConstructionEvent) {
        toollib = pluginManager.getPluginInstance("toollib")!!
        guilib = pluginManager.getPluginInstance("guilib")
        eh = pluginManager.getPluginInstance("eventhelper")
    }

    @Listener
    fun onServerStart(event: GameStartedServerEvent) {
        toollib.toolregistry[tool.id] = tool

        eh?.also {
            it.registerToolItem(tool.id, TeleporterItem(this))
        }
        guilib?.also {
            taggergui = TaggerGui(this)
        }

        Sponge.getEventManager().registerListeners(this, offlineteleporter)
        logger.info("Teleporter v${container.version.orElse("null")} has been Enabled")
    }

}