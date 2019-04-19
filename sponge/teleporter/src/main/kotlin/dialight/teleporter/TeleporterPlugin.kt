package dialight.teleporter

import com.google.inject.Inject
import dialight.eventhelper.EventHelperPlugin
import dialight.extensions.Server_getPlayer
import dialight.extensions.getPluginInstance
import dialight.extensions.teleportSafe
import dialight.guilib.GuiPlugin
import dialight.teleporter.event.PlayerTeleportEvent
import dialight.teleporter.gui.TeleporterGui
import dialight.teleporter.gui.TeleporterItem
import dialight.toollib.ToolPlugin
import dialight.user.OfflinePlugin
import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameConstructionEvent
import org.spongepowered.api.event.game.state.GameStartedServerEvent
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.plugin.PluginManager
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import java.util.*

@Plugin(id = "teleporter")
class TeleporterPlugin @Inject constructor(
    val container: PluginContainer,
    val logger: Logger,
    val pluginManager: PluginManager
) {

    lateinit var toollib: ToolPlugin
        private set
    lateinit var offlinelib: OfflinePlugin
        private set
    var guilib: GuiPlugin? = null
        private set
    var eh: EventHelperPlugin? = null
        private set

    val tool = TeleporterTool(this)
    lateinit var teleportergui: TeleporterGui
        private set

    val teleporter = Teleporter()

    @Listener
    fun onConstruction(event: GameConstructionEvent) {
        toollib = pluginManager.getPluginInstance("toollib")!!
        offlinelib = pluginManager.getPluginInstance("offlinelib")!!
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
            teleportergui = TeleporterGui(this)
            Sponge.getEventManager().registerListeners(this, teleportergui)
        }

        logger.info("${container.name} v${container.version.orElse("null")} has been Enabled")
    }

    fun teleport(plugin: Any, uuid: UUID, loc: Location<World>) {
        val pluginContainer = Sponge.getPluginManager().fromInstance(plugin).get()
        val player = Server_getPlayer(uuid)
        if(player != null) {
            Sponge.getEventManager().post(PlayerTeleportEvent.ByPlugin(player, pluginContainer, player.location, loc))
            player.teleportSafe(loc)
            player.sendMessage(TeleporterMessages.YouHBTp(pluginContainer.name))
            return
        }
        offlinelib.teleport(uuid, loc)
    }

}