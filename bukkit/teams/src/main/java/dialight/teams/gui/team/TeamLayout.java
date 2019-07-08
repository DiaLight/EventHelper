package dialight.teams.gui.team;

import dialight.guilib.layout.NamedLayout;
import dialight.guilib.layout.ReplaceableLayout;
import dialight.offlinelib.UuidPlayer;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;

public class TeamLayout extends ReplaceableLayout<NamedLayout<UuidPlayer>> {

    private final Teams proj;
    private final MembersLayout teamLayout;
    private final NotMembersLayout notMembersLayout;

    public TeamLayout(Teams proj, ObservableTeam oteam) {
        this.proj = proj;
        teamLayout = new MembersLayout(proj, oteam);
        notMembersLayout = new NotMembersLayout(proj, oteam);

        setCurrent(teamLayout);
    }

    @Override public void onViewersNotEmpty() {
        proj.getListener().registerTeamsObserver(this);
    }
    @Override public void onViewersEmpty() {
        proj.getListener().unregisterTeamsObserver(this);
    }

    public void setTeamLayout() {
        setCurrent(teamLayout);
    }

    public void setNotMembersLayout() {
        setCurrent(notMembersLayout);
    }

}
