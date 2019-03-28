package dialight.eventhelper.gui

import dialight.eventhelper.EventHelperPlugin
import dialight.eventhelper.EventHelperTool
import dialight.extensions.ItemStackBuilderEx
import dialight.extensions.closeInventoryLater
import dialight.guilib.View
import dialight.guilib.events.ItemClickEvent
import jekarus.colorizer.Text_colorized
import jekarus.colorizer.Text_colorizedList
import org.spongepowered.api.data.type.HandTypes
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.scheduler.Task

class EventHelperItem(val plugin: EventHelperPlugin) : View.Item {


    override val item = ItemStackBuilderEx(ItemTypes.EMERALD)
        .name(Text_colorized("|a|EventHelper"))
        .lore(
            Text_colorizedList(
                "|g|ЛКМ|y|: получить инструмент",
                "|g|ПКМ|y|: добавить инструмент в инвентарь",
                "",
                "|g|Плагин: |y|EventHelper",
                "|g|Версия: |y|v" + plugin.container.version.orElse("null")
            )
        )
        .build()

    override fun onClick(event: ItemClickEvent) {
        val player = event.player
        when (event.type) {
            ItemClickEvent.Type.LEFT, ItemClickEvent.Type.SHIFT_LEFT -> {
                player.closeInventoryLater(plugin)
                plugin.toollib.giveTool(player, EventHelperTool.ID)
            }
            ItemClickEvent.Type.RIGHT -> {
                player.inventory.offer(plugin.tool.buildItem())
            }
        }
    }

}