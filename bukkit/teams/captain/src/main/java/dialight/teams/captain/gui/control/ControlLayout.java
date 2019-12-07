package dialight.teams.captain.gui.control;

import dialight.guilib.elements.FixedElement;
import dialight.misc.ActionInvoker;
import dialight.misc.player.UuidPlayer;
import dialight.observable.list.ObservableList;
import dialight.teams.captain.SortByCaptain;

public class ControlLayout extends FixedElement {

    private final SortByCaptain proj;
    private final ControlSlot controlSlot;
    private final PauseSlot pauseSlot;
    private final SelectCaptainSlot selectCaptain;

    public ControlLayout(SortByCaptain proj) {
        super(9, 6);
        this.proj = proj;
        controlSlot = new ControlSlot(proj);
        pauseSlot = new PauseSlot(proj);
        selectCaptain = new SelectCaptainSlot(proj);
        this.setSlot(1, 1, controlSlot);
        this.setSlot(3, 1, pauseSlot);
        this.setSlot(5, 1, selectCaptain);
    }

    @Override public void onViewersNotEmpty() {
        ObservableList<UuidPlayer> unsorted = proj.getMembersHandler().getUnsorted();
        unsorted.onAdd(this, this::onAdd);
        unsorted.onRemove(this, this::onRemove);

        proj.getMemberHandler().getPause().onChange(this, this::onPauseToggle);
    }

    private void onPauseToggle(ActionInvoker actionInvoker, Boolean fr, Boolean to) {
        pauseSlot.update();
    }

    @Override public void onViewersEmpty() {
        ObservableList<UuidPlayer> unsorted = proj.getMembersHandler().getUnsorted();
        unsorted.removeListeners(this);
    }

    private void onAdd(UuidPlayer player, Integer index) {

    }

    private void onRemove(UuidPlayer player, Integer index) {

    }

}
