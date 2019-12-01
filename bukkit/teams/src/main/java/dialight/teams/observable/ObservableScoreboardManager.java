package dialight.teams.observable;

import dialight.observable.map.ObservableMap;
import dialight.misc.player.UuidPlayer;
import dialight.teams.observable.inject.ObservableInjectScoreboard;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface ObservableScoreboardManager {

    void inject();
    void uninject();

    ObservableMap<UuidPlayer, ObservableInjectScoreboard> getByPlayer();
    Map<String, ObservableInjectScoreboard> getByName();

    @NotNull ObservableScoreboard getOrCreate(String id);
    @Nullable ObservableScoreboard get(String id);

    @NotNull ObservableScoreboard getMainScoreboard();
    @NotNull ObservableScoreboard getPlayerScoreboard(UuidPlayer player);

}
