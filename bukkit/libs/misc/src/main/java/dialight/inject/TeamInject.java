package dialight.inject;

import dialight.nms.ReflectionUtils;
import dialight.observable.ObservableObject;
import dialight.observable.set.ObservableSet;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Constructor;

public abstract class TeamInject {

    private static final Constructor<? extends TeamInject> constructor =
            ReflectionUtils.findCompatibleConstructor(TeamInject.class, Team.class);

    protected final Team team;

    public TeamInject(Team team) {
        this.team = team;
    }

    public abstract boolean inject();
    public abstract boolean uninject();

    public abstract Object getHandle();

    public abstract ObservableSet<String> getMembers();

    public static TeamInject of(Team team) {
        return ReflectionUtils.newInstance(constructor, team);
    }

    public abstract ObservableObject<String> displayName();

    public abstract ObservableObject<String> prefix();

    public abstract ObservableObject<String> suffix();

    public abstract ObservableObject<Boolean> allowFriendlyFire();

    public abstract ObservableObject<Boolean> canSeeFriendlyInvisibles();

    public abstract ObservableObject<NameTagVisibility> nameTagVisibility();

    public abstract ObservableObject<NameTagVisibility> collisionRule();

    public abstract ObservableObject<ChatColor> color();

    public Team asBukkit() {
        return team;
    }

}
