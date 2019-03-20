package dialight.teams

import dialight.extensions.getOrNull
import dialight.toollib.Tool
import dialight.toollib.events.ToolInteractEvent
import jekarus.colorizer.Text_colorized
import jekarus.colorizer.Text_colorizedList
import org.spongepowered.api.Sponge
import org.spongepowered.api.item.ItemTypes

class TeamsTool(val plugin: TeamsPlugin) : Tool(TeamsTool.ID) {

    companion object {
        val ID = "teamstool"
    }

    override val type = ItemTypes.BLAZE_ROD
    override val title = Text_colorized("Распределитель команд")
    override val lore = Text_colorizedList(
        "|g|ПКМ|y|: открыть редактор команд",
        "|g|Shift|y|+|g|ПКМ|y|: убрать выделение команды",
        "",
        "|g|Плагин: |y|Распределитель команд",
        "|g|Версия: |y|v" + plugin.container.version.orElse("null")
    )


    override fun onClick(e: ToolInteractEvent) {
        when(e.action) {
            ToolInteractEvent.Type.LEFT_CLICK -> if(!e.sneaking) {

            } else {

            }
            ToolInteractEvent.Type.RIGHT_CLICK -> if(!e.sneaking) {
                plugin.guilib?.also { guilib ->
                    guilib.openGui(e.player, plugin.teamsgui)
                }
            } else {
                val team = plugin.selected.value
                if(team != null) {
                    plugin.selected.value = null
                    e.player.sendMessage(TeamsMessages.unselectedTeam(team))
                }
            }
        }
    }

}