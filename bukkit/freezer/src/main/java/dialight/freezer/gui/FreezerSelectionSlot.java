package dialight.freezer.gui;

import dialight.extensions.ActionInvoker;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.extensions.PlayerEx;
import dialight.freezer.Freezer;
import dialight.freezer.Frozen;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.offlinelib.UuidPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FreezerSelectionSlot implements Slot {

    @NotNull private final Freezer proj;
    @NotNull private final UuidPlayer up;

    public FreezerSelectionSlot(Freezer proj, UuidPlayer up) {
        this.proj = proj;
        this.up = up;
    }

    @Override
    public void onClick(SlotClickEvent e) {
        switch (e.getEvent().getClick()) {
            case LEFT: {
                if(proj.get(up) != null) {
                    proj.unregister(up);
                } else {
                    proj.register(new Frozen(up, up.getLocation(), new ActionInvoker(e.getPlayer()), "gui select"));
                }
            } break;
            case SHIFT_RIGHT: {
                Location to = up.getLocation();
                if(to != null) {
                    PlayerEx.of(e.getPlayer()).teleport(to);
//                    proj.teleport(e.getPlayer(), to);
                } else {
                    e.getPlayer().sendMessage(Colorizer.apply("|r|Игрок не найден"));
                }
            } break;
        }
    }

    @NotNull @Override public ItemStack createItem() {
        boolean isSelected = proj.get(up) != null;
        boolean isOnline = up.isOnline();
        Material material;
        if (isSelected) {
//            if(this.layout == unselectedLayout) throw new RuntimeException();
            if(isOnline) {
                material = Material.DIAMOND;
            } else {
                material = Material.DIAMOND_ORE;
            }
        } else {
//            if(this.layout == selectedLayout) throw new RuntimeException();
            if(isOnline) {
                material = Material.COAL;
            } else {
                material = Material.COAL_ORE;
            }
        }
        ItemStackBuilder isb = new ItemStackBuilder(material);
        if (isOnline) {
            isb.displayName(up.getName());
        } else {
            isb.displayName(up.getName() + Colorizer.apply(" |r|(Офлайн)"));
        }
        if (isSelected) {
            isb.addLore(Colorizer.apply("|a|ЛКМ|y|: отменить выбор"));
        } else {
            isb.addLore(Colorizer.apply("|a|ЛКМ|y|: выбрать игрока"));
        }
        isb.addLore(Colorizer.apply("|a|Shift|y|+|a|ПКМ|y|: телепортироваться к игроку"));
        return isb.build();
    }

}
