package dialight.teams.captain.state;

import dialight.misc.player.UuidPlayer;
import dialight.observable.list.ObservableList;
import dialight.observable.list.ObservableListWrapper;
import dialight.observable.map.ObservableMap;
import dialight.observable.map.ObservableMapWrapper;
import dialight.observable.set.ObservableSet;
import dialight.offlinelib.SavedPlayerState;
import dialight.stateengine.StateEngineHandler;
import dialight.teams.captain.SortByCaptain;
import dialight.teams.captain.SortByCaptainState;
import dialight.teams.captain.utils.CaptainMap;
import dialight.teams.observable.ObservableScoreboard;
import dialight.teams.observable.ObservableTeam;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;

public class CollectMembersHandler extends StateEngineHandler<SortByCaptainState> {

    private final ObservableList<UuidPlayer> unsorted = new ObservableListWrapper<>();
    private final ObservableMap<UuidPlayer, String> result = new ObservableMapWrapper<>();
    private final CaptainMap captains = new CaptainMap();
    private final List<SavedPlayerState> savedStates = new ArrayList<>();
    private final Random rnd = new Random();

    public CollectMembersHandler(SortByCaptain proj) {
        super(proj, SortByCaptainState.COLLECT_MEMBERS);
    }

    @Nullable public UuidPlayer popRandomUnsorted() {
        if(unsorted.size() == 0) return null;
        return unsorted.remove(rnd.nextInt(unsorted.size()));
    }
    @Nullable public UuidPlayer peekRandomUnsorted() {
        if(unsorted.size() == 0) return null;
        return unsorted.get(rnd.nextInt(unsorted.size()));
    }

    public CaptainMap getCaptainMap() {
        return captains;
    }


    @Override public void enter() {
        unsorted.clear();
        result.clear();
        captains.clear();
        savedStates.clear();

        ObservableScoreboard mainScoreboard = proj.getTeams().getScoreboardManager().getMainScoreboard();
        ObservableScoreboard scoreboard = proj.getScoreboard();
        scoreboard.teamsByName().clear();

        ObservableSet<UUID> playerBlackList = proj.getTeams().getPlayerBlackList();
        for (Player player : proj.getPlugin().getServer().getOnlinePlayers()) {
            if (playerBlackList.contains(player.getUniqueId())) continue;
            unsorted.add(proj.getOfflineLib().getUuidPlayer(player));
            player.setScoreboard(scoreboard.asBukkit());
        }

//        UuidPlayer diaLight = proj.getOfflineLib().getUuidPlayer("DiaLight");
//        if(diaLight != null) {
//            proj.getCaptainsByTeam().put("GreenTeam", diaLight);
//        }

        for (String teamName : proj.getTeams().getTeamWhiteList()) {
            ObservableTeam mainTeam = mainScoreboard.teamsByName().get(teamName);
            ObservableTeam team = scoreboard.getOrCreate(teamName);
            team.color().setValue(mainTeam.color().getValue());
            UuidPlayer captain = proj.getCaptainsByTeam().get(teamName);
            if(captain == null) {
                captain = popRandomUnsorted();
            } else {
                unsorted.remove(captain);
            }
            if(captain == null) throw new IllegalStateException("Not enough players");
            captains.put(captain, teamName);
            result.put(captain, team.getName());
            team.getMembers().add(captain);
        }

        if(unsorted.isEmpty()) throw new IllegalStateException("Not enough players");
//        if(unsorted.size() < captains.size()) throw new IllegalStateException("Not enough players");

        // save states
        forEachMember(player -> {
            SavedPlayerState state = new SavedPlayerState(player);
            state.collect();
            savedStates.add(state);
        });

        fireDone();
    }

    @Override public void tick(int tick) {

    }

    @Override public void leave() {

    }

    @Override public void clear() {
        ObservableScoreboard mainScoreboard = proj.getTeams().getScoreboardManager().getMainScoreboard();
        forEachMember(member -> {
            Player player = member.getPlayer();
            if(player == null) return;
            player.setScoreboard(mainScoreboard.asBukkit());
        });
        ObservableScoreboard scoreboard = proj.getScoreboard();
        scoreboard.teamsByName().clear();
        for (SavedPlayerState state : savedStates) {
            state.apply();
        }
    }

    public void broadcastMembers(String msg) {
        forEachMember(member -> {
            Player player = member.getPlayer();
            if(player == null) return;
            player.sendMessage(msg);
        });
    }

    public void forEachMember(Consumer<UuidPlayer> op) {
        for (UuidPlayer member : unsorted) {
            op.accept(member);
        }
        for (UuidPlayer member : result.keySet()) {
            op.accept(member);
        }
    }

    public ObservableList<UuidPlayer> getUnsorted() {
        return unsorted;
    }

    public ObservableMap<UuidPlayer, String> getResult() {
        return result;
    }

    public void select(@NotNull UuidPlayer captain, @NotNull UuidPlayer selected) {
        unsorted.remove(selected);
        result.put(selected, captains.getTeamByCaptain(captain));
    }

    public boolean isMember(UuidPlayer uuidPlayer) {
        if (result.containsKey(uuidPlayer)) return true;
        return unsorted.contains(uuidPlayer);
    }

}
