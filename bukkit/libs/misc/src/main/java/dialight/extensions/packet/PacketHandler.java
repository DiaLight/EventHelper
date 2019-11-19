package dialight.extensions.packet;

import org.bukkit.entity.Player;

import java.util.function.Consumer;

public interface PacketHandler {

    void onInboundPacket(Consumer<PacketEvent> op);

    void onOutboundPacket(Consumer<PacketEvent> op);

    Player getPlayer();

    String getId();

    void unregister();

}
