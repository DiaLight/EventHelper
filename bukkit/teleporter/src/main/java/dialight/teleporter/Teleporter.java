package dialight.teleporter;

import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import dialight.eventhelper.project.ProjectApi;
import dialight.extensions.OfflinePlayerEx;
import dialight.guilib.GuiLibApi;
import dialight.maingui.MainGuiApi;
import dialight.offlinelib.OfflineLibApi;
import dialight.teleporter.gui.TeleporterGui;
import dialight.toollib.ToolLibApi;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Teleporter extends Project {

    private MainGuiApi maingui;
    private ToolLibApi toollib;
    private OfflineLibApi offlinelib;
    private GuiLibApi guilib;

    private TeleporterTool tool;
    private TeleporterGui gui;
    private Map<UUID, SelectedPlayers> selectedMap;

    public Teleporter(JavaPlugin plugin) {
        super(plugin);
    }

    @Override public void enable(EventHelper eh) {
        maingui = eh.require("MainGui");
        toollib = eh.require("ToolLib");
        offlinelib = eh.require("OfflineLib");
        guilib = eh.require("GuiLib");

        selectedMap = new HashMap<>();
        tool = new TeleporterTool(this);
        gui = new TeleporterGui(this);

        maingui.registerToolItem(TeleporterTool.ID, new TeleporterSlot(this));
        toollib.getTools().add(tool);
    }

    @Override public void disable() {
        toollib = null;
        selectedMap = null;
    }

    @Override public ProjectApi getApi() {
        return new TeleporterApi(this);
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

    public TeleporterTool getTool() {
        return tool;
    }

    public TeleporterGui getGui() {
        return gui;
    }

    @NotNull public SelectedPlayers getSelectedPlayers(UUID uuid) {
        SelectedPlayers selected = selectedMap.get(uuid);
        if(selected == null) {
            selected = new SelectedPlayers(getPlugin().getServer());
            selectedMap.put(uuid, selected);
        }
        return selected;
    }

    public void teleport(OfflinePlayer op, Location loc) {
        OfflinePlayerEx opex = offlinelib.getOfflinePlayerEx(op.getUniqueId());
        loc = loc.clone();
        while (loc.getBlock().getType().isSolid()) {
            loc.add(0, 1, 0);
        }
        Location oldLoc = opex.getLocation();
        opex.setLocation(new Location(
                loc.getWorld(),
                loc.getX(),
                loc.getY(),
                loc.getZ(),
                oldLoc.getYaw(),
                oldLoc.getPitch()
        ));
    }

}
