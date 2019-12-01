package dialight.freezer;

import dialight.misc.Colorizer;
import dialight.misc.MessagesUtils;
import dialight.misc.player.UuidPlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

/**
 * Created by DiaLight on 29.04.2016.
 */
public final class FreezerMessages {

    private static final String pluginPrefix = Colorizer.apply("|go|Замораживатель|gr|: ");

    public static String selfFreeze = pluginPrefix + Colorizer.apply("|y|ты заморозил сам себя");
    public static String selfUnfreeze = pluginPrefix + Colorizer.apply("|y|ты разморозил сам себя");
    public static String unfreezeByReload = pluginPrefix + Colorizer.apply("|y|Тебя разморозило из-за перезагрузки плагина");
    public static String youFrozen = pluginPrefix + Colorizer.apply("|y|Ты находишься в замороженном состоянии");
    public static String unfreezeAll = pluginPrefix + Colorizer.apply("|y|Все игроки были разморожены");

    public static String youFreezed(UuidPlayer target) {
        return pluginPrefix + Colorizer.apply("|y|ты заморозил: |w|" + target.getName());
    }
    public static String youUnfreezed(UuidPlayer target) {
        return pluginPrefix + Colorizer.apply("|y|ты разморозил: |w|" + target.getName());
    }

    public static String youFreezed(Collection<UuidPlayer> online, Collection<UuidPlayer> offline) {
        StringBuilder sb = new StringBuilder();
        sb.append(pluginPrefix);
        if(!online.isEmpty()) {
            if (online.size() == 1) {
                if (offline.isEmpty()) sb.append(Colorizer.apply("|g|Заморожен |w|"));
                else sb.append(Colorizer.apply("|g|Заморожен онлайн-игрок |w|"));
                UuidPlayer op = online.iterator().next();
                sb.append(op.getName());
            } else {
                if (offline.isEmpty()) sb.append(Colorizer.apply("|g|Заморожены |w|"));
                else sb.append(Colorizer.apply("|g|Заморожены онлайн-игроки |w|"));
                sb.append(MessagesUtils.players(online));
            }
        }
        if(!offline.isEmpty()) {
            if (offline.size() == 1) {
                if(online.isEmpty()) sb.append(Colorizer.apply("|g|Заморожен офлайн-игрок |w|"));
                else sb.append(Colorizer.apply("|g| и офлайн-игрок |w|"));
                UuidPlayer op = offline.iterator().next();
                sb.append(op.getName());
            } else {
                if(online.isEmpty()) sb.append(Colorizer.apply("|g|Заморожены офлайн-игроки |w|"));
                else sb.append(Colorizer.apply("|g| и офлайн-игроки |w|"));
                sb.append(MessagesUtils.players(offline));
            }
        }
        return sb.toString();
    }

    public static String youUnfreezed(Collection<UuidPlayer> online, Collection<UuidPlayer> offline) {
        StringBuilder sb = new StringBuilder();
        sb.append(pluginPrefix);
        if(!online.isEmpty()) {
            if (online.size() == 1) {
                if (offline.isEmpty()) sb.append(Colorizer.apply("|g|Разморожен |w|"));
                else sb.append(Colorizer.apply("|g|Разморожен онлайн-игрок |w|"));
                UuidPlayer op = online.iterator().next();
                sb.append(op.getName());
            } else {
                if (offline.isEmpty()) sb.append(Colorizer.apply("|g|Разморожены |w|"));
                else sb.append(Colorizer.apply("|g|Разморожены онлайн-игроки |w|"));
                sb.append(MessagesUtils.players(online));
            }
        }
        if(!offline.isEmpty()) {
            if (offline.size() == 1) {
                if(online.isEmpty()) sb.append(Colorizer.apply("|g|Разморожен офлайн-игрок |w|"));
                else sb.append(Colorizer.apply("|g| и офлайн-игрок |w|"));
                UuidPlayer op = offline.iterator().next();
                sb.append(op.getName());
            } else {
                if(online.isEmpty()) sb.append(Colorizer.apply("|g|Разморожены офлайн-игроки |w|"));
                else sb.append(Colorizer.apply("|g| и офлайн-игроки |w|"));
                sb.append(MessagesUtils.players(offline));
            }
        }
        return sb.toString();
    }

    public static String youHbFreezed(UuidPlayer invoker) {
        return pluginPrefix + Colorizer.apply("|y|тебя заморозил |w|" + invoker.getName());
    }
    public static String youHbUnfreezed(UuidPlayer invoker) {
        return pluginPrefix + Colorizer.apply("|y|тебя разморозил |w|" + invoker.getName());
    }

    public static String youHbFreezed(Plugin invoker) {
        return pluginPrefix + Colorizer.apply("|y|Тебя заморозило плагином |w|" + invoker.getName());
    }
    public static String youHbUnfreezed(Plugin invoker) {
        return pluginPrefix + Colorizer.apply("|y|Тебя разморозило плагином |w|" + invoker.getName());
    }

    public static String youHbFreezed(ConsoleCommandSender invoker) {
        return pluginPrefix + Colorizer.apply("|y|Тебя заморозили из консоли");
    }
    public static String youHbUnfreezed(ConsoleCommandSender invoker) {
        return pluginPrefix + Colorizer.apply("|y|Тебя разморозили из консоли");
    }

}
