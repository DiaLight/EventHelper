package dialight.teams.gui.playerblacklist;

import dialight.guilib.layout.NamedLayout;
import dialight.guilib.layout.ReplaceableLayout;
import dialight.offlinelib.UuidPlayer;
import dialight.teams.Teams;

public class PlayerBlackListLayout extends ReplaceableLayout<NamedLayout<UuidPlayer>> {

    private final Teams proj;
    private final PlayersAllBLLayout playersAllBLLayout;
    private final PlayersInBLLayout playersInBLLayout;
    private final PlayersNotInBLLayout playersNotInBLLayout;

    public PlayerBlackListLayout(Teams proj) {
        this.proj = proj;
        this.playersAllBLLayout = new PlayersAllBLLayout(proj);
        this.playersInBLLayout = new PlayersInBLLayout(proj);
        this.playersNotInBLLayout = new PlayersNotInBLLayout(proj);

        setCurrent(playersAllBLLayout);
    }

    @Override public void onViewersNotEmpty() {
        proj.getListener().registerTeamsObserver(this);
    }

    @Override public void onViewersEmpty() {
        proj.getListener().unregisterTeamsObserver(this);
    }

    public void setAllLayout() {
        setCurrent(playersAllBLLayout);
    }

    public void setInBLLayout() {
        setCurrent(playersInBLLayout);
    }

    public void setNotInBLLayout() {
        setCurrent(playersNotInBLLayout);
    }

}
