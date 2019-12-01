package dialight.misc.packet;

import org.bukkit.entity.Player;

public class PacketEvent {

    private final Player player;
    private final Object packet;
    private boolean cancelled = false;

    public PacketEvent(Player player, Object packet) {
        this.player = player;
        this.packet = packet;
    }

    public Player getPlayer() {
        return player;
    }

    public Object getPacket() {
        return packet;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
