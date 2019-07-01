package dialight.teams.gui.playerblacklist;

import dialight.guilib.layout.NamedSetLayout;
import dialight.guilib.slot.Slot;
import dialight.observable.collection.ObservableCollection;
import dialight.offlinelib.OfflineObservable;
import dialight.offlinelib.OnlineObservable;
import dialight.offlinelib.UuidPlayer;
import dialight.teams.Teams;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PlayersNotInBLLayout extends NamedSetLayout<UuidPlayer, UUID> {

    private final Teams proj;
    private final Consumer<UUID> onAdd = this::onAdd;
    private final Consumer<UUID> onRemove = this::onRemove;
    private final Consumer<Player> onAddOnline = this::onAddOnline;
    private final Consumer<Player> onRemoveOnline = this::onRemoveOnline;
    private final BiConsumer<Boolean, Boolean> onToggleOfflineMode = this::onToggleOfflineMode;

    public PlayersNotInBLLayout(Teams proj) {
        super(5, UuidPlayer::getUuid);
        this.proj = proj;

        setNameFunction(UuidPlayer::getName);
        setSlotFunction(this::buildSlot);
    }

    @Override public void onViewersNotEmpty() {
        ObservableCollection<UUID> filter = proj.getPlayerBlackList();
        filter.onAdd(onAdd);
        filter.onRemove(onRemove);

        OnlineObservable online = proj.getOfflinelib().getOnline();
        online.onAdd(onAddOnline);
        online.onRemove(onRemoveOnline);

        proj.getOfflineMode().onChange(onToggleOfflineMode);

        fillData();
    }

    @Override public void onViewersEmpty() {
        ObservableCollection<UUID> filter = proj.getPlayerBlackList();
        filter.unregisterOnAdd(onAdd);
        filter.unregisterOnRemove(onRemove);

        OnlineObservable online = proj.getOfflinelib().getOnline();
        online.unregisterOnAdd(onAddOnline);
        online.unregisterOnRemove(onRemoveOnline);

        proj.getOfflineMode().unregisterOnChange(onToggleOfflineMode);

        proj.runTask(this::clear);
    }

    private void fillData() {
        ObservableCollection<UUID> filter = proj.getPlayerBlackList();
        if(proj.isOfflineMode()) {
            OfflineObservable offline = proj.getOfflinelib().getOffline();
            for (OfflinePlayer op : offline) {
                if (!filter.contains(op.getUniqueId())) {
                    add(proj.getOfflinelib().getUuidPlayer(op.getUniqueId()));
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

    private Slot buildSlot(UuidPlayer op) {
        return new PlayerBlackListSlot(this.proj, op.getUuid());
    }

}
