package dialight.teleporter.gui;

import dialight.extensions.UuidEx;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.misc.player.UuidPlayer;
import dialight.teleporter.SelectedPlayers;
import dialight.teleporter.Teleporter;
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

    @Override public void onClick(SlotClickEvent e) {
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
                proj.teleport(e.getPlayer(), up.getLocation());
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
                if(up.isAlien()) {
                    material = Material.INK_SACK;
                } else {
                    material = Material.DIAMOND_ORE;
                }
            }
        } else {
//            if(this.layout == selectedLayout) throw new RuntimeException();
            if(isOnline) {
                material = Material.COAL;
            } else {
                if(up.isAlien()) {
                    material = Material.LOG;
                } else {
                    material = Material.COAL_ORE;
                }
            }
        }
        ItemStackBuilder isb = new ItemStackBuilder(material);
        if (isOnline) {
//            if(!isSelected) {
//                ItemStackBuilderBc.of(isb).playerHead();
//                isb.nbt("{SkullOwner:\"" + up.getName() + "\"}");
//            }
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
        isb.addLore(UuidEx.of(up.getUuid()).toLore());
        isb.addLore(Colorizer.apply("|w|Пират|y|: ") + up.isPirate());
        return isb.build();
    }

}
