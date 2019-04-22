package dialight.toollib;

import dialight.exceptions.Todo;
import dialight.observable.map.ObservableMap;
import dialight.observable.map.ObservableMapWrapper;
import lombok.val;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class ToolCore {

    private final JavaPlugin plugin;

    private final ObservableMapWrapper<String, Tool> toolregistry = new ObservableMapWrapper<>();

    public void registerTool(Tool tool) {
        toolregistry.put(tool.getId(), tool);
    }

    @Nullable public Tool getTool(String id) {
        return toolregistry.get(id);
    }

    public ToolCore(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private final ToolListener toolListener = new ToolListener(this);

    void enable() {
        val pm = plugin.getServer().getPluginManager();
        pm.registerEvents(toolListener, plugin);
    }

    void disable() {
        HandlerList.unregisterAll(toolListener);
    }

    public boolean hasAccess(Player player) {
        return player.isOp();
    }

}
