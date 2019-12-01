package dialight.teams.captain.state;

import dialight.extensions.LocationEx;
import dialight.misc.player.UuidPlayer;
import dialight.observable.ObservableObject;
import dialight.observable.SilentWriteObservableObject;
import dialight.property.PropertyObject;
import dialight.stateengine.StateEngineHandler;
import dialight.teams.captain.SortByCaptain;
import dialight.teams.captain.SortByCaptainState;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;

public class SelectNextMemberHandler extends StateEngineHandler<SortByCaptainState> {

    private UuidPlayer currentCaptain;
    private Collection<UuidPlayer> unsorted;
    private ObservableObject<UuidPlayer> lookingAt = new ObservableObject<>();
    private SilentWriteObservableObject<UuidPlayer> selected = new SilentWriteObservableObject<>();
    private PropertyObject<Boolean> pause = new PropertyObject<>(false);
    private int time = 0;
    private int timeLimit = 600;
    private final ObservableObject<Integer> donePercent = new ObservableObject<>(0);
    private final ObservableObject<Integer> secondsLeft = new ObservableObject<>(0);


    public SelectNextMemberHandler(SortByCaptain proj) {
        super(proj, SortByCaptainState.NEXT_MEMBER);
    }

    @Override public void enter() {
        selected.silentSetValue(null);
        currentCaptain = proj.getCaptainHandler().getCurrentCaptain().getValue();
        unsorted = proj.getMembersHandler().getUnsorted();
        time = 0;
        donePercent.setValue(0);
    }

    @Override public void tick(int tick) {
        Player player = currentCaptain.getPlayer();
        if(player != null) {
            UuidPlayer lookingAt = LocationEx.of(player.getEyeLocation()).getObjByDirection(
                   1, 1.5, unsorted, up -> up.getLocation().clone().add(0, 1, 0)
            );
            this.lookingAt.setValue(lookingAt);
        }
        if(time >= timeLimit) {  // 30 * 20
            fireDone();
        }
        if(!pause.getValue()) {
            time += 1;
            if((time % 2) == 0) {
                donePercent.setValue(time * 100 / timeLimit);
                secondsLeft.setValue((timeLimit - time) / 20);
            }
        }
    }

    @Override public void leave() {
        lookingAt.setValue(null);
        Location memberLocation = proj.getArenaHandler().getMemberLocation(currentCaptain);
        currentCaptain.teleport(memberLocation);
        pause.silentSetValue(false);
        currentCaptain.clearInventory();
    }

    @Override public void clear() {
        pause.silentSetValue(false);
    }

    public ObservableObject<UuidPlayer> getLookingAt() {
        return lookingAt;
    }

    public ObservableObject<UuidPlayer> getSelected() {
        return selected;
    }

    public boolean tryConfirm() {
        if(selected.getValue() != null) {
            fireDone();
            return true;
        }
        return false;
    }

    public void selectAndConfirm(UuidPlayer target) {
        selected.silentSetValue(target);
        fireDone();
    }

    public PropertyObject<Boolean> getPause() {
        return pause;
    }

    public ObservableObject<Integer> getDonePercent() {
        return donePercent;
    }

    public ObservableObject<Integer> secondsLeft() {
        return secondsLeft;
    }

}
