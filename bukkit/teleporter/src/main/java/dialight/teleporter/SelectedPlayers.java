package dialight.teleporter;

import dialight.observable.collection.ObservableCollectionWrapper;
import dialight.offlinelib.UuidPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SelectedPlayers extends ObservableCollectionWrapper<UUID> {
    private final Server server;

    public SelectedPlayers(Server server) {
        super(new HashSet<>());
        this.server = server;
    }

    public List<OfflinePlayer> toOfflinePlayers() {
        return this.stream().map(server::getOfflinePlayer).collect(Collectors.toList());
    }

    public void sendStatus(Player player) {
        if (isEmpty()) {
            player.sendMessage(TeleporterMessages.PlayersBaseIsEmpty);
        } else {
            player.sendMessage(TeleporterMessages.targets(toOfflinePlayers()));
        }
    }

    public void addAllPlayers(Collection<? extends OfflinePlayer> offline) {
        for (OfflinePlayer player : offline) {
            add(player.getUniqueId());
        }
    }

    public void addAllUuidPlayers(Collection<? extends UuidPlayer> ups) {
        for (UuidPlayer up : ups) {
            add(up.getUuid());
        }
    }

    public void removeAllPlayers(Collection<? extends OfflinePlayer> offline) {
        for (OfflinePlayer player : offline) {
            remove(player.getUniqueId());
        }
    }

    public void removeAllUuidPlayers(Collection<? extends UuidPlayer> ups) {
        for (UuidPlayer up : ups) {
            remove(up.getUuid());
        }
    }
}
