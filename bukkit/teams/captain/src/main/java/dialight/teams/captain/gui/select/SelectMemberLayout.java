package dialight.teams.captain.gui.select;

import dialight.guilib.elements.NamedSetElement;
import dialight.guilib.slot.Slot;
import dialight.observable.list.ObservableList;
import dialight.misc.player.UuidPlayer;
import dialight.teams.captain.SortByCaptain;

import java.util.UUID;

public class SelectMemberLayout extends NamedSetElement<UuidPlayer, UUID> {

    private final SortByCaptain proj;

    public SelectMemberLayout(SortByCaptain proj) {
        super(6, UuidPlayer::getUuid);
        this.proj = proj;
        this.setNameFunction(UuidPlayer::getName);
        this.setSlotFunction(this::createSlot);
    }

    @Override public void onViewersNotEmpty() {
        ObservableList<UuidPlayer> unsorted = proj.getMembersHandler().getUnsorted();
        unsorted.onAdd(this, this::onAdd);
        unsorted.onRemove(this, this::onRemove);
        unsorted.forEach(this::add);
    }

    @Override public void onViewersEmpty() {
        ObservableList<UuidPlayer> unsorted = proj.getMembersHandler().getUnsorted();
        unsorted.removeListeners(this);

        proj.runTask(this::clear);
    }

    private void onAdd(UuidPlayer player, Integer index) {
        this.add(player);
    }

    private void onRemove(UuidPlayer player, Integer index) {
        this.remove(player);
    }

    private Slot createSlot(UuidPlayer player) {
        return new MemberSlot(proj, player);
    }

}
