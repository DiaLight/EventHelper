package dialight.extensions.packet;

import dialight.nms.PlayerNms;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ChannelPacketHandler extends ChannelDuplexHandler implements PacketHandler {

    private final Player player;
    private final String id;
    private final List<Consumer<PacketEvent>> inboundPacketListeners = new ArrayList<>();
    private final List<Consumer<PacketEvent>> outboundPacketListeners = new ArrayList<>();

    public ChannelPacketHandler(final Player player, String id) {
        this.player = player;
        this.id = id;
    }

    public void fireInboundPacket(PacketEvent e) {
        for (Consumer<PacketEvent> op : inboundPacketListeners) {
            try {
                op.accept(e);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    public void fireOutboundPacket(PacketEvent e) {
        for (Consumer<PacketEvent> op : outboundPacketListeners) {
            try {
                op.accept(e);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
        PacketEvent e = new PacketEvent(player, packet);
        fireOutboundPacket(e);
        if(e.isCancelled()) return;
        super.write(ctx, packet, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object packet) throws Exception {
        PacketEvent e = new PacketEvent(player, packet);
        fireInboundPacket(e);
        if(e.isCancelled()) return;
        super.channelRead(ctx, packet);
    }

    @Override public void onInboundPacket(Consumer<PacketEvent> op) {
        inboundPacketListeners.add(op);
    }

    @Override public void onOutboundPacket(Consumer<PacketEvent> op) {
        outboundPacketListeners.add(op);
    }

    @Override public Player getPlayer() {
        return player;
    }

    @Override public String getId() {
        return id;
    }

    @Override public void unregister() {
        PlayerNms.of(player).removePacketHandler(id);
        inboundPacketListeners.clear();
        outboundPacketListeners.clear();
    }

}
