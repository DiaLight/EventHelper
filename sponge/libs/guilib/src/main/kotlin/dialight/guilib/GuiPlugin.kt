package dialight.guilib

import com.google.inject.Inject
import dialight.extensions.getOrNull
import dialight.guilib.mixin.setValue
import dialight.observable.map.observableMapOf
import org.slf4j.Logger
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameStartedServerEvent
import org.spongepowered.api.item.inventory.Container
import org.spongepowered.api.item.inventory.Inventory
import org.spongepowered.api.item.inventory.property.Identifiable
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.plugin.PluginManager
import java.util.*

@Plugin(id = "guilib")
class GuiPlugin @Inject constructor(
    val container: PluginContainer,
    val logger: Logger
) {

    val guimap = GuiMap(this)
    val guistory = GuiStory(this)

    @Listener
    fun onServerStart(event: GameStartedServerEvent) {
        Sponge.getEventManager().registerListeners(this, GuiListener(this))
        logger.info("${container.name} v${container.version.orElse("null")} has been Enabled")
    }

    fun openView(player: Player, view: View) = guimap.openView(player, view)
    fun openGui(player: Player, gui: Gui) = guistory.openGui(player, gui)
    fun openLast(player: Player) = guistory.openLast(player)
    fun clearStory(player: Player) = guistory.clearStory(player)
    fun currentGui(player: Player, targetInventory: Container? = null): Gui? {
        var inv: Inventory? = targetInventory
        if(inv == null) inv = player.openInventory.getOrNull() ?: return null
        val gui = guistory.currentGui(player, inv)
        if(gui != null) return gui
//        val uuid = guimap.getCurrentUuid(player)
//        if(uuid != null) {
//            val idtoprop = inv.getInventoryProperty(Identifiable::class.java).getOrNull() ?: throw Exception("Can't identify to view")
//            idtoprop.setValue(uuid)
//            val gui = guistory.currentGui(player, inv)
//            if(gui != null) return gui
//        }
        return null
    }

}