package dialight.teams.observable.listener;

import dialight.observable.collection.ObservableCollection;
import dialight.observable.map.ObservableMap;
import dialight.observable.map.ObservableMapWrapper;
import dialight.offlinelib.UuidPlayer;
import dialight.teams.observable.ObservableTeam;
import dialight.teams.Teams;
import dialight.teams.event.TeamEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TeamsListenerObserver implements TeamHandler {

    private final ObservableMap<String, ObservableTeamImpl> teamsMap = new ObservableMapWrapper<>();
    private final ObservableCollection<ObservableTeamImpl> teamsInternal = teamsMap.asCollectionObaervable(ObservableTeamImpl::getName);
    private final ObservableCollection<ObservableTeamImpl> teamsImmutable = teamsMap.asImmutableCollectionObaervable(ObservableTeamImpl::getName);

    private final Collection<Consumer<ObservableTeam>> onUpdate = new LinkedList<>();
    private final Collection<BiConsumer<ObservableTeam, String>> onMemberJoin = new LinkedList<>();
    private final Collection<BiConsumer<ObservableTeam, String>> onMemberLeave = new LinkedList<>();

    private final Teams proj;
    private Scoreboard scoreboard;
    private TeamsListener listener;

    public TeamsListenerObserver(Teams proj) {
        this.listener = new TeamsListener(proj, this);
        this.scoreboard = proj.getPlugin().getServer().getScoreboardManager().getMainScoreboard();
        this.proj = proj;
    }

    public void inject() {
        listener.start();
    }

    public void uninject() {
        listener.stop();
    }

    @Nullable public ObservableTeam get(String name) {
        return teamsMap.get(name);
    }
    @Deprecated public void onTeamUpdate(Consumer<ObservableTeam> op) {
        onUpdate.add(op);
    }
    public void unregisterOnTeamUpdate(Consumer<ObservableTeam> op) {
        onUpdate.add(op);
    }
    @Deprecated public void onMemberJoin(BiConsumer<ObservableTeam, String> op) {
        onMemberJoin.add(op);
    }
    public void unregisterOnMemberJoin(BiConsumer<ObservableTeam, String> op) {
        onMemberJoin.remove(op);
    }
    @Deprecated public void onMemberLeave(BiConsumer<ObservableTeam, String> op) {
        onMemberLeave.add(op);
    }
    public void unregisterOnMemberLeave(BiConsumer<ObservableTeam, String> op) {
        onMemberLeave.remove(op);
    }

    @Override public void update() {
//        List<UuidPlayer> notInTeamToAdd = new ArrayList<>(offlinelib.getOffline());
//        List<UuidPlayer> notInTeamToRemove = new ArrayList<>();
        Scoreboard sb = proj.getPlugin().getServer().getScoreboardManager().getMainScoreboard();
        List<String> teamsToRemove = new ArrayList<>(teamsMap.keySet());
        for (Team team : sb.getTeams()) {
            ObservableTeamImpl oteam = new ObservableTeamImpl(team);
            if(teamsMap.putIfAbsent(team.getName(), oteam) == null) {
                for (String member : team.getEntries()) {
                    OfflinePlayer op = proj.getOfflinelib().getOfflinePlayerByName(member);
                    UuidPlayer up;
                    if(op != null) {
                        up = proj.getOfflinelib().getUuidPlayer(op.getUniqueId());
                    } else {
                        up = proj.getOfflinelib().getOrCreateNotPlayer(member);
                    }
                    oteam.addMember(up);
//                    notInTeamToRemove.add(up);
//                    notInTeamToAdd.remove(up);
                }
            }
            teamsToRemove.remove(team.getName());
        }
        for (String name : teamsToRemove) {
            teamsMap.remove(name);
        }
    }

    @Override
    public void onEvent(TeamEvent event) {
        switch (event.getType()) {
            case ADD: {
                Team team = scoreboard.getTeam(event.getName());
                ObservableTeamImpl ot = new ObservableTeamImpl(team);
                if(!teamsInternal.contains(ot)) teamsInternal.add(ot);
            } break;
            case REMOVE: {
                ObservableTeamImpl oteam = teamsMap.remove(event.getName());
            } break;
            case UPDATE: {
                ObservableTeamImpl ot = teamsMap.get(event.getName());
                if(ot != null) {
                    ot.onUpdate();
                    for (Consumer<ObservableTeam> op : onUpdate) {
                        op.accept(ot);
                    }
                } else {
                    System.out.println("Unexpected team update");
                }
            } break;
            case ADD_MEMBERS: {
                ObservableTeamImpl oteam = teamsMap.get(event.getName());
                TeamEvent.Members membersEvent = (TeamEvent.Members) event;
//                System.out.println("ADD_MEMBERS " + event.getName() + " " + membersEvent.getMembers());
                for (String member : membersEvent.getMembers()) {
                    OfflinePlayer op = proj.getOfflinelib().getOfflinePlayerByName(member);
                    UuidPlayer up;
                    if(op != null) {
                        up = proj.getOfflinelib().getUuidPlayer(op.getUniqueId());
                    } else {
                        up = proj.getOfflinelib().getOrCreateNotPlayer(member);
                    }
                    oteam.addMember(up);
                    for (BiConsumer<ObservableTeam, String> consumer : onMemberJoin) {
                        consumer.accept(oteam, member);
                    }
                }
            } break;
            case REMOVE_MEMBERS: {
                ObservableTeamImpl oteam = teamsMap.get(event.getName());
                TeamEvent.Members membersEvent = (TeamEvent.Members) event;
//                System.out.println("REMOVE_MEMBERS " + event.getName() + " " + membersEvent.getMembers());
                for (String member : membersEvent.getMembers()) {
                    OfflinePlayer op = proj.getOfflinelib().getOfflinePlayerByName(member);
                    UuidPlayer up;
                    if(op != null) {
                        up = proj.getOfflinelib().getUuidPlayer(op.getUniqueId());
                    } else {
                        up = proj.getOfflinelib().getOrCreateNotPlayer(member);
                    }
                    oteam.removeMember(up);
                    for (BiConsumer<ObservableTeam, String> consumer : onMemberLeave) {
                        consumer.accept(oteam, member);
                    }
                }
            } break;
        }
    }

}
