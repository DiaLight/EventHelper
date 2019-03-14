package dialight.teleporter

import jekarus.colorizer.Text_colorized
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.entity.living.player.User
import org.spongepowered.api.text.Text
import java.util.*

object TeleporterMessages {

    var pluginPrefix = Text_colorized("|go|Телепорт|gr|: ")

    fun YouHBTp(name: String): Text {
        return Text.of(pluginPrefix, Text_colorized("|w|$name |g|телепортировал вас"))
    }
    fun YouTp(online: Collection<Player>, offline: Collection<User>): Text {
        return Text.of(pluginPrefix, Text_colorized("|g|Телепортировано |w|${online.size}|g| онлайн-игроков и |w|${offline.size}|g| офлайн-игроков"))
    }


    var PlayersBaseIsEmpty = Text.of(pluginPrefix, Text_colorized("|r|Никто не выбран!"))
    var noPlayersTagged = Text.of(pluginPrefix, Text_colorized("|r|Некого телепортировать!"))
    val AllPlayersRemoved = Text.of(pluginPrefix, Text_colorized("|g|Список игроков очищен!"))

    fun untagged(names: List<String>): Text {
        return if (names.size == 1) {
            Text.of(pluginPrefix, Text_colorized("|y|Удалён: |w|" + names[0]))
        } else Text.of(pluginPrefix, Text_colorized("|y|Удалены: |w|$names"))
    }

    fun tagged(names: List<String>): Text {
        return if (names.size == 1) {
            Text.of(pluginPrefix, Text_colorized("|y|Добавлен: |w|" + names[0]))
        } else Text.of(pluginPrefix, Text_colorized("|y|Добавлены: |w|$names"))
    }

    fun targets(names: Collection<String>): Text {
        return Text.of(pluginPrefix, Text_colorized("|g|Выбраны: |w|$names"))
    }

    fun notFound(trgName: String): Text {
        return Text_colorized("Игрок с ником $trgName не найден")
    }
    fun notFound(uuid: UUID): Text {
        return Text_colorized("Игрок с UUID $uuid не найден")
    }

}
