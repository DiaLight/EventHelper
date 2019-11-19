package dialight.guilib.elements;

import dialight.guilib.UpdateListener;
import dialight.guilib.slot.Slot;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ReplaceableElement<L extends SlotElement> extends SlotElement implements UpdateListener {

    private L optional = null;

    @Nullable @Override public Slot getSlot(int x, int y) {
        return optional.getSlot(x, y);
    }
    @Override public int getWidth() {
        return optional.getWidth();
    }
    @Override public int getHeight() {
        return optional.getHeight();
    }


    @Override public void subscribe(UpdateListener listener) {
        if(optional == null) throw new IllegalStateException("You forget to set current layout in replaceable layout");
        if(this.subscribed.isEmpty()) optional.subscribe(this);
        super.subscribe(listener);
    }
    @Override public void unsubscribe(UpdateListener listener) {
        if(optional == null) throw new IllegalStateException("You forget to set current layout in replaceable layout");
        super.unsubscribe(listener);
        if(this.subscribed.isEmpty()) optional.unsubscribe(this);
    }

    @Override public void onOpenView(Player player) {
        if(optional == null) throw new IllegalStateException("You forget to set current layout in replaceable layout");
        optional.fireOpenView(player);
    }

    @Override public void onCloseView(Player player) {
        if(optional == null) throw new IllegalStateException("You forget to set current layout in replaceable layout");
        optional.fireCloseView(player);
    }

    @Override public void updateSlot(int x, int y, @Nullable Slot slot) {
        fireUpdateSlot(x, y, slot);
    }
    @Override public void updateDataBounds(int width, int height) {
        fireUpdateDataBounds(width, height);
    }
    @Override public void refresh() {
        fireRefresh();
    }

    public void setCurrent(@NotNull L layout) {
//        if(layout == null) throw new NullPointerException();
        if(optional == layout) return;
        if(optional != null) {
            optional.unsubscribe(this);
            for (Player player : new ArrayList<>(optional.getViewers())) {
                optional.fireCloseView(player);
            }
        }
        optional = layout;
        if (!this.subscribed.isEmpty()) optional.subscribe(this);
        fireRefresh();
        for (Player player : getViewers()) {
            optional.fireOpenView(player);
        }
    }

    @NotNull public L getCurrent() {
        return optional;
    }

}
