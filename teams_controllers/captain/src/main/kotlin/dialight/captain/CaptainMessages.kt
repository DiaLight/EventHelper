package dialight.captain

import jekarus.colorizer.Colorizer
import jekarus.colorizer.Text_colorized
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColor
import java.util.*

object CaptainMessages {

    var pluginPrefix = Text_colorized("|go|Система капитанов|gr|: ")


    val initializing = Text.of(pluginPrefix, Text_colorized("|y|Запуск системы капитанов"))
    val finish = Text.of(pluginPrefix, Text_colorized("|y|Распределение закончено"))
    val toolPluginDisabled = Text.of(pluginPrefix, Text_colorized("|r|Вы не можете использовать этот инструмент при выключенной системе капитанов"))
    val onlyActiveCaptain = Text.of(pluginPrefix, Text_colorized("|r|Использовать этот инструмент может только капитан, выбирающий игрока"))
    val youCantSelectHim = Text.of(pluginPrefix, Text_colorized("|r|Вы не можете выбрать этого игрока"))

    fun nextCaptain(name: String, color: TextColor): Text {
        return Text.of(pluginPrefix, Text_colorized("|y|Сейчас выбирает "), color, Text_colorized(name))
    }

    fun captainSelected(captain: String, target: String, color: TextColor): Text {
        return Text.of(pluginPrefix, Text_colorized("|y|Капитан "), color, Text_colorized("$captain|y| выбрал "), color, Text_colorized(target))
    }

    fun captainRandomSelected(captain: String, target: String, color: TextColor): Text {
        return Text.of(pluginPrefix, Text_colorized("|y|Капитан "), color, Text_colorized("$captain|y| никого не выбрал. Бог рандома выбрал за него "), color, Text_colorized(target))
    }


}
