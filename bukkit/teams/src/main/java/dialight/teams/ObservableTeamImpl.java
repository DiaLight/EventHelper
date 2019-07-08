package dialight.teams;

import dialight.compatibility.TeamBc;
import dialight.extensions.ColorConverter;
import dialight.observable.collection.ObservableCollection;
import dialight.observable.map.ObservableMap;
import dialight.observable.map.ObservableMapWrapper;
import dialight.offlinelib.OfflineLibApi;
import dialight.offlinelib.UuidPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.UUID;

public class ObservableTeamImpl implements ObservableTeam {

    private final Teams proj;
    private final String name;
    private final Team team;
    private final ObservableMap<UUID, UuidPlayer> membersMap = new ObservableMapWrapper<>();
    private final ObservableCollection<UuidPlayer> members;

    public ObservableTeamImpl(Teams proj, Team team) {
        this.proj = proj;
        this.name = team.getName();
        this.team = team;
        this.members = new MembersObservable(team, membersMap);
    }

    @Override public String getName() {
        return this.name;
    }

    @Override public ChatColor getColor() {
        return TeamBc.of(team).getColor();
    }

    @Override public Color getLeatherColor() {
        return ColorConverter.toLeatherColor(getColor());
    }

    @Override public ObservableCollection<UuidPlayer> getMembers() {
        return members;
    }

    @Override public Team getTeam() {
        return team;
    }

    @Override public void clearOfflines() {
        OfflineLibApi offlinelib = proj.getOfflinelib();
        for (String name : new ArrayList<>(team.getEntries())) {
            OfflinePlayer op = offlinelib.getOfflinePlayerByName(name);
//            UuidPlayer up;
//            if(op != null) {
//                up = offlinelib.getUuidPlayer(op.getUniqueId());
//            } else {
//                up = offlinelib.createUuidNotPlayer(name);
//            }
            if(op != null) team.removeEntry(name);
        }
    }

    public void onAddMember(UuidPlayer op) {
        membersMap.put(op.getUuid(), op);
    }

    public void onRemoveMember(UuidPlayer op) {
        membersMap.remove(op.getUuid());
    }

    public void onUpdate() {
        // update color
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObservableTeamImpl that = (ObservableTeamImpl) o;

        return name.equals(that.name);
    }

    @Override public int hashCode() {
        return name.hashCode();
    }

}
