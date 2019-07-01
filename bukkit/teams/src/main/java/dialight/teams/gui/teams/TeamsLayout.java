package dialight.teams.gui.teams;

import dialight.guilib.indexcache.SparkIndexCache;
import dialight.guilib.layout.CachedPageLayout;
import dialight.guilib.slot.Slot;
import dialight.observable.collection.ObservableCollection;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class TeamsLayout extends CachedPageLayout<ObservableTeam> {

    @NotNull private final Teams proj;
    private final Consumer<ObservableTeam> onAdd = this::onAdd;
    private final Consumer<ObservableTeam> onRemove = this::onRemove;
    private final Consumer<ObservableTeam> onTeamUpdate = this::update;
    private final Consumer<String> onAddWhiteList = this::onAddWhiteList;
    private final Consumer<String> onRemoveWhiteList = this::onRemoveWhiteList;
    private final BiConsumer<ObservableTeam, String> onAddMember = this::onAddMember;
    private final BiConsumer<ObservableTeam, String> onRemoveMember = this::onRemoveMember;

    public TeamsLayout(Teams proj) {
        super(new SparkIndexCache(9, 5));
//        super(new SpiralIndexCache(9, 5));
        this.proj = proj;

        this.setNameFunction(ObservableTeam::getName);
        this.setSlotFunction(this::createSlot);
    }

    private Slot createSlot(ObservableTeam oteam) {
        return new TeamSlot(proj, oteam);
    }

    @Override public void onViewersNotEmpty() {
        proj.getListener().registerTeamsObserver(this);

        proj.onTeamUpdate(onTeamUpdate);

        ObservableCollection<ObservableTeam> teams = proj.getTeamsInternal();
        teams.onAdd(onAdd);
        teams.onRemove(onRemove);

        ObservableCollection<String> teamWhiteList = proj.getTeamWhiteList();
        teamWhiteList.onAdd(onAddWhiteList);
        teamWhiteList.onRemove(onRemoveWhiteList);

        proj.onMemberJoin(onAddMember);
        proj.onMemberLeave(onRemoveMember);

        teams.forEach(onAdd);
    }

    @Override public void onViewersEmpty() {
        proj.getListener().unregisterTeamsObserver(this);

        proj.unregisterOnTeamUpdate(onTeamUpdate);

        ObservableCollection<ObservableTeam> teams = proj.getTeamsInternal();
        teams.unregisterOnAdd(onAdd);
        teams.unregisterOnRemove(onRemove);

        ObservableCollection<String> teamWhiteList = proj.getTeamWhiteList();
        teamWhiteList.unregisterOnAdd(onAddWhiteList);
        teamWhiteList.unregisterOnRemove(onRemoveWhiteList);

        proj.unregisterOnMemberJoin(onAddMember);
        proj.unregisterOnMemberLeave(onRemoveMember);

        proj.runTask(this::clear);
    }

    private void onAdd(ObservableTeam oteam) {
        if(proj.getTeamWhiteList().contains(oteam.getName())) {
            this.add(oteam);
        }
    }
    private void onRemove(ObservableTeam oteam) {
        this.remove(oteam);
    }

    private void onAddWhiteList(String name) {
        ObservableTeam oteam = proj.get(name);
        if(oteam != null) {
            this.add(oteam);
        }
    }

    private void onRemoveWhiteList(String name) {
        ObservableTeam oteam = proj.get(name);
        if(oteam != null) {
            this.remove(oteam);
        }
    }

    private void onAddMember(ObservableTeam oteam, String member) {
        this.update(oteam);
    }

    private void onRemoveMember(ObservableTeam oteam, String member) {
        this.update(oteam);
    }

}
