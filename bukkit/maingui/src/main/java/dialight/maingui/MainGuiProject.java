package dialight.maingui;

import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import dialight.eventhelper.project.ProjectApi;
import dialight.guilib.GuiLibApi;
import dialight.guilib.slot.Slot;
import dialight.modulelib.ModuleLibApi;
import dialight.toollib.ToolLibApi;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class MainGuiProject extends Project {

    private GuiLibApi guilib;
    private ToolLibApi toollib;
    private ModuleLibApi modulelib;

    private MainGui gui;
    private MainGuiTool tool;
    private final Map<String, Slot> toolItemRegistry = new HashMap<>();
    private final Map<String, Slot> moduleItemRegistry = new HashMap<>();
    private final MainGuiListener listener = new MainGuiListener(this);

    public MainGuiProject(JavaPlugin plugin) {
        super(plugin);
    }

    @Override public void enable(EventHelper eh) {
        guilib = eh.require("GuiLib");
        toollib = eh.require("ToolLib");
        modulelib = eh.require("ModuleLib");

        gui = new MainGui(this);
        tool = new MainGuiTool(this);

        registerToolSlot(MainGuiTool.ID, new MainGuiSlot(this));
        toollib.getTools().add(tool);

        PluginCommand eventhelper = getPlugin().getCommand("eventhelper");
        eventhelper.setExecutor(new MainGuiCommand(this));

        PluginManager pm = getPlugin().getServer().getPluginManager();
        pm.registerEvents(listener, getPlugin());
    }

    @Override public void disable() {
        HandlerList.unregisterAll(listener);
    }

    @Override public ProjectApi getApi() {
        return new MainGuiApi(this);
    }

    public GuiLibApi getGuilib() {
        return guilib;
    }

    public ToolLibApi getToollib() {
        return toollib;
    }

    public ModuleLibApi getModulelib() {
        return modulelib;
    }

    public MainGui getGui() {
        return gui;
    }

    public void registerToolSlot(String id, Slot slot) {
        toolItemRegistry.put(id, slot);
    }

    public void registerModuleSlot(String id, Slot slot) {
        moduleItemRegistry.put(id, slot);
    }

    @Nullable public Slot getToolSlot(String id) {
        return toolItemRegistry.get(id);
    }

    @Nullable public Slot getModuleSlot(String id) {
        return moduleItemRegistry.get(id);
    }

}
