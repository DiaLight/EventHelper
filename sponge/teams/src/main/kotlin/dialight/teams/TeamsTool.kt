package dialight.teams

import dialight.maingui.MainGuiTool
import dialight.extensions.ItemStackBuilderEx
import dialight.toollib.Tool
import dialight.toollib.events.ToolInteractEvent
import jekarus.colorizer.Text_colorized
import jekarus.colorizer.Text_colorizedList
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.item.ItemTypes

class TeamsTool(val plugin: TeamsPlugin) : Tool(TeamsTool.ID) {

    companion object {
        val ID = "teamstool"
    }

    override val type = ItemTypes.BANNER
    override val title = Text_colorized("|a|Распределитель команд")
    override val lore = Text_colorizedList(
        "|a|ПКМ|y|: открыть редактор команд",
        "|a|Shift|y|+|a|ПКМ|y|: убрать выделение команды",
        "",
        "|g|Плагин: |y|Распределитель команд",
        "|g|Версия: |y|v" + plugin.container.version.orElse("null")
    )

    override val build: ItemStackBuilderEx.(player: Player) -> Unit = { player ->
        raw {
            nbt = """
{BlockEntityTag:{Patterns:[
  {Color:11,Pattern:"vhr"},
  {Color:10,Pattern:"vh"},
  {Color:14,Pattern:"tr"},
  {Color:12,Pattern:"bl"},
  {Color:5,Pattern:"br"},
  {Color:1,Pattern:"tl"}
]}}
                """.trimIndent()
        }
        hideMiscellaneous = true
    }

    override fun onClick(e: ToolInteractEvent) {
        when(e.action) {
            ToolInteractEvent.Action.LEFT_CLICK -> if(!e.sneaking) {

            } else {

            }
            ToolInteractEvent.Action.RIGHT_CLICK -> if(!e.sneaking) {
                plugin.guilib?.also { guilib ->
                    guilib.openGui(e.player, plugin.teamsgui)
                }
            } else {
                val team = plugin.selected.remove(e.player.uniqueId)
                if(team != null) {
                    e.player.sendMessage(TeamsMessages.unselectedTeam(team))
                }
            }
            ToolInteractEvent.Action.DROP -> {
                if(plugin.eh != null) {
                    plugin.toollib.giveTool(e.player, MainGuiTool.ID)
                }
                plugin.guilib?.clearStory(e.player)
            }
        }
    }

}