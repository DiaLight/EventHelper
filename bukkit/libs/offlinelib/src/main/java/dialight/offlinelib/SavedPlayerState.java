package dialight.offlinelib;

import dialight.misc.player.UuidPlayer;
import dialight.nms.NbtTagListNms;
import org.bukkit.Location;

public class SavedPlayerState {

    private final UuidPlayer player;
    private final NbtTagListNms inventory;
    private Location location;

    public SavedPlayerState(UuidPlayer player) {
        this.player = player;
        this.inventory = this.player.getNbtInventory();
    }

    public void collect() {
        location = player.getLocation().clone();
    }

    public void apply() {
        player.setLocation(location.clone());
        player.setNbtInventory(inventory);
    }

    public UuidPlayer getUuidPlayer() {
        return player;
    }

}
