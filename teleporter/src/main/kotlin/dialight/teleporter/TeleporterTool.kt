package dialight.teleporter

import dialight.eventhelper.EventHelperTool
import dialight.extensions.*
import dialight.teleporter.event.PlayerTeleportEvent
import dialight.toollib.Tool
import dialight.toollib.events.ToolInteractEvent
import jekarus.colorizer.Colorizer
import jekarus.colorizer.Text_colorizedList
import org.spongepowered.api.Sponge
import org.spongepowered.api.block.BlockType
import org.spongepowered.api.block.BlockTypes
import org.spongepowered.api.entity.EntityTypes
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.api.world.Location
import org.spongepowered.api.world.World

class TeleporterTool(val plugin: TeleporterPlugin) : Tool(TeleporterTool.ID) {

    companion object {
        val ID = "teleporter"
    }

    override val type = ItemTypes.STICK
    override val title = Colorizer.apply("|a|Телепорт игроков")
    override val lore = Text_colorizedList(
        "|w|Телепортация",
        "|g|ЛКМ|y|: телепортировать выбранных игроков",
        "|w|Выбор игроков",
        "|g|ПКМ|y|: открыть редактор выбранных игроков",
        "|g|ПКМ по игроку*|y|: выбрать игрока",
        "|y|*Игрока можно пометить на расстоянии.",
        "|g|Shift|y|+|g|ПКМ|y|: очистить список выбранных",
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
            plugin.offlineteleporter.teleport(trg, trgLoc)
        }
        e.player.sendMessage(TeleporterMessages.YouTp(online, offline))
    }


    override fun onClick(e: ToolInteractEvent) {
        when (e.action) {
            ToolInteractEvent.Type.LEFT_CLICK -> if(!e.sneaking) {
                teleport(e,
                    plugin.teleporter[e.player].online,
                    plugin.teleporter[e.player].offline
                )
            } else {

            }
            ToolInteractEvent.Type.RIGHT_CLICK -> if(!e.sneaking) {
                if (e.type == ToolInteractEvent.Target.ENTITY) {
                    e as ToolInteractEvent.Entity
                    if (e.entity.type == EntityTypes.PLAYER) {
                        val player = e.entity as Player
                        val result = plugin.teleporter.invoke(e.player, Teleporter.Action.TOGGLE, player)
                        result.sendReport(e.player)
                        plugin.teleporter.sendTargetsReport(e.player)
                    }
                } else {
                    val p2 = Utils.getEnByDirection(e.player, 20.0, 1.5, EntityTypes.PLAYER)
                    if (p2 == null) {
                        plugin.guilib?.openGui(e.player, plugin.teleportergui)
                    } else {
                        val result = plugin.teleporter.invoke(e.player, Teleporter.Action.TOGGLE, p2 as Player)
                        result.sendReport(e.player)
                        plugin.teleporter.sendTargetsReport(e.player)
                    }
                }
            } else {
                plugin.teleporter.invoke(e.player, Teleporter.Action.UNTAG, Teleporter.Group.ALL)
                e.player.sendMessage(TeleporterMessages.AllPlayersRemoved)
            }
            ToolInteractEvent.Type.DROP -> {
                if(plugin.eh != null) {
                    plugin.toollib.giveTool(e.player, EventHelperTool.ID)
                }
                plugin.guilib?.clearStory(e.player)
            }
        }
    }
}

fun BlockType.isSolid() = when(this) {
    BlockTypes.AIR,
    BlockTypes.ACACIA_DOOR,
    BlockTypes.BIRCH_DOOR,
    BlockTypes.DARK_OAK_DOOR,
    BlockTypes.ACACIA_DOOR,
    BlockTypes.GRASS,
    BlockTypes.TALLGRASS,
    BlockTypes.GRASS_PATH
    -> false
    else -> true
}

fun Player.teleportSafe(loc: Location<World>) {

    val feet = loc.block
    val head = loc.add(.0, 1.0, .0).block
    if(feet.type.isSolid() || feet.type.isSolid()) {
        setLocationSafely(loc)
        return
    }
    location = loc
}
