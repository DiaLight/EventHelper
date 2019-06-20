package dialight.teams;

import dialight.observable.collection.ObservableCollection;
import dialight.observable.map.ObservableMap;
import dialight.observable.map.ObservableMapWrapper;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.UUID;

public class ObservableTeamImpl implements ObservableTeam {

    private final Teams proj;
    private final String name;
    private final Team team;
    private final ObservableMap<UUID, OfflinePlayer> membersMap = new ObservableMapWrapper<>();
    private final ObservableCollection<OfflinePlayer> members;

    public ObservableTeamImpl(Teams proj, Team team) {
        this.proj = proj;
        this.name = team.getName();
        this.team = team;
        this.members = new MembersObservable(team, membersMap);
    }

    @Override public String getName() {
        return this.name;
    }

    @Override public ObservableCollection<OfflinePlayer> getMembers() {
        return members;
    }

    @Override public Team getTeam() {
        return team;
    }

    @Override public void clear() {
        for (String name : new ArrayList<>(team.getEntries())) {
            team.removeEntry(name);
        }
    }

    public void onAddMember(OfflinePlayer op) {
        membersMap.put(op.getUniqueId(), op);
    }

    public void onRemoveMember(OfflinePlayer op) {
        membersMap.remove(op.getUniqueId());
    }

    public void onUpdate() {
        // update color
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObservableTeamImpl that = (ObservableTeamImpl) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
