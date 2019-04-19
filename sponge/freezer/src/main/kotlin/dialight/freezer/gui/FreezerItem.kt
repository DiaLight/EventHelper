package dialight.freezer.gui

import dialight.extensions.closeInventoryLater
import dialight.extensions.itemStackOf
import dialight.freezer.FreezerPlugin
import dialight.freezer.FreezerTool
import dialight.guilib.View
import dialight.guilib.events.ItemClickEvent
import jekarus.colorizer.Text_colorized
import jekarus.colorizer.Text_colorizedList
import org.spongepowered.api.item.ItemTypes

class FreezerItem(val plugin: FreezerPlugin) : View.Item {


    override val item get() = itemStackOf(ItemTypes.ICE) {
        displayName = Text_colorized("|a|Замораживатель игроков")
        lore.addAll(
            Text_colorizedList(
                "|a|ЛКМ|y|: получить инструмент",
                "|a|ПКМ|y|: открыть замораживатель",
                "",
                "|g|Плагин: |y|Замораживатель",
                "|g|Версия: |y|v" + plugin.container.version.orElse("null")
            )
        )
    }

    override fun onClick(event: ItemClickEvent) {
        val player = event.player
        when (event.type) {
            ItemClickEvent.Type.LEFT, ItemClickEvent.Type.SHIFT_LEFT -> {
                player.closeInventoryLater(plugin)
                plugin.toollib.giveTool(player, FreezerTool.ID)
            }
            ItemClickEvent.Type.RIGHT -> {
                plugin.guilib?.openGui(player, plugin.freezergui)
            }
        }
    }

}