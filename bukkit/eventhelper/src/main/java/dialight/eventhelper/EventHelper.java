package dialight.eventhelper;

import dialight.eventhelper.project.ProjectApi;
import dialight.patch.ScoreboardTeamPatch;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class EventHelper extends JavaPlugin {

    private static boolean patched = false;

    static {
        patched = ScoreboardTeamPatch.of().patch();
    }

    private final Map<String, ProjectApi> projects = new HashMap<>();

    protected void loadBuiltin() {}
    protected void enableBuiltin() {}
    protected void disableBuiltin() {}

    @Override
    public void onLoad() {
        loadBuiltin();
        if(!patched) {
            getServer().getConsoleSender().sendMessage("EventHelper ERROR: unable to patch server classes. Please, restart server");
            getServer().shutdown();
        }
    }

    @Override
    public void onEnable() {
        try {
            enableBuiltin();
        } catch (Throwable e) {
            e.printStackTrace();
            getServer().getConsoleSender().sendMessage("EventHelper ERROR: unknown startup error. Please, restart server");
            getServer().shutdown();
        }
    }

    @Override
    public void onDisable() {
        disableBuiltin();
    }

    public void register(String name, ProjectApi project) {
        this.projects.put(name, project);
    }

    @SuppressWarnings("unchecked")
    @NotNull public <P extends ProjectApi> P require(String name) {
        ProjectApi project = this.projects.get(name);
        if(project == null) throw new RuntimeException("Project with name " + name + " is not registered");
        return (P) project;
    }

    @SuppressWarnings("unchecked")
    @Nullable public <P extends ProjectApi> P optional(String name) {
        return (P) this.projects.get(name);
    }

}
