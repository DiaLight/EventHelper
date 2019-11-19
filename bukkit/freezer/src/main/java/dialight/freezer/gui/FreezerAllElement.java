package dialight.freezer.gui;

import dialight.freezer.Freezer;
import dialight.freezer.Frozen;
import dialight.guilib.elements.NamedSetElement;
import dialight.guilib.slot.Slot;
import dialight.offlinelib.OfflineObservable;
import dialight.offlinelib.OnlineObservable;
import dialight.offlinelib.UuidPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class FreezerAllElement extends NamedSetElement<UuidPlayer, UUID> {

    @NotNull private final Freezer proj;

    public FreezerAllElement(Freezer proj) {
        super(5, UuidPlayer::getUuid);
        this.proj = proj;
        setNameFunction(UuidPlayer::getName);
        setSlotFunction(this::buildSlot);
    }

    @Override public void onViewersNotEmpty() {
        for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
            UuidPlayer up = proj.getOfflinelib().getUuidPlayer(op.getUniqueId());
            this.add(up);
        }


        proj.getFrozens().onAdd(this, this::onSelect);
        proj.getFrozens().onRemove(this, this::onUnselect);

        OnlineObservable online = proj.getOfflinelib().getOnline();
        online.onAdd(this, this::onAddOnline);
        online.onRemove(this, this::onRemoveOnline);

        OfflineObservable offline = proj.getOfflinelib().getOffline();
        offline.onAdd(this, this::onAddOffline);
        offline.onRemove(this, this::onRemoveOffline);
    }

    @Override public void onViewersEmpty() {
        proj.getFrozens().removeListeners(this);

        OnlineObservable online = proj.getOfflinelib().getOnline();
        online.removeListeners(this);

        OfflineObservable offline = proj.getOfflinelib().getOffline();
        offline.removeListeners(this);

        proj.runTask(this::clear);
    }

    private void onSelect(Frozen frozen) {
        if(!update(frozen.getTarget())) FreezerViewState.dumpThrow(frozen.getTarget(), this);
    }
    private void onUnselect(Frozen frozen) {
        if(!update(frozen.getTarget())) FreezerViewState.dumpThrow(frozen.getTarget(), this);
    }
    private void onAddOnline(Player player) {
        UuidPlayer up = proj.getOfflinelib().getUuidPlayer(player);
        if(!update(up)) FreezerViewState.dumpThrow(up, this);
    }
    private void onRemoveOnline(Player player) {
        UuidPlayer up = proj.getOfflinelib().getUuidPlayer(player);
        if(!update(up)) FreezerViewState.dumpThrow(up, this);
    }
    private void onAddOffline(UuidPlayer up) {
        if(!add(up)) FreezerViewState.dumpThrow(up, this);
    }
    private void onRemoveOffline(UuidPlayer up) {
        if(!remove(up)) FreezerViewState.dumpThrow(up, this);
    }

    private Slot buildSlot(UuidPlayer up) {
        return new FreezerSelectionSlot(proj, up);
    }

}
