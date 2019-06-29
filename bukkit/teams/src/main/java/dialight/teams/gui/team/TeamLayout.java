package dialight.teams.gui.team;

import dialight.guilib.layout.NamedLayout;
import dialight.guilib.layout.ReplaceableLayout;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;
import org.bukkit.OfflinePlayer;

public class TeamLayout extends ReplaceableLayout<NamedLayout<OfflinePlayer>> {

    private final Teams proj;
    private final MembersLayout teamLayout;
    private final NotMembersLayout notCurTeamLayout;

    public TeamLayout(Teams proj, ObservableTeam oteam) {
        this.proj = proj;
        teamLayout = new MembersLayout(proj, oteam);
        notCurTeamLayout = new NotMembersLayout(proj, oteam);

        setCurrent(teamLayout);
    }

    @Override public void onViewersNotEmpty() {
        proj.getListener().registerTeamsObserver(this);
    }
    @Override public void onViewersEmpty() {
        proj.getListener().unregisterTeamsObserver(this);
    }

    public NamedLayout<OfflinePlayer> getTeamLayout() {
        return teamLayout;
    }

    public NamedLayout<OfflinePlayer> getNotCurTeamLayout() {
        return notCurTeamLayout;
    }

}
