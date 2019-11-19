package dialight.teams.observable.listener;

import dialight.extensions.packet.BroadcastPacketListener;
import dialight.extensions.packet.PacketEvent;
import dialight.extensions.packet.protocol.PacketOutTeam;
import dialight.guilib.Viewable;
import dialight.teams.Teams;
import dialight.teams.ViewableTeamObserver;
import dialight.teams.event.TeamEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Function;

public class TeamsListener extends BroadcastPacketListener {

    private final Teams proj;
    private final TeamHandler handler;
    private final Collection<Function<Player, Player>> teamObservers = new LinkedList<>();

    public TeamsListener(Teams proj, TeamHandler handler) {
        super(proj.getPlugin(), "Teams");
        this.proj = proj;
        this.handler = handler;
    }

    @Override public void onOutboundPacket(PacketEvent e) {
        TeamEvent event = toTeamEvent(PacketOutTeam.of(e.getPacket()));
        if(event == null) return;
        proj.runTask(() -> handler.onEvent(event));
    }

    private TeamEvent toTeamEvent(@Nullable PacketOutTeam packet) {
        if(packet == null) return null;
        if(packet.isCreate()) {
            return new TeamEvent.Add(packet.getName());
        } else if(packet.isRemove()) {
            return new TeamEvent.Remove(packet.getName());
        } else if(packet.isUpdate()) {
            return new TeamEvent.Update(packet.getName());
        } else if(packet.isAddPlayer()) {
            return new TeamEvent.AddMembers(packet.getName(), packet.getMembers());
        } else if(packet.isRemovePlayer()) {
            return new TeamEvent.RemoveMembers(packet.getName(), packet.getMembers());
        }
        return null;
    }

    @Nullable @Override public Player nextOnline(@Nullable Player exclude) {
        for (Function<Player, Player> op : teamObservers) {
            Player result = op.apply(exclude);
            if(result == null) continue;
            return result;
        }
        System.out.println("next not found!!!");
        return null;
    }

    public void registerTeamsObserver(Function<Player, Player> op) {
//        System.out.println("register " + op.getClass());
        boolean isEmpty = teamObservers.isEmpty();
        teamObservers.add(op);
        if(isEmpty) {
            handler.update();
            start();
//            System.out.println("start");
        }
    }

    public void unregisterTeamsObserver(Function<Player, Player> op) {
//        System.out.println("unregister " + op.getClass());
        if(!teamObservers.remove(op)) {
            System.out.println("Failed to unregister teams observer");
        }
        if(teamObservers.isEmpty()) {
            stop();
//            System.out.println("stop");
        }
    }

    public void registerTeamsObserver(Viewable viewable) {
        registerTeamsObserver(new ViewableTeamObserver(viewable));
    }

    public void unregisterTeamsObserver(Viewable viewable) {
        unregisterTeamsObserver(new ViewableTeamObserver(viewable));
    }

}
