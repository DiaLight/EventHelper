package dialight.freezer.gui;

import dialight.extensions.PlayerEx;
import dialight.extensions.UuidEx;
import dialight.freezer.Freezer;
import dialight.freezer.Frozen;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.misc.ActionInvoker;
import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.misc.player.UuidPlayer;
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
        ActionInvoker invoker = new ActionInvoker(proj.getOfflineLib().getUuidPlayer(e.getPlayer()));
        switch (e.getEvent().getClick()) {
            case LEFT: {
                if(proj.get(up) != null) {
                    proj.unregister(invoker, up);
                } else {
                    proj.register(new Frozen(this.up, this.up.getLocation(), invoker, "gui select"));
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
        isb.addLore(Colorizer.apply("|a|Shift|y|+|a|ПКМ|y|: телепортироваться к игроку"));
        isb.addLore(UuidEx.of(up.getUuid()).toLore());
        isb.addLore(Colorizer.apply("|w|Пират|y|: ") + up.isPirate());
        return isb.build();
    }

}
