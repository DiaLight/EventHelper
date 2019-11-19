package dialight.teams.gui.team;

import dialight.guilib.elements.NamedElement;
import dialight.guilib.elements.ReplaceableElement;
import dialight.offlinelib.UuidPlayer;
import dialight.teams.observable.ObservableTeam;
import dialight.teams.Teams;

public class TeamElement extends ReplaceableElement<NamedElement<UuidPlayer>> {

    private final Teams proj;
    private final MembersElement teamLayout;
    private final NotMembersElement notMembersLayout;

    public TeamElement(Teams proj, ObservableTeam oteam) {
        this.proj = proj;
        teamLayout = new MembersElement(proj, oteam);
        notMembersLayout = new NotMembersElement(proj, oteam);

        setCurrent(teamLayout);
    }

    @Override public void onViewersNotEmpty() {

    }
    @Override public void onViewersEmpty() {

    }

    public void setTeamLayout() {
        setCurrent(teamLayout);
    }

    public void setNotMembersLayout() {
        setCurrent(notMembersLayout);
    }

}
