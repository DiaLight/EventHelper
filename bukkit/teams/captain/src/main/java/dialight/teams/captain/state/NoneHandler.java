package dialight.teams.captain.state;

import dialight.extensions.ServerEx;
import dialight.stateengine.StateEngineHandler;
import dialight.teams.captain.SortByCaptain;
import dialight.teams.captain.SortByCaptainState;
import org.bukkit.World;

public class NoneHandler extends StateEngineHandler<SortByCaptainState> {

    private World world;
    private long savedTime;
    private String savedDoDaylightCycle;

    public NoneHandler(SortByCaptain proj) {
        super(proj, SortByCaptainState.NONE);
    }

    @Override public void enter() {
        if(world == null) return;
        world.setTime(savedTime);
        world.setGameRuleValue("doDaylightCycle", savedDoDaylightCycle);
        world = null;
        savedDoDaylightCycle = null;
    }

    @Override public void tick(int tick) {

    }

    @Override public void leave() {
        world = ServerEx.of(proj.getPlugin().getServer()).getMainWorld();
        savedTime = world.getTime();
        savedDoDaylightCycle = world.getGameRuleValue("doDaylightCycle");
        world.setGameRuleValue("doDaylightCycle", "false");
    }

    @Override public void clear() {

    }

    public World getWorld() {
        return world;
    }

}
