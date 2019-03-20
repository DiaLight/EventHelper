package dialight.guilib

import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.inventory.Inventory

interface Gui {

    fun ownerOf(
        player: Player,
        inv: Inventory
    ): Boolean

    fun getView(player: Player): View
    fun destroyFor(player: Player)

    fun getViewOf(player: Player): View
    fun onCloseView(player: Player): Boolean

}