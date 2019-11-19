package dialight.guilib.elements;

import dialight.guilib.UpdateListener;
import dialight.guilib.Viewable;
import dialight.guilib.slot.Slot;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates 2D Slot representation of mutable collection
 */
public abstract class SlotElement extends Viewable {

    protected final List<UpdateListener> subscribed = new ArrayList<>();

    @Nullable public abstract Slot getSlot(int x, int y);

    public void subscribe(UpdateListener listener) {
        subscribed.add(listener);
    }

    public void unsubscribe(UpdateListener listener) {
        subscribed.remove(listener);
    }

    protected void fireUpdateSlot(int x, int y, Slot slot) {
        for(UpdateListener listener : subscribed) {
            listener.updateSlot(x, y, slot);
        }
    }

    protected void fireUpdateDataBounds(int width, int height) {
        for(UpdateListener listener : subscribed) {
            listener.updateDataBounds(width, height);
        }
    }

    protected void fireRefresh() {
        for(UpdateListener listener : subscribed) {
            listener.refresh();
        }
    }

    public abstract int getWidth();
    public abstract int getHeight();

}
