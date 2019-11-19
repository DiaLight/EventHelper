package dialight.teams.gui.whitelist;

import dialight.guilib.elements.CachedPageElement;
import dialight.guilib.indexcache.SparkIndexCache;
import dialight.guilib.slot.Slot;
import dialight.teams.Teams;
import dialight.teams.observable.ObservableScoreboard;
import dialight.teams.observable.ObservableTeam;

public class TeamWhiteListElement extends CachedPageElement<String> {

    private final Teams proj;
    private final ObservableScoreboard scoreboard;

    public TeamWhiteListElement(Teams proj, ObservableScoreboard scoreboard) {
        super(new SparkIndexCache(9, 5));
        this.proj = proj;
        this.scoreboard = scoreboard;
        this.setSlotFunction(this::createSlot);
    }

    @Override public void onViewersNotEmpty() {
        scoreboard.teamsByName().onPut(this, this::onTeamAdd);
        scoreboard.teamsByName().onRemove(this, this::onTeamRemove);
        scoreboard.teamsByName().forEach(this::onTeamAdd);

        proj.getTeamWhiteList().onAdd(this, this::onFilterAdd);
        proj.getTeamWhiteList().onRemove(this, this::onFilterRemove);

        proj.getTeamWhiteList().forEach(this::add);
    }

    @Override public void onViewersEmpty() {
        scoreboard.teamsByName().removeListeners(this);
        proj.getTeamWhiteList().removeListeners(this);

        proj.runTask(this::clear);
    }

    private void onTeamAdd(String name, ObservableTeam oteam) {
        if (proj.getTeamWhiteList().contains(oteam.getName())) {
            update(oteam.getName());
        } else {
            add(oteam.getName());
        }
    }
    private void onTeamRemove(String name, ObservableTeam oteam) {
        if (proj.getTeamWhiteList().contains(oteam.getName())) {
            update(oteam.getName());
        } else {
            remove(oteam.getName());
        }
    }

    private void onFilterAdd(String name) {
        ObservableTeam oteam = scoreboard.teamsByName().get(name);
        if(oteam != null) {
            update(name);
        } else {
            add(name);
        }
    }
    private void onFilterRemove(String name) {
        ObservableTeam oteam = scoreboard.teamsByName().get(name);
        if(oteam != null) {
            update(name);
        } else {
            remove(name);
        }
    }

    private Slot createSlot(String name) {
        return new TeamWhiteListSlot(proj, scoreboard, name);
    }

}
