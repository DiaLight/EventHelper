package dialight.random.gui

import dialight.extensions.itemStackOf
import dialight.guilib.View
import dialight.guilib.events.ItemClickEvent
import dialight.random.RandomPlugin
import jekarus.colorizer.Text_colorized
import jekarus.colorizer.Text_colorizedList
import org.spongepowered.api.item.ItemTypes

class RandomItem(val plugin: RandomPlugin) : View.Item {

    override val item get() = itemStackOf(ItemTypes.ENDER_EYE) {
        displayName = Text_colorized("|y|${plugin.module.name}")
        lore.addAll(Text_colorizedList(
            "|a|ЛКМ|y|: ${if(!plugin.module.enabled) "Вкл" else "Выкл"} модуль",
            "",
            "|g|Версия: |y|v" + plugin.container.version.orElse("null")
        ))
    }

    override fun onClick(event: ItemClickEvent) {
        when(event.type) {
            ItemClickEvent.Type.LEFT -> {
                plugin.module.toggle(event.player)
            }
        }
    }

}