package dialight.toollib

import jekarus.colorizer.Text_colorized
import org.spongepowered.api.text.Text

object ToolMessages {

    var pluginPrefix = Text_colorized("|go|Инструмент|gr|: ")

    fun notFound(id: String): Text = Text.of(pluginPrefix, Text_colorized("|r|Инструмент с ID «$id» не зарегистрирован"))


}