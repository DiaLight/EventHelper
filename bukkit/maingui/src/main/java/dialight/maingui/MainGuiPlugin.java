package dialight.maingui;

import dialight.eventhelper.EventHelper;
import org.bukkit.plugin.java.JavaPlugin;

public final class MainGuiPlugin extends JavaPlugin {

    private MainGuiProject proj = new MainGuiProject(this);
    private EventHelper eh;

    @Override
    public void onLoad() {
        eh = (EventHelper) getServer().getPluginManager().getPlugin("EventHelper");
        if(eh == null) throw new RuntimeException("Eventhelper required");
        eh.register("MainGui", proj.getApi());
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
