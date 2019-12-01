package dialight.teams.observable.inject;

import dialight.inject.ScoreboardInject;
import dialight.observable.map.ObservableMap;
import dialight.observable.map.ObservableMapWrapper;
import dialight.observable.map.WriteProxyObservableMap;
import dialight.offlinelib.OfflineLibApi;
import dialight.misc.player.UuidPlayer;
import dialight.teams.observable.ObservableScoreboard;
import dialight.teams.observable.ObservableTeam;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ObservableInjectScoreboard implements ObservableScoreboard {

    private final OfflineLibApi offlinelib;
    private final ScoreboardInject inject;
    private final String id;
    private final ObservableMap<String, ObservableInjectTeam> byName = new ObservableMapWrapper<>();
    private final WriteProxyObservableMap<String, ObservableInjectTeam> byName_api = new WriteProxyObservableMap<>(byName);
    private final ObservableMap<UuidPlayer, ObservableInjectTeam> byPlayer = new ObservableMapWrapper<>();


    public ObservableInjectScoreboard(OfflineLibApi offlinelib, Scoreboard scoreboard, String id) {
        this.offlinelib = offlinelib;
        this.inject = ScoreboardInject.of(scoreboard);
        this.id = id;
        byName_api.setProxyOnPut(this::onPutTeam);
        byName_api.setProxyOnRemove(this::onRemoveTeam);
    }

    private ObservableInjectTeam onPutTeam(String name, ObservableInjectTeam team) {
        throw new RuntimeException("create team by vanilla api");
    }

    private ObservableInjectTeam onRemoveTeam(String name) {
        ObservableInjectTeam team = byName.get(name);
        team.asBukkit().unregister();
        return team;
    }

    @Override public Scoreboard asBukkit() {
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
        byPlayer.put(this.offlinelib.getUuidPlayer(member), injectTeam);
    }

    private void onMemberLeave(String member, Team team) {
        ObservableInjectTeam injectTeam = byPlayer.remove(this.offlinelib.getUuidPlayer(member));
        Objects.requireNonNull(injectTeam);
    }

    @Override public ObservableMap<String, ObservableTeam> teamsByName() {
        return (ObservableMap<String, ObservableTeam>) (ObservableMap) byName_api;
    }

    @Override public ObservableMap<UuidPlayer, ObservableTeam> teamsByMember() {
        return (ObservableMap<UuidPlayer, ObservableTeam>) (ObservableMap) byPlayer;
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


    @NotNull @Override public ObservableTeam getOrCreate(String name) {
        ObservableInjectTeam team = byName.get(name);
        if(team != null) return team;
        inject.getScoreboard().registerNewTeam(name);
        return byName.get(name);
    }

}
