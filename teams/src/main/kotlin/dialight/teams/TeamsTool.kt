package dialight.teams

import dialight.eventhelper.EventHelperTool
import dialight.extensions.ItemStackBuilderEx
import dialight.extensions.getOrNull
import dialight.toollib.Tool
import dialight.toollib.events.ToolInteractEvent
import jekarus.colorizer.Text_colorized
import jekarus.colorizer.Text_colorizedList
import org.spongepowered.api.Sponge
import org.spongepowered.api.item.ItemTypes
import java.util.function.Predicate
import java.util.function.Supplier

class TeamsTool(val plugin: TeamsPlugin) : Tool(TeamsTool.ID) {

    companion object {
        val ID = "teamstool"
    }

    override val type = ItemTypes.BANNER
    override val title = Text_colorized("Распределитель команд")
    override val lore = Text_colorizedList(
        "|g|ПКМ|y|: открыть редактор команд",
        "|g|Shift|y|+|g|ПКМ|y|: убрать выделение команды",
        "",
        "|g|Плагин: |y|Распределитель команд",
        "|g|Версия: |y|v" + plugin.container.version.orElse("null")
    )

    override val build: ItemStackBuilderEx.() -> Unit = {
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
            ToolInteractEvent.Type.LEFT_CLICK -> if(!e.sneaking) {

            } else {

            }
            ToolInteractEvent.Type.RIGHT_CLICK -> if(!e.sneaking) {
                plugin.guilib?.also { guilib ->
                    guilib.openGui(e.player, plugin.teamsgui)
                }
            } else {
                val team = plugin.selected.remove(e.player.uniqueId)
                if(team != null) {
                    e.player.sendMessage(TeamsMessages.unselectedTeam(team))
                }
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