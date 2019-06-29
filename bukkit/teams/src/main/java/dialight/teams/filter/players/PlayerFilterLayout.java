package dialight.teams.filter.players;

import dialight.guilib.layout.NamedLayout;
import dialight.guilib.layout.ReplaceableLayout;
import dialight.offlinelib.UuidPlayer;
import dialight.teams.Teams;

public class PlayerFilterLayout extends ReplaceableLayout<NamedLayout<UuidPlayer>> {

    private final Teams proj;
    private final PlayersAllFilterLayout playersAllFilterLayout;
    private final PlayersInFilterLayout playersInFilterLayout;
    private final PlayersNotInFilterLayout playersNotInFilterLayout;

    public PlayerFilterLayout(Teams proj) {
        this.proj = proj;
        this.playersAllFilterLayout = new PlayersAllFilterLayout(proj);
        this.playersInFilterLayout = new PlayersInFilterLayout(proj);
        this.playersNotInFilterLayout = new PlayersNotInFilterLayout(proj);

        setCurrent(playersAllFilterLayout);
    }

    @Override public void onViewersNotEmpty() {
        proj.getListener().registerTeamsObserver(this);
    }

    @Override public void onViewersEmpty() {
        proj.getListener().unregisterTeamsObserver(this);
    }

    public void setAllLayout() {
        setCurrent(playersAllFilterLayout);
    }

    public void setInFilterLayout() {
        setCurrent(playersInFilterLayout);
    }

    public void setNotInFilterLayout() {
        setCurrent(playersNotInFilterLayout);
    }

}
