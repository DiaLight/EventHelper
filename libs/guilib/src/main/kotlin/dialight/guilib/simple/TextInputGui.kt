package dialight.guilib.simple

import dialight.extensions.getOrNull
import dialight.extensions.set
import dialight.guilib.Gui
import dialight.guilib.GuiPlugin
import dialight.guilib.View
import dialight.guilib.events.GuiOutsideClickEvent
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.InventoryArchetypes
import org.spongepowered.api.item.inventory.property.Identifiable
import org.spongepowered.api.item.inventory.property.InventoryTitle
import org.spongepowered.api.text.Text

class TextInputGui(
    val guiplugin: GuiPlugin,
    val id: Identifiable,
    title: Text
    ) : Gui, View {

    override val capacity = 3

    override val inventory = Inventory.builder()
        .of(InventoryArchetypes.ANVIL)
        .property(InventoryTitle.of(title))
        .property(id)
        .build(guiplugin)

    private val items = Array<View.Item?>(capacity) { null }

    var first: View.Item?
        get() = this[0]
        set(value) { this[0] = value }

    var second: View.Item?
        get() = this[1]
        set(value) { this[1] = value }

    var result: View.Item?
        get() = this[2]
        set(value) { this[2] = value }


    override fun ownerOf(player: Player, inv: Inventory): Boolean {
        val uuid = guiplugin.guimap.getCurrentUuid(player) ?: return false
        return id.value!! == uuid
//        val prop = inv.getInventoryProperty(Identifiable::class.java).getOrNull() ?: return false
//        return id.value!! == prop.value!!
    }

    override fun getView(player: Player): View = this

    override fun destroyFor(player: Player) {

    }

    override fun getViewOf(player: Player) = this
    override fun onCloseView(player: Player) = true

    override operator fun get(index: Int) = items[index]
    override operator fun set(index: Int, item: View.Item?) {
//        inventory.set(SlotIndex.of(line * width), info)
        inventory[index] = item?.item
        items[index] = item
    }

    override fun onOutsideClick(event: GuiOutsideClickEvent) {

    }

}