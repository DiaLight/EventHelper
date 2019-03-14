package dialight.eventhelper

import dialight.toollib.Tool
import dialight.toollib.events.ToolInteractEvent
import jekarus.colorizer.Colorizer
import org.spongepowered.api.item.ItemTypes

class EventHelperTool(val plugin: EventHelperPlugin) : Tool(ID) {

    companion object {
        val ID = "eventhelper"
    }

    override val title = Colorizer.apply("|a|Вещь всея Майнкрафта")
    override val lore = Colorizer.apply(
        mutableListOf(
//            "|a|ЛКМ|y|: Открыть «Мегамодули»",
            "|a|ПКМ|y|: Открыть «Мегаинвентарь»",
            "|a|Shift|y|+|a|ПКМ|y|: Открыть ранее открытый интерфейс",
            "|y|Аналог: |g|/eh"
        )
    )
    override val type = ItemTypes.EMERALD

    init {

    }

    override fun onClick(e: ToolInteractEvent) {
        super.onClick(e)
        when(e.action) {
            ToolInteractEvent.Type.LEFT_CLICK -> {

            }
            ToolInteractEvent.Type.RIGHT_CLICK -> if(!e.sneaking) {
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
