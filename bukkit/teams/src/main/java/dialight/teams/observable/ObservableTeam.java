package dialight.teams.observable;

import dialight.extensions.ColorConverter;
import dialight.observable.ObservableObject;
import dialight.observable.set.ObservableSet;
import dialight.offlinelib.UuidPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;

public abstract class ObservableTeam {

    protected final Team team;
    private final String name;

    public ObservableTeam(Team team) {
        this.team = team;
        this.name = team.getName();
    }

    public String getName() {
        return this.name;
    }

    public Color getLeatherColor() {
        return ColorConverter.toLeatherColor(color().getValue());
    }
    public Team getTeam() {
        return team;
    }

    public abstract ObservableSet<UuidPlayer> getMembers();

    public abstract ObservableObject<String> displayName();
    public abstract ObservableObject<String> prefix();
    public abstract ObservableObject<String> suffix();
    public abstract ObservableObject<Boolean> allowFriendlyFire();
    public abstract ObservableObject<Boolean> canSeeFriendlyInvisibles();
    public abstract ObservableObject<NameTagVisibility> nameTagVisibility();
    public abstract ObservableObject<NameTagVisibility> collisionRule();
    public abstract ObservableObject<ChatColor> color();

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObservableTeam that = (ObservableTeam) o;

        return name.equals(that.name);
    }

    @Override public int hashCode() {
        return name.hashCode();
    }
}
