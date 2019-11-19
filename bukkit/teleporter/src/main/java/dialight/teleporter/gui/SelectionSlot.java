package dialight.teleporter.gui;

import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.offlinelib.UuidPlayer;
import dialight.teleporter.SelectedPlayers;
import dialight.teleporter.Teleporter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SelectionSlot implements Slot {

    @NotNull private final Teleporter proj;
    @NotNull private final SelectedPlayers selected;
    @NotNull private final UuidPlayer up;

    public SelectionSlot(Teleporter proj, SelectedPlayers selected, UuidPlayer up) {
        this.proj = proj;
        this.selected = selected;
        this.up = up;
    }

    @Override
    public void onClick(SlotClickEvent e) {
        switch (e.getEvent().getClick()) {
            case LEFT: {
                if(selected.contains(up.getUuid())) {
                    selected.remove(up.getUuid());
                } else {
                    selected.add(up.getUuid());
                }
            } break;
//                case RIGHT: {
//                    event.player.sendMessage(DefMes.notImplementedYet)
//                } break;
            case SHIFT_RIGHT: {
                Location to = up.getLocation();
                if(to != null) {
                    proj.teleport(e.getPlayer(), to);
                } else {
                    e.getPlayer().sendMessage(Colorizer.apply("|r|Игрок не найден"));
                }
            } break;
        }
    }

    @Override public ItemStack createItem() {
        boolean isSelected = selected.contains(up.getUuid());
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
//            "~|g|ПКМ|y|: Телепортировать игрока к другому игроку";
        isb.addLore(Colorizer.apply("|a|Shift|y|+|a|ПКМ|y|: телепортироваться к игроку"));
        return isb.build();
    }

}
