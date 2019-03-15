package dialight.freezer

import dialight.eventhelper.EventHelperTool
import dialight.extensions.Utils
import dialight.teleporter.Teleporter
import dialight.toollib.Tool
import dialight.toollib.events.ToolInteractEvent
import jekarus.colorizer.Text_colorized
import jekarus.colorizer.Text_colorizedList
import org.spongepowered.api.entity.EntityTypes
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.ItemTypes

class FreezerTool(val plugin: FreezerPlugin) : Tool(FreezerTool.ID) {

    companion object {
        val ID = "freezer"
    }

    override val type = ItemTypes.ICE
    override val title = Text_colorized("|a|Замораживатель игроков")
    override val lore = Text_colorizedList(
        "|w|Заморозка",
        "|g|ЛКМ по игроку|y|: заморозить/разморозить игрока",
        "|w|Выбор игроков",
        "|g|ПКМ|y|: открыть замораживатель",
        "|g|Shift|y|+|g|ПКМ|y|: добавить замороженных",
        "|y| в список телепортируемых",
        "",
        "|g|Плагин: |y|Замораживатель",
        "|g|Версия: |y|v" + plugin.container.version.orElse("null")
    )

    override fun onClick(e: ToolInteractEvent) {
        when (e.action) {
            ToolInteractEvent.Type.LEFT_CLICK -> if (!e.sneaking) {
                if (e.type == ToolInteractEvent.Target.ENTITY) {
                    e as ToolInteractEvent.Entity
                    if (e.entity.type == EntityTypes.PLAYER) {
                        plugin.freezer.invoke(e.player, Freezer.Action.TOGGLE, e.entity as Player)
                    }
                } else {
                    val p2 = Utils.getEnByDirection(e.player, 20.0, 1.5, EntityTypes.PLAYER) as? Player
                    if (p2 != null) {
                        plugin.freezer.invoke(e.player, Freezer.Action.TOGGLE, p2)
                    }
                }
            } else {

            }
            ToolInteractEvent.Type.RIGHT_CLICK -> if (!e.sneaking) {
                plugin.guilib?.also { guilib ->
                    guilib.openGui(e.player, plugin.freezergui)
                }
            } else {
                val teleporter = plugin.teleporter.teleporter
                teleporter.invoke(e.player, Teleporter.Action.UNTAG, Teleporter.Group.ALL)
                val result = Teleporter.Result()
                plugin.freezer.forEachOnline { uuid, name ->
                    result.add(teleporter.invoke(e.player, Teleporter.Action.TAG, uuid, name))
                }
                result.sendReport(e.player)
            }
            ToolInteractEvent.Type.DROP -> {
                plugin.eh?.also { eh ->
                    plugin.toollib.giveTool(e.player, EventHelperTool.ID)
                }
                plugin.guilib?.clearStory(e.player)
            }
        }
    }

}
