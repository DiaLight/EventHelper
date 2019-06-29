package dialight.guilib.slot;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

public abstract class DynamicSlot implements Slot {

    private final List<SlotUsage> usages = new ArrayList<>();
    private int updateTask = -1;

    @Override public void attached(SlotUsage usage) {
        usages.add(usage);
    }

    @Override public void detached(SlotUsage usage) {
        usages.remove(usage);
    }

    public void update() {
        for (SlotUsage usage : usages) {
            usage.update();
        }
    }

    public void updateLater(Plugin plugin) {
        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        if(scheduler.isQueued(updateTask)) return;
        updateTask = scheduler.runTask(plugin, this::update).getTaskId();
    }

}
