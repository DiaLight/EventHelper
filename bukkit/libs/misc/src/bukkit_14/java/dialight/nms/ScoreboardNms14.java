package dialight.nms;

import net.minecraft.server.v1_14_R1.ScoreboardServer;
import net.minecraft.server.v1_14_R1.ScoreboardTeam;
import org.bukkit.craftbukkit.v1_14_R1.scoreboard.CraftScoreboard;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Map;

public class ScoreboardNms14 extends ScoreboardNms {

    public ScoreboardNms14(Scoreboard scoreboard) {
        super(scoreboard);
    }

    private void replaceTeams(Map<String, ScoreboardTeam> value) {
//        Object handle = asCraft().getHandle();
//        Field f_teamsByName = handle.getClass().getDeclaredField("teamsByName");
//        f_teamsByName.setAccessible(true);
        ScoreboardServer sbs = (ScoreboardServer) getHandle();
    }

    @Override public Object getHandle() {
        return asCraft().getHandle();
    }

    private CraftScoreboard asCraft() {
        return (CraftScoreboard) this.scoreboard;
    }

}
