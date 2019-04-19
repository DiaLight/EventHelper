package dialight.teams.gui

import dialight.extensions.closeInventoryLater
import dialight.extensions.itemStackOf
import dialight.guilib.View
import dialight.guilib.events.ItemClickEvent
import dialight.teams.TeamsTool
import dialight.teams.TeamsPlugin
import jekarus.colorizer.Text_colorized
import jekarus.colorizer.Text_colorizedList
import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.data.persistence.DataFormats
import org.spongepowered.api.item.ItemTypes
import org.spongepowered.common.data.util.DataQueries


class TeamsItem(val plugin: TeamsPlugin) : View.Item {

    override val item get() = itemStackOf(ItemTypes.BANNER) {
//        builder {
//            add(Keys.BANNER_PATTERNS, listOf(
//                PatternLayer.of(BannerPatternShapes.HALF_VERTICAL_MIRROR, DyeColors.YELLOW),
//                PatternLayer.of(BannerPatternShapes.HALF_VERTICAL, DyeColors.LIME),
//                PatternLayer.of(BannerPatternShapes.SQUARE_TOP_RIGHT, DyeColors.ORANGE),
//                PatternLayer.of(BannerPatternShapes.SQUARE_BOTTOM_LEFT, DyeColors.LIGHT_BLUE),
//                PatternLayer.of(BannerPatternShapes.SQUARE_BOTTOM_RIGHT, DyeColors.PURPLE),
//                PatternLayer.of(BannerPatternShapes.SQUARE_TOP_LEFT, DyeColors.RED)
//            ))
//        }
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
        displayName = Text_colorized("|a|Распределитель команд")
        lore.addAll(Text_colorizedList(
            "|a|ЛКМ|y|: получить инструмент",
            "|a|ПКМ|y|: открыть распределитель",
            "",
            "|g|Плагин: |y|Распределитель команд",
            "|g|Версия: |y|v" + plugin.container.version.orElse("null")
        ))
    }

    override fun onClick(event: ItemClickEvent) {
        val player = event.player
        when (event.type) {
            ItemClickEvent.Type.LEFT, ItemClickEvent.Type.SHIFT_LEFT -> {
                player.closeInventoryLater(plugin)
                plugin.toollib.giveTool(player, TeamsTool.ID)
            }
            ItemClickEvent.Type.RIGHT -> {
                plugin.guilib?.openGui(player, plugin.teamsgui)
            }
        }
    }

}