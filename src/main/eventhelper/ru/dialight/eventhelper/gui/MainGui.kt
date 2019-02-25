package ru.dialight.eventhelper.gui

import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.type.DyeColors
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.item.inventory.property.SlotIndex
import org.spongepowered.api.text.Text
import ru.dialight.eventhelper.EventHelper
import ru.dialight.eventhelper.Text_colorized
import ru.dialight.eventhelper.Text_colorizedList
import ru.dielight.guilib.Gui
import ru.dielight.guilib.simple.SimpleGui

class MainGui(plugin: EventHelper) : SimpleGui (
    plugin.guiregistry,
    Text_colorized("Ультра мега инвентарь!"),
    9, 6) {

    init {
        /* Информация */
        for (line in 0..height) {
            val info = ItemStack.builder()
                .itemType(ItemTypes.STAINED_GLASS_PANE)
                .build()
            info.offer(Keys.DYE_COLOR, DyeColors.LIGHT_BLUE)
            info.offer(Keys.DISPLAY_NAME, Text_colorized("Краткое описание"))
            info.offer(Keys.ITEM_LORE, Text_colorizedList(
                "|a|Первая строчка - инструменты",
                "|a|Вторая строчка - модули",
                "|a|Третья строчка - модули*",
                "|a|* - Уникальные модули для",
                "|a|текущей реализации EventHelper",
                "",
                "|g|Версия: |y|v" + plugin.container.version.orElse("null")
            ))
            inventory.set(SlotIndex.of(line * width), info)
        }
    }

    override fun openFor(player: Player) {
        player.openInventory(inventory)
    }

    override fun onClose(player: Player) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}