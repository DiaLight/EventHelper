package dialight.inject;

import dialight.nms.ReflectionUtils;
import dialight.observable.collection.ObservableCollection;
import dialight.observable.map.ObservableMap;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.lang.reflect.Constructor;

public abstract class ScoreboardManagerInject {

    private static final Constructor<? extends ScoreboardManagerInject> constructor =
            ReflectionUtils.findCompatibleConstructor(ScoreboardManagerInject.class, ScoreboardManager.class);

    protected final ScoreboardManager scoreboardManager;

    public ScoreboardManagerInject(ScoreboardManager scoreboardManager) {
        this.scoreboardManager = scoreboardManager;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public abstract boolean inject();
    public abstract boolean uninject();

    public abstract ObservableCollection<Scoreboard> getScoreboards();
    public abstract ObservableMap<Player, Scoreboard> getPlayerBoards();

    public static ScoreboardManagerInject of(ScoreboardManager scoreboardManager) {
        return ReflectionUtils.newInstance(constructor, scoreboardManager);
    }

}
