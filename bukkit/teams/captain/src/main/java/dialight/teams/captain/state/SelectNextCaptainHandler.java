package dialight.teams.captain.state;

import dialight.observable.ObservableObject;
import dialight.misc.player.UuidPlayer;
import dialight.stateengine.StateEngineHandler;
import dialight.teams.captain.CaptainMessages;
import dialight.teams.captain.SortByCaptain;
import dialight.teams.captain.SortByCaptainState;
import dialight.teams.captain.utils.CaptainEntry;
import dialight.teams.captain.utils.CaptainMap;
import dialight.teams.observable.ObservableTeam;

public class SelectNextCaptainHandler extends StateEngineHandler<SortByCaptainState> {

    private ObservableObject<UuidPlayer> currentCaptain = new ObservableObject<>();
    private ObservableObject<ObservableTeam> currentTeam = new ObservableObject<>();
    private int nextIndex = 0;

    public SelectNextCaptainHandler(SortByCaptain proj) {
        super(proj, SortByCaptainState.NEXT_CAPTAIN);
    }

    public void setNextCaptain(UuidPlayer captain) {
        nextIndex = proj.getMembersHandler().getCaptainMap().getCaptainIndex(captain);
    }

    @Override public void enter() {
        // select next
        CaptainMap captainMap = proj.getMembersHandler().getCaptainMap();
        CaptainEntry entry = captainMap.get(nextIndex);
        UuidPlayer captain = entry.getCaptain();
        this.currentCaptain.setValue(captain);
        ObservableTeam team = proj.getScoreboard().teamsByName().get(entry.getTeamName());
        if(team == null) throw new IllegalStateException("Team with name " + entry.getTeamName() + " not found");
        this.currentTeam.setValue(team);
        nextIndex++;
        if(nextIndex >= captainMap.size()) nextIndex = 0;

        proj.getMembersHandler().broadcastMembers(CaptainMessages.nextCaptain(captain.getName(), team.color().getValue()));

        // teleport
        captain.teleport(proj.getArenaHandler().getSelectLocation());
        captain.clearInventory();

        UuidPlayer master = proj.getStateEngine().getInvoker().getPlayer();
        if(captain.equals(master)) {
            for (int i = 0; i < 8; i++) {
                captain.setItemInInventory(i, proj.getTool().createItem(team));
            }
            captain.setItemInInventory(8, proj.getTool().createItem());
        } else {
            for (int i = 0; i < 9; i++) {
                captain.setItemInInventory(i, proj.getTool().createItem(team));
            }
            if(master != null) {
                master.clearInventory();
//                master.setItemInInventory(7, proj.getTool().createItem(team));
                master.setItemInInventory(8, proj.getTool().createItem());
            }
        }

        fireDone();
    }

    @Override public void tick(int tick) {

    }

    @Override public void leave() {

    }

    @Override public void clear() {
        currentCaptain.setValue(null);
        currentTeam.setValue(null);
        nextIndex = 0;
    }

    public ObservableObject<UuidPlayer> getCurrentCaptain() {
        return currentCaptain;
    }

    public ObservableObject<ObservableTeam> getCurrentTeam() {
        return currentTeam;
    }

}
