package dialight.eventhelper

import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.type.DyeColors
import org.spongepowered.api.item.ItemTypes
import dialight.extensions.ItemStackBuilderEx
import dialight.guilib.View
import dialight.guilib.events.ItemClickEvent
import dialight.guilib.mixin.*
import jekarus.colorizer.Text_colorized
import jekarus.colorizer.Text_colorizedList
import dialight.guilib.simple.SimpleGui
import dialight.guilib.simple.SimpleItem
import dialight.toollib.Tool


class EventHelperGui(val plugin: EventHelperPlugin) : SimpleGui(
    plugin.guilib,
    Text_colorized("Инвентарь EventHelper"),
    9, 6) {

    companion object {
        val ID = "main"
    }

    val toolsInfo: View.Item

    init {
        /* Информация */

        toolsInfo = SimpleItem(
            ItemStackBuilderEx(ItemTypes.STAINED_GLASS_PANE)
            .also {
                offer(Keys.DYE_COLOR, DyeColors.LIGHT_BLUE)
            }
            .name(Text_colorized("|r|Краткое описание"))
            .lore(Text_colorizedList(
                "|a|Тут находятся инструменты",
                "",
                "|g|Версия: |y|v" + plugin.container.version.orElse("null")
            ))
            .build()
        ) {
            if(it.type == ItemClickEvent.Type.LEFT) {
                inventory.title = Text_colorized("Hello World!")
            }
        }

        updateItems()
        plugin.toollib.toolregistry.apply {
            onPut{ k, v ->
                updateItems()
            }
            onRemove { s, tool ->
                updateItems()
            }
            onReplace { key, old, new ->

            }
        }
        plugin.toolItemRegistry.apply {
            onPut{ k, v ->
                updateItems()
            }
            onRemove { s, tool ->
                updateItems()
            }
            onReplace { key, old, new ->

            }
        }
    }

    fun createDefault(k: String, v: Tool): View.Item {
        return SimpleItem(
            ItemStackBuilderEx(v.type)
//                .also {
//                    offer(Keys.DYE_COLOR, DyeColors.LIGHT_BLUE)
//                }
                .name(v.title)
                .lore(Text_colorizedList(
                        "|g|ЛКМ|y|: Получить инструмент"
//                    "",
//                    "|g|Версия: |y|v" + plugin.container.version.orElse("null")
                ))
                .build()
        ) {
            plugin.toollib.giveTool(it.player, v.id)
        }
    }

    fun updateItems() {
        var itemIndex = 0
        val toolit = plugin.toollib.toolregistry.iterator()
        while(toolit.hasNext() && itemIndex < capacity) {
            if((itemIndex % width) == 0) {
                this[itemIndex] = toolsInfo
            } else {
                val (k, v) = toolit.next()
                val item = plugin.toolItemRegistry[k] ?: createDefault(k, v)
                this[itemIndex] = item
            }
            itemIndex++
        }
        while(itemIndex < capacity) {
            if((itemIndex % width) == 0) break
            itemIndex++
        }
//        val modit = plugin.toollib.toolregistry.iterator()
    }

}
