package dialight.teleporter.gui;

import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.layout.NamedLayout;
import dialight.guilib.layout.NamedSetLayout;
import dialight.guilib.layout.ReplaceableLayout;
import dialight.guilib.layout.SlotLayout;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.offlinelib.OfflineLibApi;
import dialight.offlinelib.OfflineObservable;
import dialight.offlinelib.OnlineObservable;
import dialight.teleporter.SelectedPlayers;
import dialight.teleporter.Teleporter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SelectedViewState extends ReplaceableLayout<NamedLayout<OfflinePlayer>> {

    private class SelectionSlot implements Slot {

        private final UUID uuid;
        private SlotLayout layout;

        public SelectionSlot(UUID uuid) {
            this.uuid = uuid;
        }

        @Override
        public void attached(SlotLayout layout) {
            this.layout = layout;
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
                if(this.layout == unselectedLayout) throw new RuntimeException();
                if(isOnline) {
                    material = Material.DIAMOND;
                } else {
                    material = Material.DIAMOND_ORE;
                }
            } else {
                if(this.layout == selectedLayout) throw new RuntimeException();
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

    private final NamedLayout<OfflinePlayer> selectedLayout = new NamedSetLayout<>(5, OfflinePlayer::getUniqueId);
    private final NamedLayout<OfflinePlayer> unselectedLayout = new NamedSetLayout<>(5, OfflinePlayer::getUniqueId);
    private final NamedLayout<OfflinePlayer> allLayout = new NamedSetLayout<>(5, OfflinePlayer::getUniqueId);
    @NotNull private final Teleporter proj;
    @NotNull private final Server server;
    @NotNull private final SelectedPlayers selected;

    public SelectedViewState(Teleporter proj, SelectedPlayers selected) {
        this.proj = proj;
        this.server = proj.getPlugin().getServer();
        this.selected = selected;

        selectedLayout.setNameFunction(OfflinePlayer::getName);
        selectedLayout.setSlotFunction(this::buildSlot);
        unselectedLayout.setNameFunction(OfflinePlayer::getName);
        unselectedLayout.setSlotFunction(this::buildSlot);
        allLayout.setNameFunction(OfflinePlayer::getName);
        allLayout.setSlotFunction(this::buildSlot);

        selected.toOfflinePlayers().forEach(selectedLayout::add);
        for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
            allLayout.add(op);
            if(selected.contains(op.getUniqueId())) continue;
            unselectedLayout.add(op);
        }

        selected.onAdd(this::onSelect);
        selected.onRemove(this::onUnselect);
        OfflineObservable offline = proj.getOfflinelib().getOffline();
        offline.onAdd(this::onAddOffline);
        offline.onRemove(this::onRemoveOffline);
        OnlineObservable online = proj.getOfflinelib().getOnline();
        online.onAdd(this::onAddOnline);
        online.onRemove(this::onRemoveOnline);

        setCurrent(allLayout);
    }

    private void dumpThrow(OfflinePlayer op, NamedLayout<OfflinePlayer> layout) {
        layout.dump();
        throw new RuntimeException("Somethings wrong");
    }

    private void onSelect(UUID uuid) {
        OfflinePlayer op = server.getOfflinePlayer(uuid);
        if(!selectedLayout.add(op)) dumpThrow(op, selectedLayout);
        if(!unselectedLayout.remove(op)) dumpThrow(op, unselectedLayout);
        if(!allLayout.update(op)) dumpThrow(op, allLayout);
    }
    private void onUnselect(UUID uuid) {
        OfflinePlayer op = server.getOfflinePlayer(uuid);
        if(!selectedLayout.remove(op))  dumpThrow(op, selectedLayout);
        if(!unselectedLayout.add(op)) dumpThrow(op, unselectedLayout);
        if(!allLayout.update(op)) dumpThrow(op, allLayout);
    }
    private void onAddOnline(Player player) {
//        OfflinePlayer op = proj.getPlugin().getServer().getOfflinePlayer(player.getUniqueId());
        OfflinePlayer op = player;
        if(selected.contains(op.getUniqueId())) {
            if(!selectedLayout.update(op)) dumpThrow(op, selectedLayout);
            if(unselectedLayout.update(op)) dumpThrow(op, unselectedLayout);
        } else {
            if(selectedLayout.update(op)) dumpThrow(op, selectedLayout);
            if(!unselectedLayout.update(op)) dumpThrow(op, unselectedLayout);
        }
        if(!allLayout.update(op)) dumpThrow(op, allLayout);
    }
    private void onRemoveOnline(Player player) {
//        OfflinePlayer op = proj.getPlugin().getServer().getOfflinePlayer(player.getUniqueId());
        OfflinePlayer op = player;
        if(selected.contains(op.getUniqueId())) {
            if(!selectedLayout.update(op)) dumpThrow(op, selectedLayout);
            if(unselectedLayout.update(op)) dumpThrow(op, unselectedLayout);
        } else {
            if(selectedLayout.update(op)) dumpThrow(op, selectedLayout);
            if(!unselectedLayout.update(op)) dumpThrow(op, unselectedLayout);
        }
        if(!allLayout.update(op)) dumpThrow(op, allLayout);
    }
    private void onAddOffline(OfflinePlayer op) {
        if(!unselectedLayout.add(op)) dumpThrow(op, unselectedLayout);
        if(!allLayout.add(op)) dumpThrow(op, allLayout);
    }
    private void onRemoveOffline(OfflinePlayer op) {
        if(selected.remove(op.getUniqueId())) {

        }
        if(selectedLayout.remove(op)) dumpThrow(op, selectedLayout);
        if(!unselectedLayout.remove(op)) dumpThrow(op, unselectedLayout);
        if(!allLayout.remove(op)) dumpThrow(op, allLayout);
    }

    private Slot buildSlot(OfflinePlayer op) {
        return new SelectionSlot(op.getUniqueId());
    }

    public NamedLayout<OfflinePlayer> getAllLayout() {
        return allLayout;
    }

    public NamedLayout<OfflinePlayer> getUnselectedLayout() {
        return unselectedLayout;
    }

    public NamedLayout<OfflinePlayer> getSelectedLayout() {
        return selectedLayout;
    }

    @NotNull public SelectedPlayers getSelected() {
        return selected;
    }

}
