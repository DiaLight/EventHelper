package dialight.teams.captain.gui.captain.team;

import dialight.guilib.elements.CachedPageElement;
import dialight.guilib.indexcache.SparkIndexCache;
import dialight.guilib.slot.Slot;
import dialight.teams.captain.SortByCaptain;
import dialight.teams.observable.ObservableScoreboard;
import dialight.teams.observable.ObservableTeam;

public class CaptainTeamElement extends CachedPageElement<ObservableTeam> {

    private final SortByCaptain proj;
    private final ObservableScoreboard scoreboard;

    public CaptainTeamElement(SortByCaptain proj, ObservableScoreboard scoreboard) {
        super(new SparkIndexCache(9, 5));
        this.proj = proj;
        this.scoreboard = scoreboard;
        this.setSlotFunction(this::createSlot);
    }

    @Override public void onViewersNotEmpty() {
        scoreboard.teamsByName().onPut(this, this::onTeamAdd);
        scoreboard.teamsByName().onRemove(this, this::onTeamRemove);
        scoreboard.teamsByName().forEach(this::onTeamAdd);

        proj.getTeams().getTeamWhiteList().onAdd(this, this::onFilterAdd);
        proj.getTeams().getTeamWhiteList().onRemove(this, this::onFilterRemove);
    }

    @Override public void onViewersEmpty() {
        scoreboard.teamsByName().removeListeners(this);
        proj.getTeams().getTeamWhiteList().removeListeners(this);

        proj.runTask(this::clear);
    }

    private void onTeamAdd(String name, ObservableTeam oteam) {
        if (proj.getTeams().getTeamWhiteList().contains(oteam.getName())) {
            add(oteam);
        }
    }
    private void onTeamRemove(String name, ObservableTeam oteam) {
        remove(oteam);
    }

    private void onFilterAdd(String name) {
        ObservableTeam oteam = scoreboard.teamsByName().get(name);
        if(oteam != null) {
            add(oteam);
        }
    }
    private void onFilterRemove(String name) {
        ObservableTeam oteam = scoreboard.teamsByName().get(name);
        if(oteam != null) {
            remove(oteam);
        }
    }

    private Slot createSlot(ObservableTeam oteam) {
        return new CaptainTeamSlot(proj, oteam);
    }

}
