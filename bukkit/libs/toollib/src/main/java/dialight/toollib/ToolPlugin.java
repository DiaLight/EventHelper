package dialight.toollib;

import lombok.val;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ToolPlugin extends JavaPlugin {

    private final ToolCore core = new ToolCore(this);

    @Override
    public void onEnable() {
        core.enable();
        getCommand("tool").setExecutor(new ToolCommand());

        val desc = getDescription();
        getLogger().info( desc.getName() + " version " + desc.getVersion() + " is enabled!" );
    }

    @Override
    public void onDisable() {
        core.disable();
    }

}
