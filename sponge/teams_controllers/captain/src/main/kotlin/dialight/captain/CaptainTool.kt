package dialight.captain

import dialight.extensions.*
import dialight.toollib.Tool
import dialight.toollib.events.ToolInteractEvent
import jekarus.colorizer.Colorizer
import jekarus.colorizer.Text_colorized
import org.spongepowered.api.entity.Entity
import org.spongepowered.api.entity.EntityTypes
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.scheduler.Task
import java.util.*

class CaptainTool(val plugin: CaptainPlugin) : Tool(CaptainTool.ID, true) {

    companion object {
        val ID = "captain"
    }

    override val type = ItemTypes.BANNER
    override val title = Colorizer.apply("|a|Выбор игрока")
    override val lore = Colorizer.apply(
        mutableListOf(
            "|a|ЛКМ по игроку|y|: Выбрать игрока",
            "|y| для выбора игроков",
            "|a|ПКМ|y|: Открыть список доступных",
            "|y| для выбора игроков"
        )
    )

    override val build: ItemStackBuilderEx.(player: Player) -> Unit = build@{ player ->
        val sys = plugin.system
        val cap = sys.getCaptain(player) ?: return@build
        raw {
            itemDamageValue = cap.dyeColor.bannerDurability
        }
        hideMiscellaneous = true
    }

    override fun onClick(e: ToolInteractEvent) {
//        println(e.item)
//        println("- " + e.player.getItemInHand(HandTypes.MAIN_HAND))
        val sys = plugin.system
        if(!sys.running) {
            e.player.sendMessage(CaptainMessages.toolPluginDisabled)
            return
        }
        if(e.player.uniqueId != sys.current.uuid) {
            e.player.sendMessage(CaptainMessages.onlyActiveCaptain)
            return
        }
        when(e.action) {
            ToolInteractEvent.Action.LEFT_CLICK -> if(!e.sneaking) {
                if (e.type == ToolInteractEvent.Type.ENTITY) {
                    e as ToolInteractEvent.Entity
                    trySelect(e.player, e.entity)
                } else {
                    val p2 = Utils.getEnByDirection(e.player, 20.0, 1.5, EntityTypes.PLAYER, EntityTypes.VILLAGER)
                    if (p2 != null) {
                        trySelect(e.player, p2)
                    }
                }
            } else {

            }
            ToolInteractEvent.Action.RIGHT_CLICK -> if(!e.sneaking) {
                e.player.sendMessage(Text_colorized("|r|Not implemented yet"))
            } else {

            }
            ToolInteractEvent.Action.DROP -> if(!e.sneaking) {

            } else {

            }
        }
    }

    private fun trySelect(player: Player, entity: Entity) {
        if(entity.type == EntityTypes.PLAYER) {
            println("try select player")
            trySelectLater(player, entity as Player)
        } else {
            println("try select shadow")
            val shadow = plugin.offlinelib.shadow.getShadow(entity)
            if(shadow != null) {
                if(Server_getPlayer(shadow.uuid) == null) {
                    trySelectLater(player, shadow.uuid)
                }
            }
        }
    }

    /**
     * Sponge do not allow make any transactions in event handler. So, we must do it later.
     */
    private fun trySelectLater(player: Player, target: Player) = trySelectLater(player, target.uniqueId)
    private fun trySelectLater(player: Player, target: UUID) {
        Task.builder().execute { task->
            val sys = plugin.system
            if(!sys.select(target)) {
                player.sendMessage(CaptainMessages.youCantSelectHim)
            }
        }.submit(plugin)
    }

}
