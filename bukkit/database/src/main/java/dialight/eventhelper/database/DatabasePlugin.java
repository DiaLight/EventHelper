package dialight.eventhelper.database;

import dialight.eventhelper.EventHelper;
import org.bukkit.plugin.java.JavaPlugin;

public class DatabasePlugin extends JavaPlugin {

    private Database proj = new Database(this);
    private EventHelper eh;

    @Override
    public void onLoad() {
        eh = (EventHelper) getServer().getPluginManager().getPlugin("EventHelper");
        if(eh == null) throw new RuntimeException("Eventhelper required");
        eh.register(getName(), proj.getApi());
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
