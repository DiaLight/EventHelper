package dialight.teleporter.gui

import dialight.extensions.ItemStackBuilderEx
import dialight.guilib.View
import dialight.guilib.events.ItemClickEvent
import dialight.teleporter.TeleporterPlugin
import dialight.teleporter.TeleporterTool
import jekarus.colorizer.Text_colorized
import jekarus.colorizer.Text_colorizedList
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.type.DyeColors
import org.spongepowered.api.item.ItemTypes

class TeleporterItem(val plugin: TeleporterPlugin) : View.Item {

    override val item = ItemStackBuilderEx(ItemTypes.STICK)
        .also {
            offer(Keys.DYE_COLOR, DyeColors.LIGHT_BLUE)
        }
        .name(Text_colorized("|a|Телепортация игроков"))
        .lore(
            Text_colorizedList(
                "|g|ЛКМ|y|: Получить инструмент",
                "|g|ПКМ|y|: Открыть редактор",
                "|y| выбранных игроков",
                "",
                "|g|Плагин: |y|Телепорт",
                "|g|Версия: |y|v" + plugin.container.version.orElse("null")
            )
        )
        .build()

    override fun onClick(event: ItemClickEvent) {
        val player = event.player
        when (event.type) {
            ItemClickEvent.Type.LEFT, ItemClickEvent.Type.SHIFT_LEFT -> {
                player.closeInventory()
                plugin.toollib.giveTool(player, TeleporterTool.ID)
            }
            ItemClickEvent.Type.RIGHT -> {
                plugin.guilib?.openGui(player, plugin.taggergui)
            }
        }
    }

}
