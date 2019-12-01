package dialight.eventhelper;

import dialight.eventhelper.project.Project;
import dialight.freezer.Freezer;
import dialight.guilib.GuiLib;
import dialight.maingui.MainGuiProject;
import dialight.modulelib.ModuleLib;
import dialight.offlinelib.OfflineLib;
import dialight.teams.Teams;
import dialight.teams.captain.SortByCaptain;
import dialight.teams.random.SortByRandom;
import dialight.teleporter.Teleporter;
import dialight.toollib.ToolLib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EventHelperBuiltin extends EventHelper {

    private final Map<String, Project> builtinProjects = new HashMap<>();
    private final List<String> inLoadOrder = new ArrayList<>();

    private void registerProj(String name, Project proj) {
        builtinProjects.put(name, proj);
        register(name, proj.getApi());
        inLoadOrder.add(name);
    }

    @Override protected void loadBuiltin() {
        registerProj("GuiLib", new GuiLib(this));
        registerProj("ToolLib", new ToolLib(this));
        registerProj("ModuleLib", new ModuleLib(this));
        registerProj("OfflineLib", new OfflineLib(this));
        registerProj("MainGui", new MainGuiProject(this));
        registerProj("Teleporter", new Teleporter(this));
        registerProj("Freezer", new Freezer(this));
//        registerProj("AutoRespawn", new AutoRespawn(this));
        registerProj("Teams", new Teams(this));
        registerProj("SortByRandom", new SortByRandom(this));
        registerProj("SortByCaptain", new SortByCaptain(this));
//        registerProj("SortByPriority", new SortByPriority(this));
    }

    @Override protected void enableBuiltin() {
        for (String name : inLoadOrder) {
            builtinProjects.get(name).enable(this);
        }
    }

    @Override protected void disableBuiltin() {
        for(Project proj : builtinProjects.values()) {
            proj.disable();
        }
    }

}
