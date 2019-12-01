package dialight.teams.random;

import dialight.eventhelper.EventHelper;
import org.bukkit.plugin.java.JavaPlugin;

public class SortByRandomPlugin extends JavaPlugin {

    private SortByRandom proj = new SortByRandom(this);
    private EventHelper eh;

    @Override
    public void onLoad() {
        eh = (EventHelper) getServer().getPluginManager().getPlugin("EventHelper");
        if(eh == null) throw new RuntimeException("Eventhelper required");
        eh.register("SortByRandom", proj.getApi());
    }

    @Override
    public void onEnable() {
        this.proj.enable(eh);
    }

    @Override
    public void onDisable() {
        this.proj.disable();
    }

}
