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

public class PlayersNotInBLElement extends NamedSetElement<UuidPlayer, UUID> {

    private final Teams proj;

    public PlayersNotInBLElement(Teams proj) {
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

        proj.getOfflineMode().onChange(this, this::onToggleOfflineMode);

        fillData();
    }

    @Override public void onViewersEmpty() {
        ObservableSet<UUID> filter = proj.getPlayerBlackList();
        filter.removeListeners(this);

        OnlineObservable online = proj.getOfflinelib().getOnline();
        online.removeListeners(this);

        proj.getOfflineMode().removeListeners(this);

        proj.runTask(this::clear);
    }

    private void fillData() {
        ObservableSet<UUID> filter = proj.getPlayerBlackList();
        if(proj.isOfflineMode()) {
            OfflineObservable offline = proj.getOfflinelib().getOffline();
            for (UuidPlayer up : offline) {
                if (!filter.contains(up.getUuid())) {
                    add(up);
                }
            }
        } else {
            OnlineObservable online = proj.getOfflinelib().getOnline();
            for (Player op : online) {
                if (!filter.contains(op.getUniqueId())) {
                    add(proj.getOfflinelib().getUuidPlayer(op.getUniqueId()));
                }
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
        this.remove(proj.getOfflinelib().getUuidPlayer(uuid));
    }
    private void onRemove(UUID uuid) {
        this.add(proj.getOfflinelib().getUuidPlayer(uuid));
    }

    private void onAddOnline(Player player) {
        update(proj.getOfflinelib().getUuidPlayer(player.getUniqueId()));
    }
    private void onRemoveOnline(Player player) {
        update(proj.getOfflinelib().getUuidPlayer(player.getUniqueId()));
    }

    private Slot buildSlot(UuidPlayer up) {
        return new PlayerBlackListSlot(this.proj, up);
    }

}
