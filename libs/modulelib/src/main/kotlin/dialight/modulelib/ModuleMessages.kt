package dialight.modulelib

import jekarus.colorizer.Text_colorized
import org.spongepowered.api.text.Text

object ModuleMessages {

    var pluginPrefix = Text_colorized("|go|Модуль|gr|: ")

    fun notFound(id: String): Text = Text.of(pluginPrefix, Text_colorized("|r|Модуль с ID «$id» не зарегистрирован"))

}