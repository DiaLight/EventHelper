package dialight.teams.gui

import dialight.extensions.ItemStackBuilderEx
import dialight.extensions.dyeColor
import dialight.extensions.textColor
import dialight.guilib.View
import dialight.guilib.events.ItemClickEvent
import dialight.guilib.simple.SimpleGui
import dialight.guilib.simple.SimpleItem
import dialight.guilib.snapshot.SequentialPageBuilder
import dialight.teams.Server_getScoreboard
import dialight.teams.TeamsMessages
import dialight.teams.TeamsPlugin
import jekarus.colorizer.Text_colorized
import jekarus.colorizer.Text_colorizedList
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.type.DyeColors
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.scoreboard.Team
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors

class AddTeamGui(val plugin: TeamsPlugin) : SimpleGui(plugin.guilib!!, Text_colorized("Создание команды"), 9, 6) {

    companion object {
        val COLORS = arrayOf(
            TextColors.WHITE,
            TextColors.BLACK,
            TextColors.DARK_GRAY,
            TextColors.GRAY,
            TextColors.DARK_RED,
            TextColors.RED,
            TextColors.GREEN,
            TextColors.DARK_GREEN,
            TextColors.BLUE,
            TextColors.DARK_BLUE,
            TextColors.YELLOW,
            TextColors.GOLD,
            TextColors.AQUA,
            TextColors.DARK_AQUA,
            TextColors.LIGHT_PURPLE,
            TextColors.DARK_PURPLE
        )
    }

    init {
        val items = arrayListOf<View.Item>()
        val scoreboard = Server_getScoreboard()
        val coloredTeams = scoreboard.teams.map { it.color }
        for(color in COLORS) {
            if(color in coloredTeams) continue
            items.add(SimpleItem(ItemStackBuilderEx(ItemTypes.WOOL)
                .also {
                    offer(Keys.DYE_COLOR, color.dyeColor)
                }
                .name(Text_colorized(color.name))
                .lore(Text_colorizedList(
                    "|g|ЛКМ|y|: создать команду"
                ))
                .build()) {
                when(it.type) {
                    ItemClickEvent.Type.LEFT -> {
                        val team = Team.builder()
                            .name("${color.name}_team")
                            .color(color)
                            .prefix(Text.of(color))
                            .build()
                        scoreboard.registerTeam(team)
                        it.player.sendMessage(TeamsMessages.addTeam(team))

                        guiplugin.guistory.openPrev(it.player)
                    }
                    ItemClickEvent.Type.RIGHT -> {

                    }
                }
            })
        }
        val pages = SequentialPageBuilder(width, height - 1, width, items).toList()
        for((index, item) in pages.first()) {
            this[index] = item
        }
    }


}