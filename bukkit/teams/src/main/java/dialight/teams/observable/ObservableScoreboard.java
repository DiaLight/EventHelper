package dialight.teams.observable;

import dialight.observable.map.ObservableMap;
import dialight.misc.player.UuidPlayer;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;

public interface ObservableScoreboard {

    Scoreboard asBukkit();

    ObservableMap<String, ObservableTeam> teamsByName();

    ObservableMap<UuidPlayer, ObservableTeam> teamsByMember();

    String getId();

    @NotNull ObservableTeam getOrCreate(String name);

}
