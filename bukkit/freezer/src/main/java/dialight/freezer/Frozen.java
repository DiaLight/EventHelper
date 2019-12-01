package dialight.freezer;

import dialight.misc.ActionInvoker;
import dialight.misc.player.UuidPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Frozen {

    private final UuidPlayer target;
    private final Vector velocity;
    private Location location;
    private final ActionInvoker invoker;
    private final String reason;
    private final boolean flyState;

    public Frozen(UuidPlayer target, Location location, ActionInvoker invoker, String reason) {
        this.target = target;
        this.location = new Location(
                location.getWorld(),
                location.getBlockX() + 0.5,
                location.getBlockY() + 0.5,
                location.getBlockZ() + 0.5,
                location.getYaw(),
                location.getPitch()
        );
        this.velocity = target.getVelocity();
        this.invoker = invoker;
        this.reason = reason;
        this.flyState = target.isFlying();
    }

    public UuidPlayer getTarget() {
        return target;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public ActionInvoker getInvoker() {
        return invoker;
    }

    public String getReason() {
        return reason;
    }

    public void sendMessage(String msg) {
        Player player = target.getPlayer();
        if(player != null) {
            player.sendMessage(msg);
        }
    }

    public boolean isSelf() {
        UuidPlayer invoker = this.invoker.getPlayer();
        if(invoker == null) return false;
        return invoker.getUuid().equals(target.getUuid());
    }

    public void resetState() {
        target.setFlying(this.flyState);
    }

}
