package dialight.inject;

import dialight.observable.map.ObservableMap;
import dialight.observable.map.ObservableMapWrapper;
import net.minecraft.server.v1_14_R1.ScoreboardServer;
import net.minecraft.server.v1_14_R1.ScoreboardTeam;
import org.bukkit.craftbukkit.v1_14_R1.scoreboard.CraftScoreboard;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Field;
import java.util.Map;

public class ScoreboardInject14 extends ScoreboardInject {

    private static Field f_teamsByName;
    private static Field f_teamsByPlayer;
    static {
        try {
            f_teamsByName = net.minecraft.server.v1_14_R1.Scoreboard.class.getDeclaredField("teamsByName");
            f_teamsByName.setAccessible(true);

            f_teamsByPlayer = net.minecraft.server.v1_14_R1.Scoreboard.class.getDeclaredField("teamsByPlayer");
            f_teamsByPlayer.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
    private ObservableMap<String, Team> teamsByName_dupl = new ObservableMapWrapper<>();
    private ObservableMap<String, ScoreboardTeam> teamsByName;
    private Map<String, ScoreboardTeam> teamsByName_orig;

    public ScoreboardInject14(Scoreboard scoreboard) {
        super(scoreboard);
    }

    @Override public Object getHandle() {
        return asCraft().getHandle();
    }

    @Override public boolean inject() {
        net.minecraft.server.v1_14_R1.Scoreboard handle = asCraft().getHandle();
        ScoreboardServer sbs = (ScoreboardServer) handle;
        try {
            f_teamsByName.setAccessible(true);
            teamsByName_orig = (Map<String, ScoreboardTeam>) f_teamsByName.get(handle);
            teamsByName = new ObservableMapWrapper<>(teamsByName_orig);
            f_teamsByName.set(handle, teamsByName);
            teamsByName.onPut(this, this::onAdd);
            teamsByName.onRemove(this, this::onRemove);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void onAdd(String name, ScoreboardTeam team) {
//        teamsByName_dupl.put(name, TeamInject14.toBukkit(team));
        throw new RuntimeException("unimplemented");
    }

    private void onRemove(String name, ScoreboardTeam team) {
        teamsByName_dupl.remove(name);
    }

    @Override public boolean uninject() {
        net.minecraft.server.v1_14_R1.Scoreboard handle = asCraft().getHandle();
        try {
            f_teamsByName.set(handle, teamsByName_orig);
            teamsByName.removeListeners(this);
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ObservableMap<String, Team> getTeamsByName() {
        return teamsByName_dupl;
    }

    @Override
    public ObservableMap<String, Team> getTeamsByPlayer() {
        return null;
    }

    private CraftScoreboard asCraft() {
        return (CraftScoreboard) this.scoreboard;
    }

}
