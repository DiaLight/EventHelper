package dialight.teams.gui.playerblacklist;

import dialight.guilib.elements.NamedSetElement;
import dialight.guilib.slot.Slot;
import dialight.observable.set.ObservableSet;
import dialight.offlinelib.OnlineObservable;
import dialight.misc.player.UuidPlayer;
import dialight.teams.Teams;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayersInBLElement extends NamedSetElement<UuidPlayer, UUID> {

    private final Teams proj;

    public PlayersInBLElement(Teams proj) {
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

        filter.forEach(this::onAdd);
    }

    @Override public void onViewersEmpty() {
        ObservableSet<UUID> filter = proj.getPlayerBlackList();
        filter.removeListeners(this);

        OnlineObservable online = proj.getOfflinelib().getOnline();
        online.removeListeners(this);

        proj.runTask(this::clear);
    }

    private void onAdd(UUID uuid) {
        this.add(proj.getOfflinelib().getUuidPlayer(uuid));
    }

    private void onRemove(UUID uuid) {
        this.remove(proj.getOfflinelib().getUuidPlayer(uuid));
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
