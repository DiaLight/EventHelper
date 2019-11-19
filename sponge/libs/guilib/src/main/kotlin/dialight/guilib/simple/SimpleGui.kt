package dialight.guilib.simple

import dialight.extensions.getOrNull
import dialight.guilib.Gui
import dialight.guilib.GuiPlugin
import dialight.guilib.IdentifiableView
import dialight.guilib.View
import dialight.guilib.events.GuiOutsideClickEvent
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.property.Identifiable
import org.spongepowered.api.scheduler.Task
import org.spongepowered.api.text.Text


open class SimpleGui(
    guiplugin: GuiPlugin,
    title: Text,
    width: Int,
    height: Int
) : Gui, IdentifiableView(guiplugin, Identifiable(), title, width, height) {

//    val id = Identifiable.random()
//    val id = Identifiable()

    override fun ownerOf(
        player: Player,
        inv: Inventory
    ): Boolean {
        val prop = inv.getInventoryProperty(Identifiable::class.java).getOrNull() ?: return false
        return id.value!! == prop.value!!
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
                Task.builder().execute { task ->
                    guiplugin.guistory.openPrev(event.player)
                }.submit(guiplugin)
            }
            GuiOutsideClickEvent.Type.RIGHT -> {

            }
        }
    }

}
