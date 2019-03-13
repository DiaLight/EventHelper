package dialight.guilib.simple

import dialight.extensions.openInventoryLater
import dialight.guilib.Gui
import dialight.guilib.GuiPlugin
import dialight.guilib.IdentifiableView
import dialight.guilib.View
import dialight.guilib.events.GuiOutsideClickEvent
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.property.Identifiable
import org.spongepowered.api.text.Text


open class SimpleGui(
    guiplugin: GuiPlugin,
    title: Text,
    width: Int,
    height: Int
) : Gui, IdentifiableView(guiplugin, Identifiable(), title, width, height) {

//    val id = Identifiable.random()
//    val id = Identifiable()

    override fun ownerOf(inv: Inventory): Boolean {
        val oprop = inv.getInventoryProperty(Identifiable::class.java)
        if(!oprop.isPresent) throw Exception("not present $inv")
        return id.value!! == oprop.get().value!!
    }

    override fun getView(player: Player): View = this

    override fun destroyFor(player: Player) {

    }

    override fun getViewOf(player: Player) = this
    override fun onCloseView(player: Player) = true

    override fun onOutsideClick(event: GuiOutsideClickEvent) {
        when(event.type) {
            GuiOutsideClickEvent.Type.LEFT -> {

            }
            GuiOutsideClickEvent.Type.MIDDLE -> {
                guiplugin.guistory.openPrev(event.player)
            }
            GuiOutsideClickEvent.Type.RIGHT -> {

            }
        }
    }

}
