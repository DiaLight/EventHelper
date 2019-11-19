package dialight.teams.priority;

import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import dialight.eventhelper.project.ProjectApi;
import dialight.guilib.GuiLibApi;
import dialight.maingui.MainGuiApi;
import dialight.offlinelib.OfflineLibApi;
import dialight.teams.TeamsApi;
import org.bukkit.plugin.java.JavaPlugin;

public class TeamPriorityProject extends Project {

    private MainGuiApi maingui;
    private GuiLibApi guilib;
    private OfflineLibApi offlinelib;
    private TeamsApi teams;
    private boolean running = false;

    public TeamPriorityProject(JavaPlugin plugin) {
        super(plugin);
    }

    @Override public void enable(EventHelper eh) {
        maingui = eh.require("MainGui");
        guilib = eh.require("GuiLib");
        offlinelib = eh.require("OfflineLib");
        teams = eh.require("Teams");

//        teams.addControlItem(new TeamPrioritySlot(this));

    }

    @Override public void disable() {

    }

    @Override public ProjectApi getApi() {
        return new TeamPriorityApi(this);
    }

    public void start() {

    }

    public void stop() {

    }

    public boolean isRunning() {
        return running;
    }
}
