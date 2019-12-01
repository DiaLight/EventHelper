package dialight.teams.captain;

import dialight.eventhelper.EventHelper;
import org.bukkit.plugin.java.JavaPlugin;

public class SortByPriorityPlugin extends JavaPlugin {

    private SortByPriority proj = new SortByPriority(this);
    private EventHelper eh;

    @Override
    public void onLoad() {
        eh = (EventHelper) getServer().getPluginManager().getPlugin("EventHelper");
        if(eh == null) throw new RuntimeException("Eventhelper required");
        eh.register("SortByPriority", proj.getApi());
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
