package dialight.teams.gui

import dialight.extensions.ItemStackBuilderEx
import dialight.extensions.closeInventoryLater
import dialight.guilib.View
import dialight.guilib.events.ItemClickEvent
import dialight.teams.TeamsTool
import dialight.teams.TeamsPlugin
import jekarus.colorizer.Text_colorized
import jekarus.colorizer.Text_colorizedList
import org.spongepowered.api.item.ItemTypes


class TeamsItem(val plugin: TeamsPlugin) : View.Item {


    override val item = ItemStackBuilderEx(ItemTypes.BLAZE_ROD)
        .name(Text_colorized("|a|Распределитель команд"))
        .lore(
            Text_colorizedList(
                "|g|ЛКМ|y|: получить инструмент",
                "|g|ПКМ|y|: открыть распределитель",
                "",
                "|g|Плагин: |y|Распределитель команд",
                "|g|Версия: |y|v" + plugin.container.version.orElse("null")
            )
        )
        .build()

    override fun onClick(event: ItemClickEvent) {
        val player = event.player
        when (event.type) {
            ItemClickEvent.Type.LEFT, ItemClickEvent.Type.SHIFT_LEFT -> {
                player.closeInventoryLater(plugin)
                plugin.toollib.giveTool(player, TeamsTool.ID)
            }
            ItemClickEvent.Type.RIGHT -> {
                plugin.guilib?.openGui(player, plugin.teamsgui)
            }
        }
    }

}