package dialight.teams.observable.listener;

import dialight.observable.ObservableObject;
import dialight.observable.set.ObservableSet;
import dialight.observable.set.ObservableSetWrapper;
import dialight.observable.set.WriteProxyObservableSet;
import dialight.misc.player.UuidPlayer;
import dialight.teams.observable.ObservableTeam;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

public class ObservableTeamImpl extends ObservableTeam {

    private final ObservableSet<UuidPlayer> members = new ObservableSetWrapper<>();
    private final WriteProxyObservableSet<UuidPlayer> membersApi = new WriteProxyObservableSet<>(members);

    public ObservableTeamImpl(Team team) {
        super(team);
        membersApi.setProxyOnAdd(this::addMember);
        membersApi.setProxyOnRemove(this::removeMember);
    }


    public boolean addMember(UuidPlayer element) {
        if(this.members.contains(element)) return false;
        String name = element.getName();
        if(name == null) return false;
        team.addEntry(name);
        return true;
    }

    public boolean removeMember(UuidPlayer element) {
        if(!this.members.contains(element)) return false;
        String name = ((UuidPlayer) element).getName();
        if(name == null) return false;
        team.removeEntry(name);
        return true;
    }

    @Override public ObservableSet<UuidPlayer> getMembers() {
        return membersApi;
    }

    @Override public ObservableObject<String> displayName() {
        return null;
    }

    @Override public ObservableObject<String> prefix() {
        return null;
    }

    @Override public ObservableObject<String> suffix() {
        return null;
    }

    @Override public ObservableObject<Boolean> allowFriendlyFire() {
        return null;
    }

    @Override public ObservableObject<Boolean> canSeeFriendlyInvisibles() {
        return null;
    }

    @Override public ObservableObject<NameTagVisibility> nameTagVisibility() {
        return null;
    }

    @Override public ObservableObject<NameTagVisibility> collisionRule() {
        return null;
    }

    @Override public ObservableObject<ChatColor> color() {
        return null;
    }

    public void onUpdate() {
        // update color
    }

}
