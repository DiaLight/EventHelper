package ru.dialight.eventhelper

import com.google.inject.Inject
import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameStartedServerEvent
import org.spongepowered.api.event.game.state.GameStoppedServerEvent
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer
import ru.dialight.eventhelper.gui.GuiListener
import ru.dialight.eventhelper.gui.MainGui
import ru.dialight.eventhelper.teleporter.TeleporterTool
import ru.dialight.eventhelper.tool.ToolRegistry

@Plugin(
    id = "eventhelper",
    name = "Eventhelper",
    version = "2.0",
    authors = ["DiaLight"],
    description = "Useful tool to help event masters with theirs job"
)
class EventHelper @Inject constructor(
    val container: PluginContainer,
    val logger: Logger
) {

    val toolregistry = ToolRegistry()
    lateinit var gui: MainGui
        private set

    init {
        toolregistry.registerTool(TeleporterTool())
    }

    @Listener
    fun onServerStart(event: GameStartedServerEvent) {
        this.gui = MainGui(this)
        registerCommands()
        Sponge.getEventManager().registerListeners(this, GuiListener(this))
        logger.info("EventHelper v${container.version.orElse("null")} has been Enabled")
    }

}