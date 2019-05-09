package dialight.teleporter;

import dialight.extensions.Colorizer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TeleporterMessages {


    private static final String pluginPrefix = Colorizer.apply("|go|Телепорт|gr|: ");

    public static String YouHBTp(String name) {
        return pluginPrefix + Colorizer.apply("|w|" + name + " |g|телепортировал вас");
    }

    private static String players(Collection<? extends OfflinePlayer> players) {
        int visible = 3;
        if(players.size() <= visible) return Colorizer.apply(players.stream().map(OfflinePlayer::getName).collect(Collectors.joining("|g|, |w|")));
        StringBuilder sb = new StringBuilder();
        Iterator<? extends OfflinePlayer> it = players.iterator();
        for (int i = 0; i < visible; i++) {
            if(i != 0) sb.append("|g|, |w|");
            OfflinePlayer next = it.next();
            sb.append(next.getName());
        }
        sb.append("|g| и еще |w|");
        int left = players.size() - visible;
        sb.append(left);
        if(left == 1) sb.append("|g| игрок");
        else sb.append("|g| игроки");
        return Colorizer.apply(sb.toString());
    }
    public static String YouTp(Collection<Player> online, Collection<OfflinePlayer> offline) {
        StringBuilder sb = new StringBuilder();
        sb.append(pluginPrefix);
        if(!online.isEmpty()) {
            if (online.size() == 1) {
                if (offline.isEmpty()) sb.append(Colorizer.apply("|g|Телепортирован |w|"));
                else sb.append(Colorizer.apply("|g|Телепортирован онлайн-игрок |w|"));
                Player op = online.iterator().next();
                sb.append(op.getName());
            } else {
                if (offline.isEmpty()) sb.append(Colorizer.apply("|g|Телепортированы |w|"));
                else sb.append(Colorizer.apply("|g|Телепортированы онлайн-игроки |w|"));
                sb.append(players(online));
            }
        }
        if(!offline.isEmpty()) {
            if (offline.size() == 1) {
                if(online.isEmpty()) sb.append(Colorizer.apply("|g|Телепортирован офлайн-игрок |w|"));
                else sb.append(Colorizer.apply("|g| и офлайн-игрок |w|"));
                OfflinePlayer op = offline.iterator().next();
                sb.append(op.getName());
            } else {
                if(online.isEmpty()) sb.append(Colorizer.apply("|g|Телепортированы офлайн-игроки |w|"));
                else sb.append(Colorizer.apply("|g| и офлайн-игроки |w|"));
                sb.append(players(offline));
            }
        }
        return sb.toString();
    }


    public static String PlayersBaseIsEmpty = pluginPrefix + Colorizer.apply("|r|Никто не выбран!");
    public static String noPlayersSelected = pluginPrefix + Colorizer.apply("|r|Некого телепортировать!");
    public static String AllPlayersRemoved = pluginPrefix + Colorizer.apply("|g|Список игроков очищен!");

    public static String unselected(List<OfflinePlayer> offline) {
        if (offline.size() == 1) {
            OfflinePlayer op = offline.get(0);
            return pluginPrefix + Colorizer.apply("|y|Удалён: |w|" + op.getName());
        }
        List<String> offlineNames = offline.stream().map(OfflinePlayer::getName).collect(Collectors.toList());
        return pluginPrefix + Colorizer.apply("|y|Удалены: |w|") + offlineNames;
    }

    public static String selected(List<OfflinePlayer> offline) {
        if (offline.size() == 1) {
            OfflinePlayer op = offline.get(0);
            return pluginPrefix + Colorizer.apply("|y|Добавлен: |w|" + op.getName());
        }
        List<String> offlineNames = offline.stream().map(OfflinePlayer::getName).collect(Collectors.toList());
        return pluginPrefix + Colorizer.apply("|y|Добавлены: |w|") + offlineNames;
    }

    public static String targets(Collection<OfflinePlayer> players) {
        List<String> names = players.stream().map(OfflinePlayer::getName).collect(Collectors.toList());
        return pluginPrefix + Colorizer.apply("|g|Выбраны: |w|") + names;
    }

    public static String notFound(String trgName) {
        return pluginPrefix + Colorizer.apply("Игрок с ником " + trgName + " не найден");
    }
    public static String notFound(UUID uuid) {
        return pluginPrefix + Colorizer.apply("Игрок с UUID " + uuid + " не найден");
    }

}