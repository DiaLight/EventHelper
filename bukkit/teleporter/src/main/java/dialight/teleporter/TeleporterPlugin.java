package dialight.teleporter;

import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import org.bukkit.plugin.java.JavaPlugin;

public final class TeleporterPlugin extends JavaPlugin {

    private Teleporter proj = new Teleporter(this);
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