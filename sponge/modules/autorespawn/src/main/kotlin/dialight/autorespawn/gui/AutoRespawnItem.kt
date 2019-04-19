package dialight.autorespawn.gui

import dialight.autorespawn.AutoRespawnPlugin
import dialight.extensions.Server_getPlayers
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
            "|a|ЛКМ|y|: ${if(!plugin.moduule.enabled) "Вкл" else "Выкл"} модуль",
            "|a|Shift|y|+|a|ЛКМ|y|: Отправить всем пакет респавна.",
            "|y| Это повлияет только на тех,",
            "|y| кто находится в меню респавна.",
            "",
            "|g|Версия: |y|v" + plugin.container.version.orElse("null")
        ))
    }

    override fun onClick(event: ItemClickEvent) {
        when(event.type) {
            ItemClickEvent.Type.LEFT -> {
                plugin.moduule.toggle(event.player)
                event.updateItem = true
            }
            ItemClickEvent.Type.SHIFT_LEFT -> {
                for(player in Server_getPlayers()) player.respawnPlayer()
            }
        }
    }

}