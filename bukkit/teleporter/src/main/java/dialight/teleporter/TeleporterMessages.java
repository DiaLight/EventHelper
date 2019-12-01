package dialight.teleporter;

import dialight.misc.Colorizer;
import dialight.misc.MessagesUtils;
import dialight.misc.player.UuidPlayer;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TeleporterMessages {


    private static final String pluginPrefix = Colorizer.apply("|go|Телепорт|gr|: ");

    public static String YouHBTp(String name) {
        return pluginPrefix + Colorizer.apply("|w|" + name + " |g|телепортировал вас");
    }

    public static String YouTp(Collection<UuidPlayer> online, Collection<UuidPlayer> offline) {
        StringBuilder sb = new StringBuilder();
        sb.append(pluginPrefix);
        if(!online.isEmpty()) {
            if (online.size() == 1) {
                if (offline.isEmpty()) sb.append(Colorizer.apply("|g|Телепортирован |w|"));
                else sb.append(Colorizer.apply("|g|Телепортирован онлайн-игрок |w|"));
                UuidPlayer op = online.iterator().next();
                sb.append(op.getName());
            } else {
                if (offline.isEmpty()) sb.append(Colorizer.apply("|g|Телепортированы |w|"));
                else sb.append(Colorizer.apply("|g|Телепортированы онлайн-игроки |w|"));
                sb.append(MessagesUtils.players(online));
            }
        }
        if(!offline.isEmpty()) {
            if (offline.size() == 1) {
                if(online.isEmpty()) sb.append(Colorizer.apply("|g|Телепортирован офлайн-игрок |w|"));
                else sb.append(Colorizer.apply("|g| и офлайн-игрок |w|"));
                UuidPlayer op = offline.iterator().next();
                sb.append(op.getName());
            } else {
                if(online.isEmpty()) sb.append(Colorizer.apply("|g|Телепортированы офлайн-игроки |w|"));
                else sb.append(Colorizer.apply("|g| и офлайн-игроки |w|"));
                sb.append(MessagesUtils.players(offline));
            }
        }
        return sb.toString();
    }


    public static String PlayersBaseIsEmpty = pluginPrefix + Colorizer.apply("|r|Никто не выбран!");
    public static String noPlayersSelected = pluginPrefix + Colorizer.apply("|r|Некого телепортировать!");
    public static String AllPlayersRemoved = pluginPrefix + Colorizer.apply("|g|Список игроков очищен!");

    public static String unselected(List<UuidPlayer> offline) {
        if (offline.size() == 1) {
            UuidPlayer up = offline.get(0);
            return pluginPrefix + Colorizer.apply("|y|Удалён: |w|" + up.getName());
        }
        List<String> offlineNames = offline.stream().map(UuidPlayer::getName).collect(Collectors.toList());
        return pluginPrefix + Colorizer.apply("|y|Удалены: |w|") + offlineNames;
    }

    public static String selected(List<UuidPlayer> offline) {
        if (offline.size() == 1) {
            UuidPlayer up = offline.get(0);
            return pluginPrefix + Colorizer.apply("|y|Добавлен: |w|" + up.getName());
        }
        List<String> offlineNames = offline.stream().map(UuidPlayer::getName).collect(Collectors.toList());
        return pluginPrefix + Colorizer.apply("|y|Добавлены: |w|") + offlineNames;
    }

    public static String targets(Collection<UuidPlayer> players) {
        List<String> names = players.stream().map(UuidPlayer::getName).collect(Collectors.toList());
        return pluginPrefix + Colorizer.apply("|g|Выбраны: |w|") + names;
    }

    public static String notFound(String trgName) {
        return pluginPrefix + Colorizer.apply("Игрок с ником " + trgName + " не найден");
    }
    public static String notFound(UUID uuid) {
        return pluginPrefix + Colorizer.apply("Игрок с UUID " + uuid + " не найден");
    }

}
