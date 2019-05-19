package dialight.guilib.layout;

import dialight.guilib.slot.Slot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReplaceableLayout<L extends SlotLayout> extends SlotLayout implements LayoutListener {

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


    @Override public void subscribe(LayoutListener listener) {
        if(optional == null) throw new IllegalStateException("You forget to set current layout in replaceable layout");
        if(this.subscribed.isEmpty()) optional.subscribe(this);
        super.subscribe(listener);
    }
    @Override public void unsubscribe(LayoutListener listener) {
        if(optional == null) throw new IllegalStateException("You forget to set current layout in replaceable layout");
        super.unsubscribe(listener);
        if(this.subscribed.isEmpty()) optional.unsubscribe(this);
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
        if(optional != null) optional.unsubscribe(this);
        optional = layout;
        if (!this.subscribed.isEmpty()) optional.subscribe(this);
        fireRefresh();
    }

    @NotNull public L getCurrent() {
        return optional;
    }

}
