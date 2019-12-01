package dialight.teams.gui.playerblacklist;

import dialight.guilib.elements.NamedSetElement;
import dialight.guilib.slot.Slot;
import dialight.observable.set.ObservableSet;
import dialight.offlinelib.OfflineObservable;
import dialight.offlinelib.OnlineObservable;
import dialight.misc.player.UuidPlayer;
import dialight.teams.Teams;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayersAllBLElement extends NamedSetElement<UuidPlayer, UUID> {

    private final Teams proj;

    public PlayersAllBLElement(Teams proj) {
        super(5, UuidPlayer::getUuid);
        this.proj = proj;

        setNameFunction(UuidPlayer::getName);
        setSlotFunction(this::buildSlot);
    }

    @Override public void onViewersNotEmpty() {
        ObservableSet<UUID> filter = proj.getPlayerBlackList();
        filter.onAdd(this, this::onAdd);
        filter.onRemove(this, this::onRemove);

        OnlineObservable online = proj.getOfflinelib().getOnline();
        online.onAdd(this, this::onAddOnline);
        online.onRemove(this, this::onRemoveOnline);

        OfflineObservable offline = proj.getOfflinelib().getOffline();
        offline.onAdd(this, this::onAddOffline);
        offline.onRemove(this, this::onRemoveOffline);

        proj.getOfflineMode().onChange(this, this::onToggleOfflineMode);

        fillData();
    }

    @Override public void onViewersEmpty() {
        ObservableSet<UUID> filter = proj.getPlayerBlackList();
        filter.removeListeners(this);

        OnlineObservable online = proj.getOfflinelib().getOnline();
        online.removeListeners(this);

        OfflineObservable offline = proj.getOfflinelib().getOffline();
        offline.removeListeners(this);

        proj.getOfflineMode().removeListeners(this);

        proj.runTask(this::clear);
    }

    private void fillData() {
        if(proj.isOfflineMode()) {
            OfflineObservable offline = proj.getOfflinelib().getOffline();
            for (UuidPlayer up : offline) {
                add(up);
            }
        } else {
            OnlineObservable online = proj.getOfflinelib().getOnline();
            for (Player op : online) {
                add(proj.getOfflinelib().getUuidPlayer(op.getUniqueId()));
            }
        }
    }

    private void onToggleOfflineMode(Boolean ondValue, Boolean newValue) {
        proj.runTask(() -> {
            this.clear();
            fillData();
        });
    }

    private void onAdd(UUID uuid) {
        this.update(proj.getOfflinelib().getUuidPlayer(uuid));
    }
    private void onRemove(UUID uuid) {
        this.update(proj.getOfflinelib().getUuidPlayer(uuid));
    }

    private void onAddOnline(Player player) {
        UuidPlayer uuidPlayer = proj.getOfflinelib().getUuidPlayer(player.getUniqueId());
        if(proj.isOfflineMode()) {
            update(uuidPlayer);
        } else {
            add(uuidPlayer);
        }
    }
    private void onRemoveOnline(Player player) {
        UuidPlayer uuidPlayer = proj.getOfflinelib().getUuidPlayer(player.getUniqueId());
        if(proj.isOfflineMode()) {
            update(uuidPlayer);
        } else {
            remove(uuidPlayer);
        }
    }

    private void onAddOffline(UuidPlayer up) {
        if(proj.isOfflineMode()) {
            add(up);
        } else {
            update(up);
        }
    }
    private void onRemoveOffline(UuidPlayer up) {
        if(proj.isOfflineMode()) {
            remove(up);
        } else {
            update(up);
        }
    }

    private Slot buildSlot(UuidPlayer up) {
        return new PlayerBlackListSlot(this.proj, up);
    }

}
