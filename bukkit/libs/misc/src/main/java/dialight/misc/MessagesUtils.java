package dialight.misc;

import dialight.misc.player.UuidPlayer;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

public class MessagesUtils {

    public static String players(Collection<? extends UuidPlayer> players) {
        int visible = 3;
        if(players.size() <= visible) return Colorizer.apply(players.stream().map(UuidPlayer::getName).collect(Collectors.joining("|g|, |w|")));
        StringBuilder sb = new StringBuilder();
        Iterator<? extends UuidPlayer> it = players.iterator();
        for (int i = 0; i < visible; i++) {
            if(i != 0) sb.append("|g|, |w|");
            UuidPlayer next = it.next();
            sb.append(next.getName());
        }
        sb.append("|g| и еще |w|");
        int left = players.size() - visible;
        sb.append(left);
        if(left == 1) sb.append("|g| игрок");
        else sb.append("|g| игроки");
        return Colorizer.apply(sb.toString());
    }

}
