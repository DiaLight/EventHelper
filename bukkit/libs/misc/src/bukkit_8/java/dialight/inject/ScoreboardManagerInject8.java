package dialight.inject;

import dialight.observable.collection.ObservableCollection;
import dialight.observable.collection.ObservableCollectionWrapper;
import dialight.observable.map.ObservableMap;
import dialight.observable.map.ObservableMapWrapper;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboardManager;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

public class ScoreboardManagerInject8 extends ScoreboardManagerInject {

    private static Field f_craftScoreboardManager_scoreboards;
    private static Field f_craftScoreboardManager_playerBoards;

    static {
        try {
            f_craftScoreboardManager_scoreboards = CraftScoreboardManager.class.getDeclaredField("scoreboards");
            f_craftScoreboardManager_scoreboards.setAccessible(true);

            f_craftScoreboardManager_playerBoards = CraftScoreboardManager.class.getDeclaredField("playerBoards");
            f_craftScoreboardManager_playerBoards.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private Collection<CraftScoreboard> scoreboards_orig;
    private ObservableCollection<CraftScoreboard> scoreboards;
    private Map<CraftPlayer, CraftScoreboard> playerBoards_orig;
    private ObservableMap<CraftPlayer, CraftScoreboard> playerBoards;

    public ScoreboardManagerInject8(ScoreboardManager scoreboardManager) {
        super(scoreboardManager);
    }

    @Override public boolean inject() {
        try {
            scoreboards_orig = (Collection<CraftScoreboard>) f_craftScoreboardManager_scoreboards.get(scoreboardManager);
            scoreboards = new ObservableCollectionWrapper<>(scoreboards_orig);
            f_craftScoreboardManager_scoreboards.set(scoreboardManager, scoreboards);
            playerBoards_orig = (Map<CraftPlayer, CraftScoreboard>) f_craftScoreboardManager_playerBoards.get(scoreboardManager);
            playerBoards = new ObservableMapWrapper<>(playerBoards_orig);
            f_craftScoreboardManager_playerBoards.set(scoreboardManager, playerBoards);
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override public boolean uninject() {
        try {
            f_craftScoreboardManager_scoreboards.set(scoreboardManager, scoreboards_orig);

            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override public ObservableCollection<Scoreboard> getScoreboards() {
        return (ObservableCollection<Scoreboard>) (ObservableCollection) scoreboards;
    }

    @SuppressWarnings("unchecked")
    @Override public ObservableMap<Player, Scoreboard> getPlayerBoards() {
        return (ObservableMap<Player, Scoreboard>) (ObservableMap) playerBoards;
    }

    public CraftScoreboardManager asCraft() {
        return (CraftScoreboardManager) scoreboardManager;
    }

}
