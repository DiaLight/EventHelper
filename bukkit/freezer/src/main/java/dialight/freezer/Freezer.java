package dialight.freezer;

import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import dialight.eventhelper.project.ProjectApi;
import dialight.toollib.ToolLibApi;
import org.bukkit.plugin.java.JavaPlugin;

public class Freezer extends Project {

    private ToolLibApi toollib;

    public Freezer(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void enable(EventHelper eh) {
        toollib = eh.require("ToolLib");

    }

    @Override public void disable() {

    }

    @Override
    public ProjectApi getApi() {
        return new FreezerApi(this);
    }

}
