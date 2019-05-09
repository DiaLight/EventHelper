package dialight.eventhelper;

import dialight.eventhelper.project.ProjectApi;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class EventHelper extends JavaPlugin {

    private final Map<String, ProjectApi> projects = new HashMap<>();

    protected void loadBuiltin() {}
    protected void enableBuiltin() {}
    protected void disableBuiltin() {}

    @Override
    public void onLoad() {

        loadBuiltin();

    }

    @Override
    public void onEnable() {

        enableBuiltin();

    }

    @Override
    public void onDisable() {

        disableBuiltin();

    }

    public void register(String name, ProjectApi project) {
        this.projects.put(name, project);
    }

    @SuppressWarnings("unchecked")
    public <P extends ProjectApi> P require(String name) {
        ProjectApi project = this.projects.get(name);
        if(project == null) throw new RuntimeException("Project with name " + name + " is not registered");
        return (P) project;
    }

}
