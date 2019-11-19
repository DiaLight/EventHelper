package dialight.maingui

import dialight.toollib.Tool
import dialight.toollib.events.ToolInteractEvent
import jekarus.colorizer.Colorizer
import org.spongepowered.api.item.ItemTypes

class MainGuiTool(val plugin: MainGuiPlugin) : Tool(ID) {

    companion object {
        val ID = "eventhelper"
    }

    override val type = ItemTypes.EMERALD
    override val title = Colorizer.apply("|a|Вещь всея Майнкрафта")
    override val lore = Colorizer.apply(
        mutableListOf(
            "|a|ПКМ|y|: Открыть «Инвентарь EventHelper»",
            "|a|Shift|y|+|a|ПКМ|y|: Открыть ранее открытый",
            "|y| инентарь",
            "|y|Аналог: |g|/eh"
        )
    )

    override fun onClick(e: ToolInteractEvent) {
        super.onClick(e)
        when(e.action) {
            ToolInteractEvent.Action.LEFT_CLICK -> {

            }
            ToolInteractEvent.Action.RIGHT_CLICK -> if(!e.sneaking) {
                plugin.guilib.clearStory(e.player)
                plugin.guilib.openGui(e.player, plugin.maingui)
            } else {
                if(!plugin.guilib.openLast(e.player)) {
//                    e.player.sendMessage(Text_colorized("Last gui is not found. Open tools gui"))
                    plugin.guilib.openGui(e.player, plugin.maingui)
                }
            }
        }
    }

}
