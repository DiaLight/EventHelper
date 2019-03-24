package dialight.eventhelper.gui

import dialight.eventhelper.EventHelperPlugin
import dialight.extensions.ItemStackBuilderEx
import dialight.extensions.closeInventoryLater
import dialight.guilib.Gui
import dialight.guilib.View
import dialight.guilib.events.ItemClickEvent
import dialight.guilib.simple.SimpleItem
import dialight.guilib.snapshot.Snapshot
import dialight.modulelib.Module
import dialight.toollib.Tool
import jekarus.colorizer.Text_colorized
import jekarus.colorizer.Text_colorizedList
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.type.DyeColors
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.inventory.property.Identifiable
import org.spongepowered.api.text.Text

class EventHelperSnapshot(val plugin: EventHelperPlugin, id: Identifiable) : Snapshot<Snapshot.Page>(plugin.guilib, id) {


    fun createDefault(k: String, v: Tool): View.Item {
        return SimpleItem(
            ItemStackBuilderEx(v.type)
                .name(v.title)
                .lore(
                    Text_colorizedList(
                        "|g|ЛКМ|y|: Получить инструмент"
//                    "",
//                    "|g|Версия: |y|v" + plugin.container.version.orElse("null")
                    )
                )
                .build()
        ) {
            when(it.type) {
                ItemClickEvent.Type.LEFT -> {
                    it.player.closeInventoryLater(plugin)
                    plugin.toollib.giveTool(it.player, v.id)
                }
            }
        }
    }

    fun createDefault(k: String, v: Module): View.Item {
        return SimpleItem(
            ItemStackBuilderEx(ItemTypes.ANVIL)
                .name(Text_colorized("|y|${v.id}"))
                .lore(
                    Text_colorizedList(
                        "|g|ЛКМ|y|: Вкл/Выкл модуль"
//                    "",
//                    "|g|Версия: |y|v" + plugin.container.version.orElse("null")
                    )
                )
                .build()
        ) {
            when(it.type) {
                ItemClickEvent.Type.LEFT -> {
                    it.player.closeInventoryLater(plugin)
                    plugin.toollib.giveTool(it.player, v.id)
                }
            }
        }
    }

    fun updateItems() {
        pages.clear()

        val maxLines = 6
        val maxColumns = 9
        var index = 0

        // tools
        val toolItems = arrayListOf<View.Item>()
        for((id, tool) in plugin.toollib.toolregistry) {
            toolItems.add(plugin.toolItemRegistry[id] ?: createDefault(id, tool))
        }
        val rawToolPages = EventHelperPageBuilder(maxColumns - 2, maxLines, toolItems).toList()

        // modules
        val moduleItems = arrayListOf<View.Item>()
        for((id, module) in plugin.modulelib.moduleregistry) {
            moduleItems.add(plugin.moduleItemRegistry[id] ?: createDefault(id, module))
        }
        val rawModulePages = EventHelperPageBuilder(maxColumns - 2, maxLines, moduleItems).toList()

        val total = rawToolPages.size + rawModulePages.size

        rawToolPages.forEach { slotCache ->
            val gui = ToolsPage(this, Text_colorized("EventHelper(${index + 1}/$total): Tools"), maxColumns, maxLines, index)
            slotCache.forEach { pos, slot ->
                gui[(pos.x + 1) + pos.y * maxColumns] = slot
            }
            pages.add(gui)
            index++
        }
        rawModulePages.forEach { slotCache ->
            val gui = ModulesPage(this, Text_colorized("EventHelper(${index + 1}/$total): Modules"), maxColumns, maxLines, index)
            slotCache.forEach { pos, slot ->
                gui[(pos.x + 1) + pos.y * maxColumns] = slot
            }
            pages.add(gui)
            index++
        }

        if(pages.isEmpty()) pages.add(Page(this, Text_colorized("EventHelper: tools or modules not found"), maxColumns, maxLines, 0))

    }


    class ToolsPage(
        snap: EventHelperSnapshot,
        title: Text,
        width: Int,
        height: Int,
        index: Int
    ) : Snapshot.Page(
        snap, title, width, height, index
    ) {

        init {
            val description = SimpleItem(ItemStackBuilderEx(ItemTypes.STAINED_GLASS_PANE)
                .also {
                    offer(Keys.DYE_COLOR, DyeColors.LIGHT_BLUE)
                }
                .name(Text_colorized("Инструменты"))
                .build()) {
            }
            for(y in (0..(height - 1))) {
                this[y * width] = description
                this[(width - 1) + y * width] = description
            }
        }

    }

    class ModulesPage(
        snap: EventHelperSnapshot,
        title: Text,
        width: Int,
        height: Int,
        index: Int
    ) : Snapshot.Page(
        snap, title, width, height, index
    ) {

        init {
            val description = SimpleItem(ItemStackBuilderEx(ItemTypes.STAINED_GLASS_PANE)
                .also {
                    offer(Keys.DYE_COLOR, DyeColors.RED)
                }
                .name(Text_colorized("Модули"))
                .build())
            for(y in (0..(height - 1))) {
                this[y * width] = description
                this[(width - 1) + y * width] = description
            }
        }

    }

}