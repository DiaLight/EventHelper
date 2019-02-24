package ru.dialight.eventhelper

import com.google.inject.Inject
import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.command.CommandCallable
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.spec.CommandExecutor
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.text.Text
import org.spongepowered.api.command.source.CommandBlockSource
import org.spongepowered.api.command.source.ConsoleSource
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.format.TextColors
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World
import ru.dialight.eventhelper.utils.Colorizer
import java.util.*

// TODO: figure it out
//source.sendMessageColorized("|y|---------|w| Справка по EventHelper |y|----------------")
//source.sendMessageColorized("|gr|Описание действий предметов описано у них в свойствах.")
//source.sendMessageColorized("|gr|Просто наведи курсор мыши на предмет.")

fun EventHelper.registerCommands() {
    val ehCmd = CommandSpec.builder()
        .description(Text.of("Инструмент, облегчающий работу ивент-мастеров"))
        .permission("eventhelper.command.eh")
        .executor(EventHelperCommand(this))
        .extendedDescription(Text_colorized(
            "|y|---------|w| Справка по EventHelper |y|----------------",
            "|gr|Описание действий предметов описано у них в свойствах.",
            "|gr|Просто наведи курсор мыши на предмет."
        ))
        .build()

    Sponge.getCommandManager().register(this, ehCmd, "eventhelper", "eh")

}

fun Text_colorized(msg: String) = Text.of(Colorizer.apply(msg))
fun Text_colorized(vararg msgs: String) = Text.of(Colorizer.apply(*msgs))
fun Text_colorizedList(vararg msgs: String) = msgs.map { Text_colorized(it) }

class EventHelperCommand(val plugin: EventHelper) : CommandExecutor {

    override fun execute(src: CommandSource, args: CommandContext): CommandResult {
        if (src is Player) {
            plugin.gui.openFor(src)
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
