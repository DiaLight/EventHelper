package dialight.teams.captain;

import dialight.eventhelper.EventHelper;
import org.bukkit.plugin.java.JavaPlugin;

public class SortByCaptainPlugin extends JavaPlugin {

    private SortByCaptain proj = new SortByCaptain(this);
    private EventHelper eh;

    @Override
    public void onLoad() {
        eh = (EventHelper) getServer().getPluginManager().getPlugin("EventHelper");
        if(eh == null) throw new RuntimeException("Eventhelper required");
        eh.register("SortByCaptain", proj.getApi());
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
