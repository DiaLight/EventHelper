package dialight.teleporter;

import org.bukkit.plugin.java.JavaPlugin;

public class TeleporterCore {

    private final JavaPlugin plugin;

    private final TeleporterTool tool = new TeleporterTool();

    public TeleporterCore(JavaPlugin plugin) {
        this.plugin = plugin;
    }


    public void enable() {

    }

    public void disable() {

    }
}
