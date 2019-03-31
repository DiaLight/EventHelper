package dialight.autorespawn.gui

import dialight.autorespawn.AutoRespawnPlugin
import dialight.extensions.itemStackOf
import dialight.guilib.View
import dialight.guilib.events.ItemClickEvent
import dialight.modulelib.ModuleMessages
import jekarus.colorizer.Text_colorized
import jekarus.colorizer.Text_colorizedList
import org.spongepowered.api.item.ItemTypes

class AutoRespawnItem(val plugin: AutoRespawnPlugin) : View.Item {

    override val item get() = itemStackOf(ItemTypes.BED) {
        displayName = Text_colorized("|y|${plugin.moduule.name}")
        lore.addAll(Text_colorizedList(
            "|g|ЛКМ|y|: ${if(!plugin.moduule.enabled) "Вкл" else "Выкл"} модуль",
            "",
            "|g|Версия: |y|v" + plugin.container.version.orElse("null")
        ))
    }

    override fun onClick(event: ItemClickEvent) {
        when(event.type) {
            ItemClickEvent.Type.LEFT -> {
                val newState = !plugin.moduule.enabled
                if(!plugin.moduule.toggle()) {
                    if(newState) {
                        event.player.sendMessage(ModuleMessages.cantEnable(plugin.moduule))
                    } else {
                        event.player.sendMessage(ModuleMessages.cantDisable(plugin.moduule))
                    }
                } else {
                    if(newState) {
                        event.player.sendMessage(ModuleMessages.successEnable(plugin.moduule))
                    } else {
                        event.player.sendMessage(ModuleMessages.successDisable(plugin.moduule))
                    }
                }
                event.updateItem = true
            }
        }
    }

}