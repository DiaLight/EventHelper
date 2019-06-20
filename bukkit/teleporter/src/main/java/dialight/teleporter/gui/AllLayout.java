package dialight.teleporter.gui;

import dialight.guilib.layout.NamedSetLayout;
import dialight.guilib.slot.Slot;
import dialight.offlinelib.OfflineObservable;
import dialight.offlinelib.OnlineObservable;
import dialight.teleporter.SelectedPlayers;
import dialight.teleporter.Teleporter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

public class AllLayout extends NamedSetLayout<OfflinePlayer, UUID> {

    @NotNull private final Teleporter proj;
    @NotNull private final Server server;
    @NotNull private final SelectedPlayers selected;
    private final Consumer<UUID> onSelect = this::onSelect;
    private final Consumer<UUID> onUnselect = this::onUnselect;
    private final Consumer<Player> onAddOnline = this::onAddOnline;
    private final Consumer<Player> onRemoveOnline = this::onRemoveOnline;
    private final Consumer<OfflinePlayer> onAddOffline = this::onAddOffline;
    private final Consumer<OfflinePlayer> onRemoveOffline = this::onRemoveOffline;

    public AllLayout(Teleporter proj, SelectedPlayers selected) {
        super(5, OfflinePlayer::getUniqueId);
        this.proj = proj;
        this.server = proj.getPlugin().getServer();
        this.selected = selected;
        setNameFunction(OfflinePlayer::getName);
        setSlotFunction(this::buildSlot);
    }

    @Override public void onViewersNotEmpty() {
        for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
            this.add(op);
        }

        selected.onAdd(onSelect);
        selected.onRemove(onUnselect);

        OnlineObservable online = proj.getOfflinelib().getOnline();
        online.onAdd(onAddOnline);
        online.onRemove(onRemoveOnline);

        OfflineObservable offline = proj.getOfflinelib().getOffline();
        offline.onAdd(onAddOffline);
        offline.onRemove(onRemoveOffline);
    }

    @Override public void onViewersEmpty() {
        selected.unregisterOnAdd(onSelect);
        selected.unregisterOnRemove(onUnselect);

        OnlineObservable online = proj.getOfflinelib().getOnline();
        online.unregisterOnAdd(onAddOnline);
        online.unregisterOnRemove(onRemoveOnline);

        OfflineObservable offline = proj.getOfflinelib().getOffline();
        offline.unregisterOnAdd(onAddOffline);
        offline.unregisterOnRemove(onRemoveOffline);

        proj.runTask(this::clear);
    }

    private void onSelect(UUID uuid) {
        OfflinePlayer op = server.getOfflinePlayer(uuid);
        if(!update(op)) TeleporterViewState.dumpThrow(op, this);
    }
    private void onUnselect(UUID uuid) {
        OfflinePlayer op = server.getOfflinePlayer(uuid);
        if(!update(op)) TeleporterViewState.dumpThrow(op, this);
    }
    private void onAddOnline(Player player) {
//        OfflinePlayer op = proj.getPlugin().getServer().getOfflinePlayer(player.getUniqueId());
        OfflinePlayer op = player;
        if(!update(op)) TeleporterViewState.dumpThrow(op, this);
    }
    private void onRemoveOnline(Player player) {
//        OfflinePlayer op = proj.getPlugin().getServer().getOfflinePlayer(player.getUniqueId());
        OfflinePlayer op = player;
        if(!update(op)) TeleporterViewState.dumpThrow(op, this);
    }
    private void onAddOffline(OfflinePlayer op) {
        if(!add(op)) TeleporterViewState.dumpThrow(op, this);
    }
    private void onRemoveOffline(OfflinePlayer op) {
        if(!remove(op)) TeleporterViewState.dumpThrow(op, this);
    }

    private Slot buildSlot(OfflinePlayer op) {
        return new SelectionSlot(proj, selected, op.getUniqueId());
    }

}
