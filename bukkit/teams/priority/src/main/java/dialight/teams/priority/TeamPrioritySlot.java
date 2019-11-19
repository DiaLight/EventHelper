package dialight.teams.priority;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

public class TeamPrioritySlot implements Slot {

    private final TeamPriorityProject proj;

    public TeamPrioritySlot(TeamPriorityProject proj) {
        this.proj = proj;
    }

    @Override public void onClick(SlotClickEvent e) {
        switch (e.getEvent().getClick()) {
            case LEFT:
                if (proj.isRunning()) {

                }
                proj.start();
                break;
            case SHIFT_LEFT:
                proj.stop();
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
