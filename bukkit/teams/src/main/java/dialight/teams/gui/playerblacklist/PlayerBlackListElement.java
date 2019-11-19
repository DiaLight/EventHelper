package dialight.teams.gui.playerblacklist;

import dialight.guilib.elements.NamedElement;
import dialight.guilib.elements.ReplaceableElement;
import dialight.offlinelib.UuidPlayer;
import dialight.teams.Teams;

public class PlayerBlackListElement extends ReplaceableElement<NamedElement<UuidPlayer>> {

    private final Teams proj;
    private final PlayersAllBLElement playersAllBLLayout;
    private final PlayersInBLElement playersInBLLayout;
    private final PlayersNotInBLElement playersNotInBLLayout;

    public PlayerBlackListElement(Teams proj) {
        this.proj = proj;
        this.playersAllBLLayout = new PlayersAllBLElement(proj);
        this.playersInBLLayout = new PlayersInBLElement(proj);
        this.playersNotInBLLayout = new PlayersNotInBLElement(proj);

        setCurrent(playersAllBLLayout);
    }

    @Override public void onViewersNotEmpty() {

    }

    @Override public void onViewersEmpty() {

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
