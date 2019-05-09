package dialight.toollib;

import dialight.compatibility.PlayerInventoryBc;
import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import dialight.eventhelper.project.ProjectApi;
import dialight.observable.collection.ObservableCollection;
import dialight.observable.map.ObservableMapWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

public class ToolLib extends Project {

    private final ObservableMapWrapper<String, Tool> toolregistry = new ObservableMapWrapper<>();
    private final ObservableCollection<Tool> observable = toolregistry.asCollectionObaervable(Tool::getId);

    @Nullable public Tool getTool(String id) {
        return toolregistry.get(id);
    }

    public ToolLib(JavaPlugin plugin) {
        super(plugin);
    }

    private final ToolListener toolListener = new ToolListener(this);

    @Override public void enable(EventHelper eh) {
        PluginManager pm = getPlugin().getServer().getPluginManager();
        pm.registerEvents(toolListener, getPlugin());

        getPlugin().getCommand("tool").setExecutor(new ToolCommand());
    }

    @Override public void disable() {
        HandlerList.unregisterAll(toolListener);
    }

    @Override
    public ProjectApi getApi() {
        return new ToolLibApi(this);
    }

    public boolean hasAccess(Player player) {
        return player.isOp();
    }

    public ObservableCollection<Tool> getTools() {
        return observable;
    }

    public void giveTool(Player player, String id) {
        ItemStack item = buildItem(id);
        if(item == null) return;
        PlayerInventoryBc.of(player.getInventory()).setItemInMainHand(item);
    }

    @Nullable public ItemStack buildItem(String id) {
        Tool tool = toolregistry.get(id);
        if(tool == null) return null;
        ItemStack item = tool.createItem();
        item = tool.applyId(item);
        return item;
    }
}
