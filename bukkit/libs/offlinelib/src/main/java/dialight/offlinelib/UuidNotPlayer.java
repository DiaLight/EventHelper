package dialight.offlinelib;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class UuidNotPlayer extends UuidPlayer {

    @NotNull private final String name;

    public UuidNotPlayer(OfflineLib proj, String name) {
        super(proj, UUID.nameUUIDFromBytes(name.getBytes()));
        this.name = name;
    }

    @Override @NotNull public String getName() {
        return name;
    }

    @Override public boolean isOnline() {
        return false;
    }

    @Nullable @Override public Player getPlayer() {
        return null;
    }

    @Nullable @Override public OfflinePlayer getOfflinePlayer() {
        return null;
    }

}
