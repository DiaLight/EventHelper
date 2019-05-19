package dialight.nms;

import org.bukkit.scoreboard.Scoreboard;

import java.lang.reflect.Constructor;

public abstract class ScoreboardNms {

    private static final Constructor<? extends ScoreboardNms> constructor =
            ReflectionUtils.findCompatibleClass(ScoreboardNms.class, Scoreboard.class);

    protected final Scoreboard scoreboard;

    public ScoreboardNms(Scoreboard scoreboard) {
        this.scoreboard = scoreboard;
    }

    public static ScoreboardNms of(Scoreboard scoreboard) {
        return ReflectionUtils.newInstance(constructor, scoreboard);
    }

    public abstract Object getHandle();
}
