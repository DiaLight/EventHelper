package dialight.maingui

import org.spongepowered.api.Sponge
import org.spongepowered.api.command.CommandCallable
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.spec.CommandExecutor
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.text.Text
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.SpongeEventFactory
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import jekarus.colorizer.Text_colorized
import java.util.*


fun MainGuiPlugin.registerCommands() {
    val ehCmd = CommandSpec.builder()
        .description(Text.of("Инструмент, облегчающий работу ивент-мастеров"))
        .permission("eventhelper.command.eh")
        .executor(EventHelperCommand(this))
        .child(ReloadCommand(), "reload")
        .child(ItemCommand(this), "item")
        .child(GuiCommand(this), "gui")
        .extendedDescription(
            Text_colorized(
                "|y|---------|w| Справка по EventHelper |y|----------------",
                "|gr|Описание действий предметов описано у них в свойствах.",
                "|gr|Просто наведи курсор мыши на предмет."
            )
        )
        .build()

    Sponge.getCommandManager().register(this, ehCmd, "eventhelper", "eh")

}

class ReloadCommand : CommandCallable_stub() {

    override fun process(src: CommandSource, arguments: String): CommandResult {
        Sponge.getEventManager().post(SpongeEventFactory.createGameReloadEvent(Sponge.getCauseStackManager().getCurrentCause()))
        return CommandResult.success()
    }

}

class ItemCommand(val plugin: MainGuiPlugin) : CommandCallable_stub() {

    override fun process(src: CommandSource, arguments: String): CommandResult {
        if (src is Player) {
            plugin.toollib.giveTool(src, MainGuiTool.ID)
        }
        return CommandResult.success()
    }

}

class GuiCommand(val plugin: MainGuiPlugin) : CommandCallable_stub() {

    override fun process(src: CommandSource, arguments: String): CommandResult {
        if (src is Player) {
            plugin.guilib.openGui(src, plugin.maingui)
        }
        return CommandResult.success()
    }

}

class EventHelperCommand(val plugin: MainGuiPlugin) : CommandExecutor {

    override fun execute(src: CommandSource, args: CommandContext): CommandResult {
        if (src is Player) {
            plugin.guilib.openGui(src, plugin.maingui)
        }
        return CommandResult.success()
    }

}

@Deprecated("Temporary solution")
abstract class CommandCallable_stub : CommandCallable {
    override fun getUsage(source: CommandSource): Text {
        return Text.of("Unimplemented")
    }

    override fun testPermission(source: CommandSource): Boolean {
        return true
    }

    override fun getShortDescription(source: CommandSource): Optional<Text> {
        return Optional.empty()
    }

    override fun getSuggestions(
        source: CommandSource,
        arguments: String,
        targetPosition: Location<World>?
    ): MutableList<String> {
        return mutableListOf<String>()
    }

    override fun getHelp(source: CommandSource): Optional<Text> {
        return Optional.empty()
    }
}
