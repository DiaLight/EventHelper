package dialight.teams.priority;

import dialight.eventhelper.EventHelper;
import org.bukkit.plugin.java.JavaPlugin;

public class TeamPriorityPlugin extends JavaPlugin {

    private TeamPriorityProject proj = new TeamPriorityProject(this);
    private EventHelper eh;

    @Override
    public void onLoad() {
        eh = (EventHelper) getServer().getPluginManager().getPlugin("EventHelper");
        if(eh == null) throw new RuntimeException("Eventhelper required");
        eh.register("TeamPriority", proj.getApi());
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
