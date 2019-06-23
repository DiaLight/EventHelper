package dialight.autorespawn;

import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import dialight.eventhelper.project.ProjectApi;
import dialight.guilib.GuiLibApi;
import dialight.maingui.MainGuiApi;
import dialight.modulelib.ModuleLibApi;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoRespawn extends Project {

    private MainGuiApi maingui;
    private ModuleLibApi modulelib;
    private GuiLibApi guilib;

    private final AutoRespawnListener listener = new AutoRespawnListener(this);
    private AutoRespawnModule module;

    public AutoRespawn(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void enable(EventHelper eh) {
        maingui = eh.require("MainGui");
        modulelib = eh.require("ModuleLib");
        guilib = eh.require("GuiLib");

        module = new AutoRespawnModule(this);

        maingui.registerModuleItem(AutoRespawnModule.ID, new AutoRespawnSlot(this));
        modulelib.register(module);
        PluginManager pm = getPlugin().getServer().getPluginManager();
        pm.registerEvents(listener, getPlugin());
    }

    @Override
    public void disable() {
        HandlerList.unregisterAll(listener);
    }

    @Override
    public ProjectApi getApi() {
        return new AutoRespawnApi(this);
    }

}
