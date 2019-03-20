package dialight.teams

import com.google.inject.Inject
import dialight.ObservableObject
import dialight.eventhelper.EventHelperPlugin
import dialight.extensions.getOrNull
import dialight.extensions.getPluginInstance
import dialight.guilib.GuiPlugin
import dialight.teams.gui.TeamsGui
import dialight.teams.gui.TeamsItem
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
import org.spongepowered.api.scoreboard.Team
import java.util.*
import kotlin.properties.ObservableProperty

@Plugin(id = "teams")
class TeamsPlugin @Inject constructor(
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
    var teleporter: TeleporterPlugin? = null
        private set

    val tool = TeamsTool(this)
    lateinit var teamsgui: TeamsGui
        private set

    var selected = ObservableObject<Team?>(null)

    @Listener
    fun onConstruction(event: GameConstructionEvent) {
        toollib = pluginManager.getPluginInstance("toollib")!!
        guilib = pluginManager.getPluginInstance("guilib")
        eh = pluginManager.getPluginInstance("eventhelper")
        teleporter = pluginManager.getPluginInstance("teleporter")
    }

    @Listener
    fun onServerStart(event: GameStartedServerEvent) {
        toollib.toolregistry[tool.id] = tool

        eh?.also {
            it.registerToolItem(tool.id, TeamsItem(this))
        }
        guilib?.also {
            teamsgui = TeamsGui(this)
        }

//        Sponge.getServer().serverScoreboard.getOrNull()?.let {
//            for(team in it.teams) {
//                teams += Team(team.name, team.color)
//            }
//        }
        Sponge.getEventManager().registerListeners(this, teamsgui)
        Sponge.getEventManager().registerListeners(this, TeamsListener(this))


        logger.info("TeamsTool v${container.version.orElse("null")} has been Enabled")
    }


}

fun Server_getScoreboard() = Sponge.getServer().serverScoreboard.get()

