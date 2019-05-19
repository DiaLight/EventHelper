package dialight.teams;

import dialight.compatibility.TeamBc;
import dialight.extensions.Colorizer;
import org.bukkit.scoreboard.Team;

public class TeamsMessages {

    private static final String pluginPrefix = Colorizer.apply("|go|Команды|gr|: ");

    public static String setScoreboard = pluginPrefix + Colorizer.apply("|g|Вам установлен скорбоард плагина");
    public static String setDefaultScoreboard = pluginPrefix + Colorizer.apply("|g|Вам установлен скорбоард по умолчанию");
    public static String cantSetDefaultScoreboard = pluginPrefix + Colorizer.apply("|r|Не могу установить скорбоард по умолчанию");
    public static String thisColorAlreadyInUse = pluginPrefix + Colorizer.apply("|r|Команда с таким цветом уже создана");
    public static String thisNameAlreadyInUse = pluginPrefix + Colorizer.apply("|r|Команда с таким названием уже создана");

    private static String teamName(Team team) {
        return TeamBc.of(team).getColor().toString() + team.getName();
    }

    public static String addTeam(Team team) {
        return pluginPrefix + Colorizer.apply("|g|Добавлена команда ") + teamName(team);
    }
    public static String removeTeam(Team team) {
        return pluginPrefix + Colorizer.apply("|g|Удалена команда ") + teamName(team);
    }

    public static String addTeamPlayer(Team team, String name) {
        return pluginPrefix + Colorizer.apply("|g|Добавлен игрок |w|" + name + "|g| в команду ") + teamName(team);
    }
    public static String removeTeamPlayer(Team team, String name) {
        return pluginPrefix + Colorizer.apply("|g|Удален игрок |w|" + name + "|g| из команды ") + teamName(team);
    }
    public static String selectedTeam(Team team) {
        return pluginPrefix + Colorizer.apply("|g|Выбрана команда ") + teamName(team) + Colorizer.apply("|g|. Теперь вы можете управлять участниками команды при помоши телепортера.");
    }
    public static String unselectedTeam(Team team) {
        return pluginPrefix + Colorizer.apply("|g|Команда ") + teamName(team) + Colorizer.apply("|g| больше не выбрана. Телепортер теперь работает как обычно.");
    }


}
