package dialight.toollib;

import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import dialight.eventhelper.project.ProjectApi;
import dialight.observable.collection.ObservableCollection;
import dialight.observable.map.ObservableMapWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ToolLib extends Project {

    private final ObservableMapWrapper<String, Tool> toolregistry = new ObservableMapWrapper<>();
    private final ObservableCollection<Tool> immutableObservable = toolregistry.asImmutableCollectionObaervable(Tool::getId);
    private final Map<Class<?>, Tool> classToolMap = new HashMap<>();

    @Nullable public Tool getTool(String id) {
        return toolregistry.get(id);
    }

    @Nullable public <T extends Tool> T getTool(Class<T> clazz) {
        return (T) classToolMap.get(clazz);
    }

    public void register(Tool tool) {
        String id = tool.getId();
        if(toolregistry.putIfAbsent(id, tool) != null) {
            throw new IllegalArgumentException("Tool with id " + id + " already registered");
        }
        Class<? extends Tool> clazz = tool.getClass();
        if(classToolMap.putIfAbsent(clazz, tool) != null) {
            throw new IllegalArgumentException("Tool with class " + clazz.getSimpleName() + " already registered");
        }
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
//        return player.isOp();
        return true;
    }

    public ObservableCollection<Tool> getImmutableObservable() {
        return immutableObservable;
    }

}
