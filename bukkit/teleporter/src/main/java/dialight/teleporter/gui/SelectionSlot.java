package dialight.teleporter.gui;

import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.offlinelib.OfflineLibApi;
import dialight.teleporter.SelectedPlayers;
import dialight.teleporter.Teleporter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SelectionSlot implements Slot {

    @NotNull private final Teleporter proj;
    @NotNull private final Server server;
    @NotNull private final SelectedPlayers selected;
    @NotNull private final UUID uuid;

    public SelectionSlot(Teleporter proj, SelectedPlayers selected, UUID uuid) {
        this.proj = proj;
        this.server = proj.getPlugin().getServer();
        this.selected = selected;
        this.uuid = uuid;
    }

    @Override
    public void onClick(SlotClickEvent e) {
        switch (e.getEvent().getClick()) {
            case LEFT: {
                if(selected.contains(uuid)) {
                    selected.remove(uuid);
                } else {
                    selected.add(uuid);
                }
            } break;
//                case RIGHT: {
//                    event.player.sendMessage(DefMes.notImplementedYet)
//                } break;
            case SHIFT_RIGHT: {
                OfflineLibApi offlinelib = proj.getOfflinelib();
                Location to = offlinelib.getOfflinePlayerEx(uuid).getLocation();
                proj.teleport(e.getPlayer(), to);
            } break;
        }
    }

    @Override
    public ItemStack createItem() {
        boolean isSelected = selected.contains(uuid);
        OfflinePlayer op = server.getOfflinePlayer(this.uuid);
        boolean isOnline = op.isOnline();
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
            isb.displayName(op.getName());
        } else {
            isb.displayName(op.getName() + Colorizer.apply(" |r|(Офлайн)"));
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
