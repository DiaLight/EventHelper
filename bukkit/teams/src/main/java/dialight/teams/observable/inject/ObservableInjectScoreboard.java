package dialight.teams.observable.inject;

import dialight.inject.ScoreboardInject;
import dialight.observable.map.ObservableMap;
import dialight.observable.map.ObservableMapWrapper;
import dialight.offlinelib.OfflineLibApi;
import dialight.offlinelib.UuidPlayer;
import dialight.teams.observable.ObservableScoreboard;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

public class ObservableInjectScoreboard implements ObservableScoreboard {

    private final OfflineLibApi offlinelib;
    private final ScoreboardInject inject;
    private final String id;
    private final ObservableMap<String, ObservableInjectTeam> byName = new ObservableMapWrapper<>();
    private final ObservableMap<UuidPlayer, ObservableInjectTeam> byPlayer = new ObservableMapWrapper<>();


    public ObservableInjectScoreboard(OfflineLibApi offlinelib, Scoreboard scoreboard, String id) {
        this.offlinelib = offlinelib;
        this.inject = ScoreboardInject.of(scoreboard);
        this.id = id;
    }

    @Override public Scoreboard getScoreboard() {
        return inject.getScoreboard();
    }

    private void onAddTeam(String name, Team team) {
        ObservableInjectTeam injectTeam = new ObservableInjectTeam(this.offlinelib, team);
        injectTeam.inject();
        byName.put(name, injectTeam);
    }

    private void onRemoveTeam(String name, Team team) {
        ObservableInjectTeam injectTeam = byName.remove(name);
        Objects.requireNonNull(injectTeam);
        injectTeam.uninject();
    }

    private void onMemberJoin(String member, Team team) {
        ObservableInjectTeam injectTeam = byName.get(team.getName());
        Objects.requireNonNull(injectTeam);
        byPlayer.put(this.offlinelib.getOrCreateNotPlayer(member), injectTeam);
    }

    private void onMemberLeave(String member, Team team) {
        ObservableInjectTeam injectTeam = byPlayer.remove(this.offlinelib.getOrCreateNotPlayer(member));
        Objects.requireNonNull(injectTeam);
    }

    @Override public ObservableMap<String, ObservableInjectTeam> teamsByName() {
        return byName;
    }

    @Override public ObservableMap<UuidPlayer, ObservableInjectTeam> teamsByMember() {
        return byPlayer;
    }

    @Override public String getId() {
        return id;
    }

    public void inject() {
        if(!inject.inject()) throw new RuntimeException("can't inject");
        inject.getTeamsByName().onPut(this, this::onAddTeam);
        inject.getTeamsByName().onRemove(this, this::onRemoveTeam);
        inject.getTeamsByName().forEach(this::onAddTeam);
        inject.getTeamsByPlayer().onPut(this, this::onMemberJoin);
        inject.getTeamsByPlayer().onRemove(this, this::onMemberLeave);
        inject.getTeamsByPlayer().forEach(this::onMemberJoin);
    }
    public void uninject() {
        inject.getTeamsByName().removeListeners(this);
        inject.getTeamsByPlayer().removeListeners(this);
        this.inject.uninject();
        for (ObservableInjectTeam value : byName.values()) value.uninject();
        byName.clear();
        byPlayer.clear();
    }

}
