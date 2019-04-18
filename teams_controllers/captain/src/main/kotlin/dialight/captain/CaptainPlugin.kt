package dialight.captain

import com.google.inject.Inject
import dialight.captain.gui.CaptainGui
import dialight.captain.gui.CaptainItem
import dialight.captain.system.CaptainSystem
import dialight.eventhelper.EventHelperPlugin
import dialight.extensions.getPluginInstance
import dialight.freezer.FreezerPlugin
import dialight.guilib.GuiPlugin
import dialight.modulelib.ModulePlugin
import dialight.teams.TeamsPlugin
import dialight.teleporter.TeleporterPlugin
import dialight.toollib.ToolPlugin
import dialight.user.OfflinePlugin
import org.slf4j.Logger
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameConstructionEvent
import org.spongepowered.api.event.game.state.GameStartedServerEvent
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.plugin.PluginManager

@Plugin(id = "captain")
class CaptainPlugin @Inject constructor(
    val container: PluginContainer,
    val logger: Logger,
    val pluginManager: PluginManager
) {

    lateinit var modulelib: ModulePlugin
        private set
    lateinit var toollib: ToolPlugin
        private set
    lateinit var teams: TeamsPlugin
        private set
    lateinit var offlinelib: OfflinePlugin
        private set
    lateinit var teleporter: TeleporterPlugin
        private set
    lateinit var freezer: FreezerPlugin
        private set
    var guilib: GuiPlugin? = null
        private set
    var eh: EventHelperPlugin? = null
        private set

    val module = CaptainModule(this)
    val system = CaptainSystem(this)
    val tool = CaptainTool(this)

    lateinit var captainGui: CaptainGui
        private set

    @Listener
    fun onConstruction(event: GameConstructionEvent) {
        modulelib = pluginManager.getPluginInstance("modulelib")!!
        toollib = pluginManager.getPluginInstance("toollib")!!
        guilib = pluginManager.getPluginInstance("guilib")
        teams = pluginManager.getPluginInstance("teams")!!
        offlinelib = pluginManager.getPluginInstance("offlinelib")!!
        teleporter = pluginManager.getPluginInstance("teleporter")!!
        freezer = pluginManager.getPluginInstance("freezer")!!
        eh = pluginManager.getPluginInstance("eventhelper")
    }

    @Listener
    fun onServerStart(event: GameStartedServerEvent) {
        modulelib.moduleregistry[module.id] = module
        toollib.registerTool(tool)

        eh?.also {
            it.registerModuleItem(module.id, CaptainItem(this))
        }
        guilib?.also {
            captainGui = CaptainGui(this)
        }

        logger.info("${container.name} v${container.version.orElse("null")} has been Enabled")
    }
}