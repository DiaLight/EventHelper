package dialight.teams.captain.gui.captain.member;

import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.misc.player.UuidPlayer;
import dialight.teams.captain.SortByCaptain;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CaptainSlot implements Slot {

    private final SortByCaptain proj;
    private final UuidPlayer up;

    public CaptainSlot(SortByCaptain proj, UuidPlayer up) {
        this.proj = proj;
        this.up = up;
    }

    @Override public void onClick(SlotClickEvent e) {
        switch (e.getEvent().getClick()) {
            case LEFT:
                proj.getCaptainSelection().selectCaptain(e.getPlayer(), up);
                break;
            case SHIFT_LEFT:
                break;
            case RIGHT:
                break;
            case SHIFT_RIGHT:
                break;
        }
    }

    @NotNull @Override public ItemStack createItem() {
        boolean isOnline = up.isOnline();
        Material material;
        if(isOnline) {
            material = Material.COAL;
        } else {
            material = Material.COAL_ORE;
        }
        ItemStackBuilder isb = new ItemStackBuilder(material);
        if (isOnline) {
            isb.displayName(up.getName());
        } else {
            isb.displayName(up.getName() + Colorizer.apply(" |r|(Офлайн)"));
        }
        isb.addLore(Colorizer.apply("|a|ЛКМ|y|: выбрать игрока себе в команду"));
        return isb.build();
    }

}
