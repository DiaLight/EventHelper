package dialight.inject;

import dialight.nms.ReflectionUtils;
import dialight.observable.map.ObservableMap;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Constructor;

public abstract class ScoreboardInject {

    private static final Constructor<? extends ScoreboardInject> constructor =
            ReflectionUtils.findCompatibleConstructor(ScoreboardInject.class, Scoreboard.class);

    protected final Scoreboard scoreboard;

    public ScoreboardInject(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public abstract boolean inject();
    public abstract boolean uninject();

    public abstract Object getHandle();

    public abstract ObservableMap<String, Team> getTeamsByName();
    public abstract ObservableMap<String, Team> getTeamsByPlayer();

    public static ScoreboardInject of(Scoreboard scoreboard) {
        return ReflectionUtils.newInstance(constructor, scoreboard);
    }

}
