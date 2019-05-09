package dialight.eventhelper.project;

import dialight.eventhelper.EventHelper;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public abstract class Project {

    private final JavaPlugin plugin;

    public Project(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract void enable(EventHelper eh);

    public abstract void disable();

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public abstract ProjectApi getApi();

    public BukkitTask runTask(Runnable op) {
        return getPlugin().getServer().getScheduler().runTask(getPlugin(), op);
    }

}
