package dialight.guilib;

import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import dialight.eventhelper.project.ProjectApi;
import dialight.guilib.gui.Gui;
import dialight.guilib.view.View;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GuiLib extends Project {

    private final GuiStory story = new GuiStory();
    private final UsageRegistry usageRegistry = new UsageRegistry();
    private final GuiListener listener = new GuiListener(this);

    public GuiLib(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void enable(EventHelper eh) {
        PluginManager pm = getPlugin().getServer().getPluginManager();
        pm.registerEvents(listener, getPlugin());
    }

    @Override
    public void disable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.closeInventory();
        }
        HandlerList.unregisterAll(listener);
    }

    @Override
    public ProjectApi getApi() {
        return new GuiLibApi(this);
    }

    public UsageRegistry getUsageRegistry() {
        return usageRegistry;
    }

    @NotNull private View getOrCreateView(Gui gui, Player player) {
        View view = usageRegistry.getView(gui, player);
        if(view != null) return view;
        view = gui.createView(player);
        usageRegistry.putView(gui, view, player);
        return view;
    }

    public void openGui(Player player, Gui gui) {
        story.addGui(player, gui);
        View view = getOrCreateView(gui, player);
        player.openInventory(view.getInventory());
    }

    public Gui openLast(Player player) {
        Gui gui = story.getLast(player);
        if(gui != null) {
            View view = getOrCreateView(gui, player);
            player.openInventory(view.getInventory());
        }
        return gui;
    }

    public void openPrev(Player player) {
        Gui gui = story.getPrev(player);
        if(gui != null) {
            View view = getOrCreateView(gui, player);
            player.openInventory(view.getInventory());
        } else {
            player.closeInventory();
            story.clearStory(player);
        }
    }

    public void clearStory(Player player) {
        story.clearStory(player);
    }

    public List<Player> getViewers(Gui gui) {
        return usageRegistry.getViewers(gui);
    }

}
