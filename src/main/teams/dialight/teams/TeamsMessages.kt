package dialight.teams

import jekarus.colorizer.Text_colorized
import org.spongepowered.api.scoreboard.Team
import org.spongepowered.api.text.Text

object TeamsMessages {

    var pluginPrefix = Text_colorized("|go|Команды|gr|: ")

    val setScoreboard = Text.of(pluginPrefix, Text_colorized("|g|Вам установлен скорбоард плагина"))
    val setDefaultScoreboard = Text.of(pluginPrefix, Text_colorized("|g|Вам установлен скорбоард по умолчанию"))
    val cantSetDefaultScoreboard = Text.of(pluginPrefix, Text_colorized("|r|Не могу установить скорбоард по умолчанию"))

    fun addTeam(team: Team) = Text.of(pluginPrefix, Text_colorized("|g|Добавлена команда "), team.color, team.name)
    fun removeTeam(team: Team) = Text.of(pluginPrefix, Text_colorized("|g|Удалена команда "), team.color, team.name)

    fun addTeamPlayer(team: Team, name: String) = Text.of(pluginPrefix, Text_colorized("|g|Добавлен игрок |w|$name|g| в команду "), team.color, team.name)
    fun removeTeamPlayer(team: Team, name: String) = Text.of(pluginPrefix, Text_colorized("|g|Удален игрок |w|$name|g| из команды "), team.color, team.name)
    fun selectedTeam(team: Team) = Text.of(pluginPrefix, Text_colorized("|g|Выбрана команда "), team.color, team.name, Text_colorized("|g|. Теперь вы можете управлять участниками команды при помоши телепортера."))
    fun unselectedTeam(team: Team) = Text.of(pluginPrefix, Text_colorized("|g|Команда "), team.color, team.name, Text_colorized("|g| больше не выбрана. Телепортер теперь работает как обычно."))


}