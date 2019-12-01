package dialight.teams.captain.gui.control;

import dialight.misc.ActionInvoker;
import dialight.guilib.elements.FixedElement;
import dialight.observable.list.ObservableList;
import dialight.misc.player.UuidPlayer;
import dialight.teams.captain.SortByCaptain;

public class ControlLayout extends FixedElement {

    private final SortByCaptain proj;
    private final ControlSlot controlSlot;
    private final PauseSlot pauseSlot;
    private final ResultsSlot resultsSlot;

    public ControlLayout(SortByCaptain proj) {
        super(9, 6);
        this.proj = proj;
        controlSlot = new ControlSlot(proj);
        pauseSlot = new PauseSlot(proj);
        resultsSlot = new ResultsSlot(proj);
        this.setSlot(1, 1, controlSlot);
        this.setSlot(3, 1, pauseSlot);
        this.setSlot(5, 1, resultsSlot);
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
