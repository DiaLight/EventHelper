package dialight.teams.gui.teams;

import dialight.guilib.indexcache.SparkIndexCache;
import dialight.guilib.elements.CachedPageElement;
import dialight.guilib.slot.Slot;
import dialight.misc.player.UuidPlayer;
import dialight.teams.Teams;
import dialight.teams.observable.ObservableScoreboard;
import dialight.teams.observable.ObservableTeam;
import org.jetbrains.annotations.NotNull;

public class TeamsElement extends CachedPageElement<ObservableTeam> {

    @NotNull private final Teams proj;
    private final ObservableScoreboard scoreboard;

    public TeamsElement(Teams proj, ObservableScoreboard scoreboard) {
        super(new SparkIndexCache(9, 5));
//        super(new SpiralIndexCache(9, 5));
        this.proj = proj;
        this.scoreboard = scoreboard;

        this.setNameFunction(ObservableTeam::getName);
        this.setSlotFunction(this::createSlot);
    }

    private Slot createSlot(ObservableTeam oteam) {
        return new TeamSlot(proj, oteam);
    }

    @Override public void onViewersNotEmpty() {
        proj.onTeamUpdate(this, this::update);

        scoreboard.teamsByName().onPut(this, this::onAdd);
        scoreboard.teamsByName().onRemove(this, this::onRemove);

        proj.getTeamWhiteList().onAdd(this, this::onAddWhiteList);
        proj.getTeamWhiteList().onRemove(this, this::onRemoveWhiteList);

        scoreboard.teamsByMember().onPut(this, this::onAddMember);
        scoreboard.teamsByMember().onRemove(this, this::onRemoveMember);

        scoreboard.teamsByName().forEach(this::onAdd);
    }

    @Override public void onViewersEmpty() {
        proj.unregisterOnTeamUpdate(this);

        scoreboard.teamsByName().removeListeners(this);
        proj.getTeamWhiteList().removeListeners(this);
        scoreboard.teamsByMember().removeListeners(this);

        proj.runTask(this::clear);
    }

    private void onAdd(String name, ObservableTeam oteam) {
        if(proj.getTeamWhiteList().contains(oteam.getName())) {
            this.add(oteam);
        }
    }
    private void onRemove(String name, ObservableTeam oteam) {
        this.remove(oteam);
    }

    private void onAddWhiteList(String name) {
        ObservableTeam oteam = scoreboard.teamsByName().get(name);
        if(oteam != null) {
            this.add(oteam);
        }
    }

    private void onRemoveWhiteList(String name) {
        ObservableTeam oteam = scoreboard.teamsByName().get(name);
        if(oteam != null) {
            this.remove(oteam);
        }
    }

    private void onAddMember(UuidPlayer up, ObservableTeam oteam) {
        this.update(oteam);
    }

    private void onRemoveMember(UuidPlayer up, ObservableTeam oteam) {
        this.update(oteam);
    }

}
