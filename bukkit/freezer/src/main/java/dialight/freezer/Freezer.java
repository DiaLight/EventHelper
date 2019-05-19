package dialight.freezer;

import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import dialight.eventhelper.project.ProjectApi;
import dialight.guilib.GuiLibApi;
import dialight.maingui.MainGuiApi;
import dialight.offlinelib.OfflineLibApi;
import dialight.toollib.ToolLibApi;
import org.bukkit.plugin.java.JavaPlugin;

public class Freezer extends Project {

    private MainGuiApi maingui;
    private ToolLibApi toollib;
    private OfflineLibApi offlinelib;
    private GuiLibApi guilib;

    private FreezerTool tool;

    public Freezer(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void enable(EventHelper eh) {
        maingui = eh.require("MainGui");
        toollib = eh.require("ToolLib");
        offlinelib = eh.require("OfflineLib");
        guilib = eh.require("GuiLib");

        tool = new FreezerTool(this);

        maingui.registerToolItem(FreezerTool.ID, new FreezerSlot(this));
        toollib.register(tool);
    }

    @Override public void disable() {

    }

    @Override
    public ProjectApi getApi() {
        return new FreezerApi(this);
    }

    public MainGuiApi getMaingui() {
        return maingui;
    }

    public ToolLibApi getToollib() {
        return toollib;
    }

    public OfflineLibApi getOfflinelib() {
        return offlinelib;
    }

    public GuiLibApi getGuilib() {
        return guilib;
    }

}
