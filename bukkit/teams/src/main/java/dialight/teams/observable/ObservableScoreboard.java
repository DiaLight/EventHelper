package dialight.teams.observable;

import dialight.observable.map.ObservableMap;
import dialight.offlinelib.UuidPlayer;
import dialight.teams.observable.inject.ObservableInjectTeam;
import org.bukkit.scoreboard.Scoreboard;

public interface ObservableScoreboard {

    Scoreboard getScoreboard();

    ObservableMap<String, ObservableInjectTeam> teamsByName();

    ObservableMap<UuidPlayer, ObservableInjectTeam> teamsByMember();

    String getId();

}
