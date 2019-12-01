package dialight.autorespawn;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.misc.ItemStackBuilder;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import org.bukkit.DyeColor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AutoRespawnSlot implements Slot {

    private final AutoRespawn proj;
    private final ItemStack item;

    public AutoRespawnSlot(AutoRespawn proj) {
        this.proj = proj;
        this.item = new ItemStackBuilder()
                .let(builder -> {
                    ItemStackBuilderBc.of(builder).bed(DyeColor.WHITE);
                })
                .displayName("autoorespawn")
                .build();
    }

    @Override
    public void onClick(SlotClickEvent e) {

    }

    @NotNull @Override public ItemStack createItem() {
        return item;
    }

}
