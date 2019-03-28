package dialight.eventhelper.gui

import dialight.eventhelper.EventHelperPlugin
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.type.DyeColors
import org.spongepowered.api.item.ItemTypes
import dialight.extensions.ItemStackBuilderEx
import dialight.extensions.closeInventoryLater
import dialight.guilib.View
import dialight.guilib.events.ItemClickEvent
import jekarus.colorizer.Text_colorized
import jekarus.colorizer.Text_colorizedList
import dialight.guilib.simple.SimpleGui
import dialight.guilib.simple.SimpleItem
import dialight.guilib.snapshot.SnapshotGui
import dialight.toollib.Tool
import org.spongepowered.api.entity.living.player.Player


class EventHelperGui(val plugin: EventHelperPlugin) : SnapshotGui<EventHelperSnapshot>() {

    private var snap = EventHelperSnapshot(plugin, id)

    override fun getSnapshot(player: Player) = snap

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

            }
        }

        snap.updateItems()
        plugin.toollib.toolregistry.apply {
            onPut{ k, v ->
                snap.updateItems()
            }
            onRemove { s, tool ->
                snap.updateItems()
            }
            onReplace { key, old, new ->

            }
        }
        plugin.toolItemRegistry.apply {
            onPut{ k, v ->
                snap.updateItems()
            }
            onRemove { s, tool ->
                snap.updateItems()
            }
            onReplace { key, old, new ->

            }
        }
        plugin.modulelib.moduleregistry.apply {
            onPut{ k, v ->
                snap.updateItems()
            }
            onRemove { s, tool ->
                snap.updateItems()
            }
            onReplace { key, old, new ->

            }
        }
        plugin.moduleItemRegistry.apply {
            onPut{ k, v ->
                snap.updateItems()
            }
            onRemove { s, tool ->
                snap.updateItems()
            }
            onReplace { key, old, new ->

            }
        }
    }

}
