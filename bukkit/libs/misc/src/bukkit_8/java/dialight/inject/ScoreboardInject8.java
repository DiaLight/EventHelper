package dialight.inject;

import dialight.observable.map.ObservableMap;
import dialight.observable.map.ObservableMapWrapper;
import net.minecraft.server.v1_8_R3.ScoreboardServer;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.BiConsumer;

public class ScoreboardInject8 extends ScoreboardInject {

    private static Constructor<CraftScoreboard> m_CraftScoreboard_init;
    private static Field f_CraftScoreboard_teamsByName;
    private static Field f_CraftScoreboard_teamsByPlayer;

    static {
        try {
            m_CraftScoreboard_init = CraftScoreboard.class.getDeclaredConstructor(net.minecraft.server.v1_8_R3.Scoreboard.class);
            m_CraftScoreboard_init.setAccessible(true);

            f_CraftScoreboard_teamsByName = net.minecraft.server.v1_8_R3.Scoreboard.class.getDeclaredField("teamsByName");
            f_CraftScoreboard_teamsByName.setAccessible(true);

            f_CraftScoreboard_teamsByPlayer = net.minecraft.server.v1_8_R3.Scoreboard.class.getDeclaredField("teamsByPlayer");
            f_CraftScoreboard_teamsByPlayer.setAccessible(true);
        } catch (NoSuchMethodException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private final BiConsumer<String, ScoreboardTeam> removeTeam = this::removeTeam;

    private final BiConsumer<String, ScoreboardTeam> removeMember = this::removeMember;

    private final ObservableMap<String, Team> teamsByName_dupl = new ObservableMapWrapper<>();
    private ObservableMap<String, ScoreboardTeam> teamsByName;
    private Map<String, ScoreboardTeam> teamsByName_orig;

    private final ObservableMap<String, Team> teamsByPlayer_dupl = new ObservableMapWrapper<>();
    private ObservableMap<String, ScoreboardTeam> teamsByPlayer;
    private Map<String, ScoreboardTeam> teamsByPlayer_orig;

    public ScoreboardInject8(Scoreboard scoreboard) {
        super(scoreboard);
    }

    @Override public boolean inject() {
        net.minecraft.server.v1_8_R3.Scoreboard handle = asCraft().getHandle();
        ScoreboardServer sbs = (ScoreboardServer) handle;
        try {
            teamsByName_orig = (Map<String, ScoreboardTeam>) f_CraftScoreboard_teamsByName.get(handle);
            teamsByName = new ObservableMapWrapper<>(teamsByName_orig);
            f_CraftScoreboard_teamsByName.set(handle, teamsByName);
            teamsByName.onPut(this, this::addTeam);
            teamsByName.onRemove(this, this::removeTeam);
            teamsByName.forEach(this::addTeam);

            teamsByPlayer_orig = (Map<String, ScoreboardTeam>) f_CraftScoreboard_teamsByPlayer.get(handle);
            teamsByPlayer = new ObservableMapWrapper<>(teamsByPlayer_orig);
            f_CraftScoreboard_teamsByPlayer.set(handle, teamsByPlayer);
            teamsByPlayer.onPut(this, this::addMember);
            teamsByPlayer.onRemove(this, this::removeMember);
            teamsByPlayer.forEach(this::addMember);
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override public boolean uninject() {
        net.minecraft.server.v1_8_R3.Scoreboard handle = asCraft().getHandle();
        try {
            f_CraftScoreboard_teamsByName.set(handle, teamsByName_orig);
            teamsByName.removeListeners(this);
            teamsByName_dupl.clear();
            teamsByPlayer.removeListeners(this);
            teamsByPlayer_dupl.clear();
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void addTeam(String name, ScoreboardTeam team) {
        teamsByName_dupl.put(name, TeamInject8.toBukkit(asCraft(), team));
    }
    private void removeTeam(String name, ScoreboardTeam team) {
        teamsByName_dupl.remove(name);
    }

    private void addMember(String name, ScoreboardTeam team) {
        teamsByPlayer_dupl.put(name, TeamInject8.toBukkit(asCraft(), team));
    }
    private void removeMember(String name, ScoreboardTeam team) {
        teamsByPlayer_dupl.remove(name);
    }

    @Override public Object getHandle() {
        return asCraft().getHandle();
    }

    @Override public ObservableMap<String, Team> getTeamsByName() {
        return teamsByName_dupl;
    }

    @Override public ObservableMap<String, Team> getTeamsByPlayer() {
        return teamsByPlayer_dupl;
    }

    private CraftScoreboard asCraft() {
        return (CraftScoreboard) this.scoreboard;
    }

}
