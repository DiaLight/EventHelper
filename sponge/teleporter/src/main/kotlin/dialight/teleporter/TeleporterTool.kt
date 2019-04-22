package dialight.teleporter

import dialight.ehgui.EHGuiTool
import dialight.extensions.*
import dialight.teleporter.event.PlayerTeleportEvent
import dialight.toollib.Tool
import dialight.toollib.events.ToolInteractEvent
import dialight.user.shadow.OfflineShadow
import jekarus.colorizer.Colorizer
import jekarus.colorizer.Text_colorizedList
import org.spongepowered.api.Sponge
import org.spongepowered.api.entity.EntityTypes
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.item.ItemTypes
import java.util.*

class TeleporterTool(val plugin: TeleporterPlugin) : Tool(TeleporterTool.ID) {

    companion object {
        val ID = "teleporter"
    }

    override val type = ItemTypes.STICK
    override val title = Colorizer.apply("|a|Телепорт игроков")
    override val lore = Text_colorizedList(
        "|w|Телепортация",
        "|a|ЛКМ|y|: телепортировать выбранных игроков",
        "|w|Выбор игроков",
        "|a|ПКМ|y|: открыть редактор выбранных игроков",
        "|a|ПКМ по игроку*|y|: выбрать игрока",
        "|y|*Игрока можно пометить на расстоянии.",
        "|a|Shift|y|+|a|ПКМ|y|: очистить список выбранных",
        "",
        "|g|Плагин: |y|Телепорт",
        "|g|Версия: |y|v" + plugin.container.version.orElse("null")
    )

    private fun teleport(e: ToolInteractEvent, online: Collection<Player>, offline: Collection<User>) {
        if (online.isEmpty() && offline.isEmpty()) {
            e.player.sendMessage(TeleporterMessages.noPlayersSelected)
            return
        }
        val trgLoc = e.lookingAtLoc()
        for (trg in online) {
            Sponge.getEventManager().post(PlayerTeleportEvent.ByPlayer(trg, e.player, trg.location, trgLoc))
            trg.teleportSafe(trgLoc)
            trg.sendMessage(TeleporterMessages.YouHBTp(e.player.name))
        }
        for (trg in offline) {
            plugin.offlinelib.teleport(trg.uniqueId, trgLoc)
        }
        e.player.sendMessage(TeleporterMessages.YouTp(online, offline))
    }


    override fun onClick(e: ToolInteractEvent) {
        when (e.action) {
            ToolInteractEvent.Action.LEFT_CLICK -> if(!e.sneaking) {
                teleport(e,
                    plugin.teleporter[e.player].online,
                    plugin.teleporter[e.player].offline
                )
            } else {

            }
            ToolInteractEvent.Action.RIGHT_CLICK -> if(!e.sneaking) {
                if (e.type == ToolInteractEvent.Type.ENTITY) {
                    e as ToolInteractEvent.Entity
                    if (e.entity.type == EntityTypes.PLAYER) {
                        toggleSelect(e.player, (e.entity as Player).uniqueId, null)
                    }
                } else {
                    val p2 = Utils.getEnByDirection(e.player, 20.0, 1.5, EntityTypes.PLAYER, OfflineShadow.TYPE)
                    if (p2 == null) {
                        plugin.guilib?.openGui(e.player, plugin.teleportergui)
                    } else {
                        when(p2.type) {
                            EntityTypes.PLAYER -> {
                                toggleSelect(e.player, (p2 as Player).uniqueId, null)
                            }
                            OfflineShadow.TYPE -> {
                                val shadow = plugin.offlinelib.shadow.getShadow(p2)
                                if(shadow != null) {
                                    toggleSelect(e.player, shadow.uuid, shadow.name)
                                }
                            }
                        }
                    }
                }
            } else {
                plugin.teleporter.invoke(e.player, Teleporter.Action.UNTAG, Teleporter.Group.ALL)
                e.player.sendMessage(TeleporterMessages.AllPlayersRemoved)
            }
            ToolInteractEvent.Action.DROP -> {
                if(plugin.eh != null) {
                    plugin.toollib.giveTool(e.player, EHGuiTool.ID)
                }
                plugin.guilib?.clearStory(e.player)
            }
        }
    }

    fun toggleSelect(player: Player, uuid: UUID, name: String?) {
        val result = plugin.teleporter.invoke(player, Teleporter.Action.TOGGLE, uuid, name)
        result.sendReport(player)
        plugin.teleporter.sendTargetsReport(player)
    }

}
