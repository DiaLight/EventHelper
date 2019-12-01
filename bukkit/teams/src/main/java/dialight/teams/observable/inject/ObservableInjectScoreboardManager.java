package dialight.teams.observable.inject;

import dialight.inject.ScoreboardInject;
import dialight.inject.ScoreboardManagerInject;
import dialight.observable.map.ObservableMap;
import dialight.observable.map.ObservableMapWrapper;
import dialight.offlinelib.OfflineLibApi;
import dialight.misc.player.UuidPlayer;
import dialight.teams.observable.ObservableScoreboard;
import dialight.teams.observable.ObservableScoreboardManager;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ObservableInjectScoreboardManager implements ObservableScoreboardManager {

    private final OfflineLibApi offlinelib;
    private final ScoreboardManagerInject inject;
    private final ObservableInjectScoreboard mainScoreboard;
    private final Object mainScoreboardHandle;
    private final ObservableMap<UuidPlayer, ObservableInjectScoreboard> byPlayer = new ObservableMapWrapper<>();
    private final Map<Object, ObservableInjectScoreboard> scoreboardMap = new HashMap<>();
    private final Map<String, ObservableInjectScoreboard> byName = new HashMap<>();
    private String next_id;

    public ObservableInjectScoreboardManager(OfflineLibApi offlinelib, ScoreboardManager scoreboardManager) {
        this.offlinelib = offlinelib;
        this.inject = ScoreboardManagerInject.of(scoreboardManager);
        this.mainScoreboard = new ObservableInjectScoreboard(offlinelib, scoreboardManager.getMainScoreboard(), "main");
        this.mainScoreboardHandle = ScoreboardInject.of(mainScoreboard.asBukkit()).getHandle();
        byName.put(this.mainScoreboard.getId(), this.mainScoreboard);
    }

    private void onSetPlayerScoreboard(Player player, Scoreboard scoreboard) {
        ObservableInjectScoreboard ois = scoreboardMap.get(ScoreboardInject.of(scoreboard).getHandle());
        byPlayer.put(this.offlinelib.getUuidPlayer(player), ois);
    }

    private void onResetPlayerScoreboard(Player player, Scoreboard scoreboard) {
        ObservableInjectScoreboard ois = scoreboardMap.get(ScoreboardInject.of(scoreboard).getHandle());
        byPlayer.put(this.offlinelib.getUuidPlayer(player), mainScoreboard);
    }

    private void onAddScoreboard(Scoreboard scoreboard) {
        Object handle = ScoreboardInject.of(scoreboard).getHandle();
        if(handle == mainScoreboardHandle) return;
        if(next_id == null) next_id = String.format("%08X", handle.hashCode());
        ObservableInjectScoreboard injectScoreboard = new ObservableInjectScoreboard(offlinelib, scoreboard, next_id);
        scoreboardMap.put(handle, injectScoreboard);
        byName.put(next_id, injectScoreboard);
        injectScoreboard.inject();
        next_id = null;
    }

    private void onRemoveScoreboard(Scoreboard scoreboard) {
        ObservableInjectScoreboard injectScoreboard = scoreboardMap.remove(ScoreboardInject.of(scoreboard).getHandle());
        injectScoreboard.uninject();
    }

    @Override public void inject() {
        if(!inject.inject()) throw new RuntimeException("can't inject");
        inject.getPlayerBoards().onPut(this, this::onSetPlayerScoreboard);
        inject.getPlayerBoards().onRemove(this, this::onResetPlayerScoreboard);
        inject.getPlayerBoards().forEach(this::onSetPlayerScoreboard);
        inject.getScoreboards().onAdd(this, this::onAddScoreboard);
        inject.getScoreboards().onRemove(this, this::onRemoveScoreboard);
        inject.getScoreboards().forEach(this::onAddScoreboard);
        this.mainScoreboard.inject();
    }

    @Override public void uninject() {
        inject.getPlayerBoards().removeListeners(this);
        inject.getScoreboards().removeListeners(this);
        this.inject.uninject();
        for (ObservableInjectScoreboard scoreboard : byName.values()) scoreboard.uninject();
        this.mainScoreboard.uninject();
        scoreboardMap.clear();
        byName.clear();
        byPlayer.clear();
    }

    @Override public ObservableMap<UuidPlayer, ObservableInjectScoreboard> getByPlayer() {
        return byPlayer;
    }

    @Override public Map<String, ObservableInjectScoreboard> getByName() {
        return byName;
    }

    @NotNull @Override public ObservableScoreboard getOrCreate(String id) {
        ObservableInjectScoreboard injectScoreboard = byName.get(id);
        if(injectScoreboard != null) return injectScoreboard;
        this.next_id = id;
        Scoreboard scoreboard = inject.getScoreboardManager().getNewScoreboard();
        return byName.get(id);
    }

    @Nullable @Override public ObservableScoreboard get(String id) {
        return byName.get(id);
    }

    @NotNull @Override public ObservableScoreboard getMainScoreboard() {
        return mainScoreboard;
    }

    @NotNull @Override public ObservableScoreboard getPlayerScoreboard(UuidPlayer player) {
        ObservableInjectScoreboard scoreboard = byPlayer.get(player);
        if(scoreboard != null) return scoreboard;
        return getMainScoreboard();
    }

}
