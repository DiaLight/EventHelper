package dialight.modulelib

import jekarus.colorizer.Text_colorized
import org.spongepowered.api.text.Text

object ModuleMessages {

    var pluginPrefix = Text_colorized("|go|Модуль|gr|: ")

    fun notFound(id: String): Text = Text.of(pluginPrefix, Text_colorized("|r|Модуль с ID «|w|$id|r|» не зарегистрирован"))

    fun cantEnable(mod: Module): Text = Text.of(pluginPrefix, Text_colorized("|r|Ошибка включения модуля «|w|${mod.name}|r|»"))
    fun cantDisable(mod: Module): Text = Text.of(pluginPrefix, Text_colorized("|r|Ошибка выключения модуля «|w|${mod.name}|r|»"))

    fun successEnable(mod: Module): Text = Text.of(pluginPrefix, Text_colorized("|g|Модуль «|w|${mod.name}|g|» был активирован"))
    fun successDisable(mod: Module): Text = Text.of(pluginPrefix, Text_colorized("|g|Модуль «|w|${mod.name}|g|» был деактивирован"))

}