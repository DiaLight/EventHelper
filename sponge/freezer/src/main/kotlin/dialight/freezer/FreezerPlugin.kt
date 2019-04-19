package dialight.freezer

import com.google.inject.Inject
import dialight.eventhelper.EventHelperPlugin
import dialight.extensions.getPluginInstance
import dialight.freezer.gui.FreezerGui
import dialight.freezer.gui.FreezerItem
import dialight.guilib.GuiPlugin
import dialight.teleporter.TeleporterPlugin
import dialight.toollib.ToolPlugin
import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameConstructionEvent
import org.spongepowered.api.event.game.state.GameStartedServerEvent
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.plugin.PluginManager
import java.util.*

@Plugin(id = "freezer")
class FreezerPlugin @Inject constructor(
    val container: PluginContainer,
    val logger: Logger,
    val pluginManager: PluginManager
) {

    lateinit var toollib: ToolPlugin
        private set
    lateinit var teleporter: TeleporterPlugin
        private set
    var guilib: GuiPlugin? = null
        private set
    var eh: EventHelperPlugin? = null
        private set

    val freezer = Freezer()

    val tool = FreezerTool(this)
    lateinit var freezergui: FreezerGui
        private set

    @Listener
    fun onConstruction(event: GameConstructionEvent) {
        toollib = pluginManager.getPluginInstance("toollib")!!
        teleporter = pluginManager.getPluginInstance("teleporter")!!
        guilib = pluginManager.getPluginInstance("guilib")
        eh = pluginManager.getPluginInstance("eventhelper")
    }

    @Listener
    fun onServerStart(event: GameStartedServerEvent) {
        toollib.toolregistry[tool.id] = tool

        eh?.also {
            it.registerToolItem(tool.id, FreezerItem(this))
        }
        guilib?.also {
            freezergui = FreezerGui(this)
            Sponge.getEventManager().registerListeners(this, freezergui)
        }

        logger.info("${container.name} v${container.version.orElse("null")} has been Enabled")
        Sponge.getEventManager().registerListeners(this, FreezerListener(this))
    }

    operator fun get(id: UUID): FrozenPlayers.Frozen? = freezer.frozen.map[id]

}