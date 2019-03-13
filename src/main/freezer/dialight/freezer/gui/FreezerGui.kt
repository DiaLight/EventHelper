package dialight.freezer.gui

import dialight.freezer.FreezerPlugin
import dialight.guilib.Gui
import dialight.guilib.View
import dialight.guilib.simple.SimpleGui
import jekarus.colorizer.Text_colorized
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.Inventory

class FreezerGui(val plugin: FreezerPlugin) : Gui {

    val stub = SimpleGui(plugin.guilib!!, Text_colorized("Stub"), 6, 9)

    override fun ownerOf(inv: Inventory): Boolean = stub.ownerOf(inv)

    override fun getView(player: Player): View = stub.getView(player)

    override fun destroyFor(player: Player) = stub.destroyFor(player)

    override fun getViewOf(player: Player): View = stub.getViewOf(player)

    override fun onCloseView(player: Player): Boolean = stub.onCloseView(player)

}