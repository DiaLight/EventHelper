package dialight.teleporter;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class TeleporterPlugin extends JavaPlugin {

    private final TeleporterCore core = new TeleporterCore(this);

    @Override
    public void onEnable() {
        core.enable();

        PluginDescriptionFile desc = getDescription();
        getLogger().info( desc.getName() + " version " + desc.getVersion() + " is enabled!" );
    }

    @Override
    public void onDisable() {
        core.disable();
    }

}
