package dialight.teams.captain;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.misc.ActionInvoker;
import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

public class SortByPrioritySlot implements Slot {

    private final SortByPriority proj;

    public SortByPrioritySlot(SortByPriority proj) {
        this.proj = proj;
    }

    @Override public void onClick(SlotClickEvent e) {
        ActionInvoker invoker = new ActionInvoker(proj.getOfflineLib().getUuidPlayer(e.getPlayer()));
        switch (e.getEvent().getClick()) {
            case LEFT:
                proj.startDistribution(invoker);
                break;
            case SHIFT_LEFT:
                proj.killDistribution(invoker);
                break;
            case RIGHT:
                break;
            case SHIFT_RIGHT:
                break;
        }
    }

    @NotNull @Override public ItemStack createItem() {
        PluginDescriptionFile desc = proj.getPlugin().getDescription();
        ItemStackBuilder isb = new ItemStackBuilder();
        ItemStackBuilderBc.of(isb).enderEye();
        isb.displayName(Colorizer.apply("|a|Распределение по приоритетам"));

        isb.addLore(Colorizer.asList(
                "",
                "|g|Плагин: |y|Распределение по приоритетам",
                "|g|Версия: |y|v" + desc.getVersion()
        ));
        isb.hideMiscellaneous(true);
        return isb.build();
    }

}
