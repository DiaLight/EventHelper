package dialight.extensions;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerEx {

    private final Player player;

    private PlayerEx(Player player) {
        this.player = player;
    }

    public void teleport(Location loc) {
        loc = LocationEx.of(loc).findSafeLoc();
        player.teleport(LocationEx.of(player.getLocation()).keepRotation(loc));
    }

    public static PlayerEx of(Player player) {
        return new PlayerEx(player);
    }

}
