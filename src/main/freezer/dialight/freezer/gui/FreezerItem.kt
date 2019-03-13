package dialight.freezer.gui

import dialight.extensions.ItemStackBuilderEx
import dialight.freezer.FreezerPlugin
import dialight.freezer.FreezerTool
import dialight.guilib.View
import dialight.guilib.events.ItemClickEvent
import jekarus.colorizer.Text_colorized
import jekarus.colorizer.Text_colorizedList
import org.spongepowered.api.item.ItemTypes

class FreezerItem(val plugin: FreezerPlugin) : View.Item {


    override val item = ItemStackBuilderEx(ItemTypes.ICE)
        .name(Text_colorized("|a|Замораживатель игроков"))
        .lore(
            Text_colorizedList(
                "|g|ЛКМ|y|: получить инструмент",
                "|g|ПКМ|y|: открыть замораживатель",
                "",
                "|g|Плагин: |y|Замораживатель",
                "|g|Версия: |y|v" + plugin.container.version.orElse("null")
            )
        )
        .build()

    override fun onClick(event: ItemClickEvent) {
        val player = event.player
        when (event.type) {
            ItemClickEvent.Type.LEFT, ItemClickEvent.Type.SHIFT_LEFT -> {
                player.closeInventory()
                plugin.toollib.giveTool(player, FreezerTool.ID)
            }
            ItemClickEvent.Type.RIGHT -> {
//                plugin.guilib?.openGui(player, plugin.freezergui)
            }
        }
    }

}