package dialight.teams.captain;

import dialight.misc.ActionInvoker;
import dialight.misc.Colorizer;
import dialight.misc.player.UuidPlayer;
import org.bukkit.ChatColor;

public class CaptainMessages {

    public static String pluginPrefix = Colorizer.apply("|go|Система капитанов|gr|: ");


    public static String initializing = pluginPrefix + Colorizer.apply("|y|Запуск системы капитанов");
    public static String collectingMembers = pluginPrefix + Colorizer.apply("|y|Подсчет игроков...");
    public static String arenaBuilding = pluginPrefix + Colorizer.apply("|y|Постройка арены...");
    public static String arenaNotBuilt = pluginPrefix + Colorizer.apply("|r|Арена не построена");
    public static String kill = pluginPrefix + Colorizer.apply("|y|Распределение прервано");
    public static String finish = pluginPrefix + Colorizer.apply("|y|Распределение закончено");
    public static String toolPluginDisabled = pluginPrefix + Colorizer.apply("|r|Вы не можете использовать этот инструмент при выключенной системе капитанов");
    public static String onlyActiveCaptain = pluginPrefix + Colorizer.apply("|r|Использовать этот инструмент может только капитан, выбирающий игрока");
    public static String youCantSelectHim = pluginPrefix + Colorizer.apply("|r|Вы не можете выбрать этого игрока");
    public static String notSelector = pluginPrefix + Colorizer.apply("|r|Сейчас выбираете не вы");
    public static String notReadyYet = pluginPrefix + Colorizer.apply("|r|Пока еще не готово(");
    public static String notStarted = pluginPrefix + Colorizer.apply("|r|Сортировка игроков по командам не запущена");

    public static String nextCaptain(String name, ChatColor color) {
        return pluginPrefix + Colorizer.apply("|y|Сейчас выбирает ") + color + Colorizer.apply(name);
    }

    public static String captainSelected(UuidPlayer captain, UuidPlayer target, ChatColor color) {
        return pluginPrefix + Colorizer.apply("|y|Капитан ") + color + captain.getName() +
                Colorizer.apply("|y| выбрал ") + color + target.getName();
    }

    public static String captainRandomSelected(UuidPlayer captain, UuidPlayer target, ChatColor color) {
        return pluginPrefix + Colorizer.apply("|y|Капитан ") + color + captain.getName() +
                Colorizer.apply("|y| никого не выбрал. Бог рандома выбрал игрока ") + color + target.getName() +
                Colorizer.apply("|y| за него");
    }

    public static String error(String error) {
        return pluginPrefix + Colorizer.apply("|r|Ошибка: ") + error;
    }

    public static String nowConfirm(UuidPlayer target, ChatColor color) {
        return pluginPrefix + Colorizer.apply("|y|Вы выбрали ") + color + target.getName() +
                Colorizer.apply("|y|. Дропните баннер(Q) чтобы подтвердить выбор");
    }

    public static String alreadyStarted(Enum state) {
        return pluginPrefix + Colorizer.apply("|r|Сортировка игроков по командам уже находится в состоянии |y|") + state;
    }

    public static String paused(ActionInvoker invoker) {
        return pluginPrefix + Colorizer.apply("|y|") + invoker.getName() + " приостановил счет времени";
    }
    public static String unpaused(ActionInvoker invoker) {
        return pluginPrefix + Colorizer.apply("|y|") + invoker.getName() + " возобновил счет времени";
    }

}
