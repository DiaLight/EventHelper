package dialight.teams.observable.inject;

import dialight.inject.TeamInject;
import dialight.observable.ObservableObject;
import dialight.observable.set.ObservableSet;
import dialight.observable.set.ObservableSetWrapper;
import dialight.observable.set.WriteProxyObservableSet;
import dialight.offlinelib.OfflineLibApi;
import dialight.offlinelib.UuidPlayer;
import dialight.teams.observable.ObservableTeam;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

public class ObservableInjectTeam extends ObservableTeam {

    private final OfflineLibApi offlinelib;
    private final TeamInject inject;
    private final ObservableSet<UuidPlayer> members = new ObservableSetWrapper<>();
    private final WriteProxyObservableSet<UuidPlayer> membersApi = new WriteProxyObservableSet<>(members);

    public ObservableInjectTeam(OfflineLibApi offlinelib, Team team) {
        super(team);
        this.offlinelib = offlinelib;
        this.inject = TeamInject.of(team);
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
        String name = element.getName();
        if(name == null) return false;
        team.removeEntry(name);
        return true;
    }

    private void onAddMember(String member) {
        members.add(offlinelib.getOrCreateNotPlayer(member));
    }

    private void onRemoveMember(String member) {
        members.remove(offlinelib.getOrCreateNotPlayer(member));
    }

    public void inject() {
        if(!inject.inject()) throw new RuntimeException("can't inject");
        inject.getMembers().onAdd(this, this::onAddMember);
        inject.getMembers().onRemove(this, this::onRemoveMember);
        inject.getMembers().forEach(this::onAddMember);
    }

    public void uninject() {
        inject.getMembers().removeListeners(this);
        this.inject.uninject();
        this.members.clear();
    }

    @Override public ObservableSet<UuidPlayer> getMembers() {
        return membersApi;
    }

    @Override public ObservableObject<String> displayName() {
        return inject.displayName();
    }

    @Override public ObservableObject<String> prefix() {
        return inject.prefix();
    }

    @Override public ObservableObject<String> suffix() {
        return inject.suffix();
    }

    @Override public ObservableObject<Boolean> allowFriendlyFire() {
        return inject.allowFriendlyFire();
    }

    @Override public ObservableObject<Boolean> canSeeFriendlyInvisibles() {
        return inject.canSeeFriendlyInvisibles();
    }

    @Override public ObservableObject<NameTagVisibility> nameTagVisibility() {
        return inject.nameTagVisibility();
    }

    @Override public ObservableObject<NameTagVisibility> collisionRule() {
        return inject.collisionRule();
    }
    @Override public ObservableObject<ChatColor> color() {
        return inject.color();
    }


}
