package dialight.ehgui.gui

import dialight.ehgui.EHGuiPlugin
import dialight.ehgui.EHGuiTool
import dialight.extensions.closeInventoryLater
import dialight.extensions.itemStackOf
import dialight.guilib.View
import dialight.guilib.events.ItemClickEvent
import jekarus.colorizer.Text_colorized
import jekarus.colorizer.Text_colorizedList
import org.spongepowered.api.item.ItemTypes

class EHGuiItem(val plugin: EHGuiPlugin) : View.Item {


    override val item get() = itemStackOf(ItemTypes.EMERALD) {
        displayName = Text_colorized("|a|EventHelper")
        lore.addAll(Text_colorizedList(
            "|a|ЛКМ|y|: получить инструмент",
            "|a|ПКМ|y|: добавить инструмент в инвентарь",
            "",
            "|g|Плагин: |y|EventHelper",
            "|g|Версия: |y|v" + plugin.container.version.orElse("null")
        ))
    }

    override fun onClick(event: ItemClickEvent) {
        val player = event.player
        when (event.type) {
            ItemClickEvent.Type.LEFT, ItemClickEvent.Type.SHIFT_LEFT -> {
                player.closeInventoryLater(plugin)
                plugin.toollib.giveTool(player, EHGuiTool.ID)
            }
            ItemClickEvent.Type.RIGHT -> {
                plugin.toollib.giveTool(event.player, plugin.tool.id)
            }
        }
    }

}