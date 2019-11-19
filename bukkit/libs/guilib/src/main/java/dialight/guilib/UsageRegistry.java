package dialight.guilib;

import dialight.guilib.gui.Gui;
import dialight.guilib.view.View;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class UsageRegistry {

    private static final class GuiUsage {

        public final Map<UUID, Player> viewers = new HashMap<>();
        public final Map<UUID, View> viewMap = new HashMap<>();

    }

    private final Map<Gui, GuiUsage> guiMap = new HashMap<>();

    @NotNull private GuiUsage getOrCreateGuiUsage(Gui gui) {
        GuiUsage usage = guiMap.get(gui);
        if(usage != null) return usage;
        usage = new GuiUsage();
        guiMap.put(gui, usage);
        return usage;
    }

    public void onOpenGui(Player player, Gui gui) {
        GuiUsage usage = getOrCreateGuiUsage(gui);
        usage.viewers.put(player.getUniqueId(), player);
    }
    public void onCloseGui(Player player, Gui gui) {
        GuiUsage usage = guiMap.get(gui);
        if(usage == null) throw new NullPointerException();
        usage.viewers.remove(player.getUniqueId());
    }

    public List<Player> getViewers(Gui gui) {
        GuiUsage usage = guiMap.get(gui);
        if(usage == null) return Collections.emptyList();
        return new ArrayList<>(usage.viewers.values());
    }

    @Nullable public View getView(Gui gui, Player player) {
        GuiUsage usage = guiMap.get(gui);
        if(usage == null) return null;
        return usage.viewMap.get(player.getUniqueId());
    }

    @NotNull public void putView(Gui gui, View view, Player player) {
        GuiUsage usage = getOrCreateGuiUsage(gui);
        usage.viewMap.put(player.getUniqueId(), view);
    }

    public View resetView(Gui gui, Player player) {
        GuiUsage usage = guiMap.get(gui);
        if(usage == null) return null;
        return usage.viewMap.remove(player.getUniqueId());
    }

}
