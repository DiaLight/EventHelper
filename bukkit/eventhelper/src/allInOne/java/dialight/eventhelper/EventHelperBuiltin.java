package dialight.eventhelper;

import dialight.eventhelper.project.Project;
import dialight.guilib.GuiLib;
import dialight.maingui.MainGuiProject;
import dialight.modulelib.ModuleLib;
import dialight.offlinelib.OfflineLib;
import dialight.teams.Teams;
import dialight.teams.random.TeamRandomizerProject;
import dialight.teleporter.Teleporter;
import dialight.toollib.ToolLib;

import java.util.HashMap;
import java.util.Map;

public final class EventHelperBuiltin extends EventHelper {

    private final Map<String, Project> builtinProjects = new HashMap<>();

    @Override protected void loadBuiltin() {
        builtinProjects.put("GuiLib", new GuiLib(this));
        builtinProjects.put("ToolLib", new ToolLib(this));
        builtinProjects.put("ModuleLib", new ModuleLib(this));
        builtinProjects.put("OfflineLib", new OfflineLib(this));
        builtinProjects.put("MainGui", new MainGuiProject(this));
        builtinProjects.put("Teleporter", new Teleporter(this));
//        builtinProjects.put("Freezer", new Freezer(this));
//        builtinProjects.put("AutoRespawn", new AutoRespawn(this));
        builtinProjects.put("Teams", new Teams(this));
        builtinProjects.put("TeamRandomizer", new TeamRandomizerProject(this));
        for(Map.Entry<String, Project> pair : builtinProjects.entrySet()) {
            register(pair.getKey(), pair.getValue().getApi());
        }
    }

    @Override protected void enableBuiltin() {
        for(Project proj : builtinProjects.values()) {
            proj.enable(this);
        }
    }

    @Override protected void disableBuiltin() {
        for(Project proj : builtinProjects.values()) {
            proj.disable();
        }
    }

}
