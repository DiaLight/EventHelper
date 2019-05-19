package dialight.teams;

import dialight.extensions.packet.BroadcastPacketListener;
import dialight.extensions.packet.PacketEvent;
import dialight.extensions.packet.protocol.PacketOutTeam;

public class TeamsListener extends BroadcastPacketListener {

    private final Teams proj;

    public TeamsListener(Teams proj) {
        super(proj.getPlugin().getServer(), "Teams");
        this.proj = proj;
    }

    @Override
    public void onListenerAttach() {
        proj.update();
    }

    @Override
    public void onListenerDetach() {

    }

    @Override
    public void onInboundPacket(PacketEvent e) {

    }

    @Override
    public void onOutboundPacket(PacketEvent e) {
        PacketOutTeam packet = PacketOutTeam.of(e.getPacket());
        if(packet == null) return;
        if(packet.isCreate()) {
            proj.onTeamAdd(packet.getName());
        } else if(packet.isRemove()) {
            proj.onTeamRemove(packet.getName());
        } else if(packet.isUpdate()) {
            proj.onTeamUpdate(packet.getName());
        } else if(packet.isAddPlayer()) {
            for (String member : packet.getMembers()) {
                proj.onTeamAddMember(packet.getName(), member);
            }
        } else if(packet.isRemovePlayer()) {
            for (String member : packet.getMembers()) {
                proj.onTeamRemoveMember(packet.getName(), member);
            }
        }
    }

}
