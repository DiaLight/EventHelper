package dialight.teams.gui.team;

import dialight.guilib.layout.NamedLayout;
import dialight.guilib.layout.ReplaceableLayout;
import dialight.observable.collection.ObservableCollection;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.function.Function;

public class TeamLayout extends ReplaceableLayout<NamedLayout<OfflinePlayer>> {

    private final Teams proj;
    private final MembersLayout teamLayout;
    private final NotMembersLayout notCurTeamLayout;
    private final Function<Player, Player> getNextPlayer = this::getNextPlayer;

    public TeamLayout(Teams proj, ObservableTeam oteam) {
        this.proj = proj;
        teamLayout = new MembersLayout(proj, oteam);
        notCurTeamLayout = new NotMembersLayout(proj, oteam);

        setCurrent(teamLayout);
    }


    @Override public void onViewersNotEmpty() {
        proj.getListener().registerTeamsObserver(getNextPlayer);
    }

    @Override public void onViewersEmpty() {
        proj.getListener().unregisterTeamsObserver(getNextPlayer);
    }

    private Player getNextPlayer(Player player) {
        ObservableCollection<Player> viewers = getViewers();
        if(player == null) {
            if(viewers.isEmpty()) return null;
            return viewers.iterator().next();
        }
        for (Player viewer : viewers) {
            if(viewer.getUniqueId() == player.getUniqueId()) continue;
            return viewer;
        }
        return null;
    }

    public NamedLayout<OfflinePlayer> getTeamLayout() {
        return teamLayout;
    }

    public NamedLayout<OfflinePlayer> getNotCurTeamLayout() {
        return notCurTeamLayout;
    }

}
