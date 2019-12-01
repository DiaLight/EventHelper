package dialight.teleporter.gui;

import dialight.guilib.elements.NamedSetElement;
import dialight.guilib.slot.Slot;
import dialight.offlinelib.OfflineObservable;
import dialight.offlinelib.OnlineObservable;
import dialight.misc.player.UuidPlayer;
import dialight.teleporter.SelectedPlayers;
import dialight.teleporter.Teleporter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UnselectedElement extends NamedSetElement<UuidPlayer, UUID> {

    @NotNull private final Teleporter proj;
    @NotNull private final SelectedPlayers selected;

    public UnselectedElement(Teleporter proj, SelectedPlayers selected) {
        super(5, UuidPlayer::getUuid);
        this.proj = proj;
        this.selected = selected;
        setNameFunction(UuidPlayer::getName);
        setSlotFunction(this::buildSlot);
    }

    @Override public void onViewersNotEmpty() {
        for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
            if(selected.contains(op.getUniqueId())) continue;
            UuidPlayer up = proj.getOfflinelib().getUuidPlayer(op.getUniqueId());
            this.add(up);
        }

        selected.onAdd(this, this::onSelect);
        selected.onRemove(this, this::onUnselect);

        OnlineObservable online = proj.getOfflinelib().getOnline();
        online.onAdd(this, this::onAddOnline);
        online.onRemove(this, this::onRemoveOnline);

        OfflineObservable offline = proj.getOfflinelib().getOffline();
        offline.onAdd(this, this::onAddOffline);
        offline.onRemove(this, this::onRemoveOffline);
    }

    @Override public void onViewersEmpty() {
        selected.removeListeners(this);

        OnlineObservable online = proj.getOfflinelib().getOnline();
        online.removeListeners(this);

        OfflineObservable offline = proj.getOfflinelib().getOffline();
        offline.removeListeners(this);

        proj.runTask(this::clear);
    }

    private void onSelect(UUID uuid) {
        UuidPlayer up = proj.getOfflinelib().getUuidPlayer(uuid);
        if(!remove(up)) TeleporterViewState.dumpThrow(up, this);
    }
    private void onUnselect(UUID uuid) {
        UuidPlayer up = proj.getOfflinelib().getUuidPlayer(uuid);
        if(!add(up)) TeleporterViewState.dumpThrow(up, this);
    }
    private void onAddOnline(Player player) {
        UuidPlayer up = proj.getOfflinelib().getUuidPlayer(player);
        if(selected.contains(up.getUuid())) {
            if(update(up)) TeleporterViewState.dumpThrow(up, this);
        } else {
            if(!update(up)) TeleporterViewState.dumpThrow(up, this);
        }
    }
    private void onRemoveOnline(Player player) {
        UuidPlayer up = proj.getOfflinelib().getUuidPlayer(player);
        if(selected.contains(up.getUuid())) {
            if(update(up)) TeleporterViewState.dumpThrow(up, this);
        } else {
            if(!update(up)) TeleporterViewState.dumpThrow(up, this);
        }
    }
    private void onAddOffline(UuidPlayer up) {
        if(!add(up)) TeleporterViewState.dumpThrow(up, this);
    }
    private void onRemoveOffline(UuidPlayer up) {
        if(selected.contains(up.getUuid())) {
            if(remove(up)) TeleporterViewState.dumpThrow(up, this);
        } else {
            if(!remove(up)) TeleporterViewState.dumpThrow(up, this);
        }
    }

    private Slot buildSlot(UuidPlayer up) {
        return new SelectionSlot(proj, selected, up);
    }

}